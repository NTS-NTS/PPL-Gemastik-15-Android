package com.raassh.gemastik15.utils

import android.util.Patterns

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = this.length >= 8