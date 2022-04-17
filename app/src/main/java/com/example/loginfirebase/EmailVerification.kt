package com.example.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginfirebase.databinding.EmailVerificationBinding

class EmailVerification : AppCompatActivity() {

    private lateinit var binding: EmailVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EmailVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            this.finish()
        }
    }
}