package com.example.tfhmobile.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Process
import android.widget.Button
import androidx.fragment.app.DialogFragment


class InternetConnectionDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).setCancelable(false)

        builder.setMessage("Internet connection lost!")
            .setPositiveButton("Retry") { _, _ -> }
            .setNegativeButton("Exit") { _, _ ->
                val pid = Process.myPid()
                Process.killProcess(pid)
            }

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        val alertDialog = dialog as AlertDialog?
        if (alertDialog != null) {
            val positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE) as Button
            positiveButton.setOnClickListener {
                val wantToCloseDialog = false
                if (wantToCloseDialog)
                    dismiss()
            }
        }
    }
}