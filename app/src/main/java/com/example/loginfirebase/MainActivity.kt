package com.example.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.core.widget.doOnTextChanged
import com.example.loginfirebase.databinding.MainActivityBinding
import com.example.loginfirebase.uielements.Loading
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

enum class ProviderType {
    BASIC
}

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
            createAccountLink.setOnClickListener { startCreateAccountFrame() }
            btnLogin.setOnClickListener {
                val emailValue = emailField.text.toString()
                val passwordValue = passwordField.text.toString()
                signIn(emailValue, passwordValue)
            }

            emailField.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    cleanErrors(emailField, emailFieldLayout)
                }
            }

            passwordField.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    cleanErrors(passwordField, passwordFieldLayout)
                }
            }
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
        if(!validateForm(email, password)) return

        loading.startDialog()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    loading.isDismiss()
                    redirectToHome(task.result?.user?.email ?: "", ProviderType.BASIC)
                } else {
                    loading.isDismiss()
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun redirectToHome(email: String, providerType: ProviderType) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("providerType", providerType.name)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        this.finish()
    }

    private fun validateForm(
        email: String,
        password: String,
    ): Boolean {
        var valid = true

        if(TextUtils.isEmpty(email)) {
            binding.emailField.error = "This field is required."
            binding.emailFieldLayout.helperText = getString(R.string.signForm_helperText)
            binding.emailFieldLayout.isHelperTextEnabled = true
            valid = false
        } else {
            binding.emailField.error = null
            binding.emailFieldLayout.helperText = getString(R.string.blank)
            binding.emailFieldLayout.isHelperTextEnabled = false
        }

        if(TextUtils.isEmpty(password)) {
            binding.passwordFieldLayout.helperText = getString(R.string.signForm_helperText)
            binding.passwordFieldLayout.isHelperTextEnabled = true
            valid = false
        } else {
            binding.passwordField.error = null
            binding.passwordFieldLayout.helperText = getString(R.string.blank)
            binding.passwordFieldLayout.isHelperTextEnabled = false
        }

        return valid
    }

    private fun cleanErrors(inputText: TextInputEditText, inputLayout: TextInputLayout) {
        inputText.error = null
        inputLayout.isHelperTextEnabled = false
        inputLayout.helperText = getString(R.string.blank)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}