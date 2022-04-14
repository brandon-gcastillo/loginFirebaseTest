package com.example.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.loginfirebase.databinding.CreateAccountBinding
import com.example.loginfirebase.uielements.Loading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: CreateAccountBinding

    private lateinit var auth: FirebaseAuth

    // Loading dialog
    private val loading = Loading(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        with (binding) {
            val emailValue = newEmailField.text.toString().trim()
            val passwordValue = newPasswordField.text.toString().trim()
            btnCreateAccount.setOnClickListener { createAccount(emailValue, passwordValue) }
            loginAccountLink.setOnClickListener { startLoginAccountFrame() }
        }
    }

    private fun startLoginAccountFrame() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("loginAccount", binding.loginAccountLink.text.toString())
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        this.finish()
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "Create Account: $email")
        if (!validateForm()) return

        loading.startDialog()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    loading.isDismiss()
                    redirectToHome()
                } else {
                    Log.w(TAG, "createAccountWithEmail:failure", it.exception)
                    loading.isDismiss()
                    Toast.makeText(this, "Registration failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun redirectToHome() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("user", binding.newEmailField.text.toString())
        }
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.newEmailField.text.toString().trim()
        if(TextUtils.isEmpty(email)) {
            binding.newEmailField.error = "This field is required."
            valid = false
        } else {
            binding.newEmailField.error = null
        }

        val password = binding.newPasswordField.text.toString().trim()
        val confirmPassword = binding.confirmPasswordField.text.toString().trim()
        if(TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(password)) {
            binding.newPasswordField.error = "This field is required."
            binding.confirmPasswordField.error = "This field is required."
            valid = false
        } else if(password != confirmPassword) {
            binding.newPasswordField.error = "Passwords do not match. Please try again."
            valid = false
        } else {
            binding.newPasswordField.error = null
            binding.confirmPasswordField.error = null
        }

        return valid
    }

    companion object {
        private const val TAG = "CreateAccountStatus"
    }


}