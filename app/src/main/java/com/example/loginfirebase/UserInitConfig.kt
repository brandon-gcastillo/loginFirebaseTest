package com.example.loginfirebase

import android.Manifest
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.core.content.ContextCompat
import com.example.loginfirebase.databinding.ActivityUserInitConfigBinding

class UserInitConfig : AppCompatActivity() {

    private lateinit var binding: ActivityUserInitConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityUserInitConfigBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        with(binding) {

            val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if(result.resultCode == RESULT_OK) {
                    val uriImage = result.data?.data
                    imageView3.setImageURI(uriImage)
                }
            }

            changePictureFabBtn.setOnClickListener {
                try {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    pickImage.launch(intent)
                } catch (exception: Exception) {
                    Log.w("Issue", exception)
                }
            }
        }
    }
}