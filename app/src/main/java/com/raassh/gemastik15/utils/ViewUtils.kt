package com.raassh.gemastik15.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.*
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.raassh.gemastik15.BuildConfig
import com.raassh.gemastik15.R

fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT, anchor: View? = null) {
    Snackbar.make(this, message, length).apply {
        if (anchor != null) {
            anchorView = anchor
        }
        show()
    }
}

fun TextInputLayout.validate(
    name: String,
    optional: Boolean = false,
    validation: ((String) -> String?)? = null
): Boolean {
    val inputted = editText?.text.toString()
    if (!optional && inputted.isBlank()) {
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

fun ImageView.loadImage(
    ref: String?,
    placeholder: Int = R.drawable.place_photo_placeholder,
    isGmpRequest: Boolean = false)
{
    var url = ref
    if (isGmpRequest) {
        url = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
            .append("?maxwidth=400")
            .append("&photoreference=$ref")
            .append("&key=${BuildConfig.MAPS_API_KEY}").toString()
    }

    Glide.with(this.context)
        .load(url ?: "")
        .placeholder(placeholder)
        .error(placeholder)
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
    private val isFull: Boolean = false
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spacing = convertDpToPixel(spacingInDp, parent.context).toInt()
        val screenMargin = convertDpToPixel(20, parent.context).toInt()

        outRect.left = 0
        outRect.right = 0
        outRect.top = 0
        outRect.bottom = 0

        when(orientation) {
            HORIZONTAL -> {
                if (position > 0) {
                    outRect.left = spacing
                } else if (position == 0) {
                    outRect.left = screenMargin
                }
                if (position == state.itemCount - 1) outRect.right = screenMargin
            }
            VERTICAL -> {
                if (!isFull && position > 0) outRect.top = spacing
                else {
                    if (position == 0) outRect.top = spacing
                    outRect.bottom = spacing
                }
            }
        }
    }
}

fun convertDpToPixel(dp: Int, context: Context): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.uriToBitmap(uri: Uri): Bitmap {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(contentResolver, uri)
        return ImageDecoder.decodeBitmap(source)
    } else {
        @Suppress("DEPRECATION")
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
}