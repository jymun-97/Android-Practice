package com.mjy.customdialog_practice

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class CustomDialog(
    context: Context,
    customDialogInterface: CustomDialogInterface
): Dialog(context) {

    private var customDialogInterface: CustomDialogInterface? = null

    init {
        this.customDialogInterface = customDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom)

        findViewById<AppCompatButton>(R.id.likeButton).setOnClickListener {
            customDialogInterface?.onLikeButtonClicked()
        }
        findViewById<AppCompatButton>(R.id.disLikeButton).setOnClickListener {
            customDialogInterface?.onDisLikeButtonClicked()
        }
    }
}