package com.raassh.gemastik15.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.raassh.gemastik15.BuildConfig
import com.raassh.gemastik15.R
import java.text.SimpleDateFormat
import java.util.*

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
                else if (isFull) {
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

fun convertPixelsToDp(px: Float, context: Context): Float {
    return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun getDisplayDp(context: Context): Pair<Int, Int> {
    val displayMetrics = context.resources.displayMetrics
    val widthDp = displayMetrics.widthPixels / displayMetrics.density
    val heightDp = displayMetrics.heightPixels / displayMetrics.density
    return Pair(widthDp.toInt(), heightDp.toInt())
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

fun Long.toDateText(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}

fun setTheme(theme: String) {
    when(theme) {
        "MODE_NIGHT_FOLLOW_SYSTEM" -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        "MODE_NIGHT_NO" -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        "MODE_NIGHT_YES" -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        "MODE_NIGHT_AUTO_BATTERY" -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
        else -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}

@ColorInt
fun Context.getColorFromAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    val colorRes = typedValue.resourceId
    return ContextCompat.getColor(this, colorRes)
}