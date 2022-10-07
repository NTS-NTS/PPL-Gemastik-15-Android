package com.raassh.gemastik15.utils

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.raassh.gemastik15.R

fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, length).show()
}

fun TextInputLayout.validate(
    name: String,
    validation: ((String) -> String?)? = null
): Boolean {
    val inputted = editText?.text.toString()
    if (inputted.isBlank()) {
        error = this.context.getString(R.string.input_empty, name)
        return false
    }

    error = validation?.invoke(inputted)

    return error == null
}

fun Context.checkPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED