package com.raassh.gemastik15.utils

import android.util.Patterns

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = this.length >= 8

fun Double.rounded(places: Int) = String.format("%.${places}f", this)

fun String.splitWithEmptyList(separator: String) = split(separator)
    .takeIf { it.size > 1 || it[0].isNotEmpty() }
    ?: emptyList()