package com.example.loginfirebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loginfirebase.databinding.ActivityUserInitConfigBinding
import com.example.loginfirebase.uielements.Loading
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class UserInitConfig : AppCompatActivity() {

    private lateinit var binding: ActivityUserInitConfigBinding

    private lateinit var uriImage: Uri

    private lateinit var fireDb: FirebaseFirestore

    private lateinit var storage: FirebaseStorage

    // Loading view
    private val loading = Loading(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserInitConfigBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        // Access a Cloud Firestore instance from your Activity
        fireDb = Firebase.firestore

        // Access to the Cloud Storage instance from the Activity
        storage = Firebase.storage

        with(binding) {

            val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if(result.resultCode == RESULT_OK) {
                    uriImage = result.data?.data!!
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

            doneBtn.setOnClickListener {
                val username = userNameTextInput.text.toString().trim()
                val firstname = firstNameTextInput.text.toString().trim()
                val lastname = lastNameTextInput.text.toString().trim()

                uploadUserConfig(
                    username,
                    firstname,
                    lastname
                )
            }
        }
    }

    private fun uploadUserConfig(
        username: String,
        firstname: String,
        lastname: String
    ) {
        // create a new user with a username, firstname and lastname
        val user = hashMapOf(
            "username" to username,
            "firstname" to firstname,
            "lastname" to lastname
        )

        val userAuth = Firebase.auth.currentUser

        loading.startDialog()

        val docRef = fireDb.collection("users").document("${userAuth?.uid}")

        docRef.set(user)
            .addOnCompleteListener {
                loading.isDismiss()
                uploadImage()
                Log.d(TAG, "DocumentSnapshot added with ID: ${taskId.toString()}")
                Toast.makeText(
                    this,
                    "Information registered successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                loading.isDismiss()
                Log.w(TAG, "Error adding document", e)
                Snackbar.make(
                    binding.root,
                    "Something went wrong. Try again.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        /*fireDb.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                loading.isDismiss()
                uploadImage()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(
                    this,
                    "Information registered successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                loading.isDismiss()
                Log.w(TAG, "Error adding document", e)
                Snackbar.make(
                    binding.root,
                    "Something went wrong. Try again.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }*/
    }

    private fun uploadImage() {

        val user = Firebase.auth.currentUser

        user?.let {
            val uid = it.uid

            val storageRef = storage.reference

            // Create a child reference
            // imagesRef now points to "images/UUID"
            val imagesRef: StorageReference = storageRef.child("images/${uid}/profile_pic.png")
            imagesRef.putFile(uriImage)
                .addOnCompleteListener { taskSnapshot ->
                    Log.d(TAGImg, "Image uploaded successfully. ${taskSnapshot.result}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAGImg, "Something went wrong uploading the image.", e)
                }
        }

    }

    companion object {
        private const val TAG = "DocSnapshot"
        private const val TAGImg = "ImgSnapshot"
    }
}