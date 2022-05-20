package com.mjy.customdialog_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity(), CustomDialogInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity - onCreate() called");
        findViewById<AppCompatButton>(R.id.dialogButton).setOnClickListener {
            val customDialog = CustomDialog(this, this)
            customDialog.show()
        }
    }

    override fun onLikeButtonClicked() {
        Toast.makeText(this, "LIKE", Toast.LENGTH_SHORT).show()
    }

    override fun onDisLikeButtonClicked() {
        Toast.makeText(this, "DISLIKE", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "#Log"
    }
}