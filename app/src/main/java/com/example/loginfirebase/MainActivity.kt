package com.example.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfirebase.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.createAccountLink.setOnClickListener { startCreateAccountFrame() }
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
        if(TextUtils.isEmpty(email)) {
            binding.emailField.error = "This field is required."
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