package com.example.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.loginfirebase.databinding.CreateAccountBinding

class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: CreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginAccountLink.setOnClickListener { startLoginAccountFrame() }
    }

    private fun startLoginAccountFrame() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("loginAccount", binding.loginAccountLink.text.toString())
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        this.finish()
    }
}