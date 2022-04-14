package com.example.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfirebase.databinding.MainActivityBinding
import com.example.loginfirebase.uielements.Loading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private lateinit var auth: FirebaseAuth

    // Loading Dialog
    private val loading = Loading(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth

        with(binding) {
            val emailValue = emailField.text.toString()
            val passwordValue = passwordField.text.toString()
            createAccountLink.setOnClickListener { startCreateAccountFrame() }
            btnLogin.setOnClickListener { signIn(emailValue, passwordValue) }
        }
    }

    private fun startCreateAccountFrame() {
        val intent = Intent(this, CreateAccountActivity::class.java).apply {
            putExtra("createAccount", binding.createAccountLink.text.toString())
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        this.finish()
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "Sign In: $email")
        if(!validateForm()) return

        loading.startDialog()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    loading.isDismiss()
                    redirectToHome()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", it.exception)
                    loading.isDismiss()
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun redirectToHome() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("user", binding.emailField.text.toString())
        }
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.emailField.text.toString().trim()
        if(TextUtils.isEmpty(email)) {
            binding.emailField.error = "This field is required."
            valid = false
        } else {
            binding.emailField.error = null
        }

        val password = binding.passwordField.text.toString().trim()
        if(TextUtils.isEmpty(password)) {
            binding.passwordField.error = "This field is required."
            valid = false
        } else {
            binding.passwordField.error = null
        }

        return valid
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}