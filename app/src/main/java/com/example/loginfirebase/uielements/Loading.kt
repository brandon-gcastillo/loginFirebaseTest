package com.example.loginfirebase.uielements

import android.app.Activity
import android.app.AlertDialog
import com.example.loginfirebase.R

class Loading(private val currentActivity: Activity) {
    private lateinit var dialog: AlertDialog

    fun startDialog() {
        // Set View
        val inflater = currentActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_item, null)
        // Set Dialog
        val builder = AlertDialog.Builder(currentActivity)
        builder.setView(dialogView)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
    }
    fun isDismiss() {
        dialog.dismiss()
    }
}