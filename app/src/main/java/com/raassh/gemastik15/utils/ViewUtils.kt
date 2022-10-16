package com.raassh.gemastik15.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
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

fun EditText.on(actionId: Int, func: () -> Unit) {
    setOnEditorActionListener { _, receivedActionId, _ ->
        if (actionId == receivedActionId) {
            func()
            return@setOnEditorActionListener true
        }

        false
    }
}

fun getCheckedFacilities(
    group: ViewGroup,
    facilities: MutableList<String>
) {
    for (i in 0 until group.childCount) {
        val child = group.getChildAt(i)
        if (child is CheckBox && child.isChecked) {
            facilities.add(child.text.toString())
        }
    }
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .into(this)
}

class GridSpaceItemDecoration(
    private val spanCount: Int,
    private val spacingInDp: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spacing = convertDpToPixel(spacingInDp, parent.context)

        outRect.left = 0
        outRect.right = 0
        outRect.top = 0
        outRect.bottom = 0

        if (position >= 0) {
            val column = position % spanCount
            outRect.left = (spacing * column).toInt()
            if (position >= spanCount) {
                outRect.top = (spacing / 4).toInt()
            }
        }
    }
}

class LinearSpaceItemDecoration(
    private val spacingInDp: Int,
    private val orientation: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spacing = convertDpToPixel(spacingInDp, parent.context)

        outRect.left = 0
        outRect.right = 0
        outRect.top = 0
        outRect.bottom = 0

        if (position > 0) {
            when(orientation) {
                HORIZONTAL -> outRect.left = spacing.toInt()
                VERTICAL -> outRect.top = spacing.toInt()
            }
        }
    }
}

private fun convertDpToPixel(dp: Int, context: Context): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}