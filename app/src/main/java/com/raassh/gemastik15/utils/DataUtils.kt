package com.raassh.gemastik15.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.PlacesItem
import com.raassh.gemastik15.local.db.PlaceEntity
import java.util.*

fun placeItemToEntity(placesItem: PlacesItem): PlaceEntity {
    return PlaceEntity(
        placesItem.id,
        placesItem.name,
        placesItem.kind,
        placesItem.image,
        placesItem.distance,
        placesItem.facilities.joinToString(",") {
            it.name
        },
        placesItem.latitude,
        placesItem.longitude,
        Calendar.getInstance().timeInMillis
    )
}

// this looks bad, should be refactored later if possible
fun Context.translateDBtoViewName(name: String): String {
    return when (name) {
        "stair_lift" -> getString(R.string.stair_lift)
        "accessible_entrance" -> getString(R.string.accessible_entrance)
        "accessible_furniture" -> getString(R.string.accessible_furniture)
        "lift" -> getString(R.string.lift)
        "accessible_space" -> getString(R.string.accessible_space)
        "assistive_listening_device" -> getString(R.string.assistive_listening)
        "audio_control" -> getString(R.string.audio_control)
        "audio_output" -> getString(R.string.audio_output)
        "audio_wayfinder" -> getString(R.string.audio_wayfinder)
        "railing" -> getString(R.string.railing)
        "braille_button" -> getString(R.string.braille_button)
        "braille_signage" -> getString(R.string.braille_signage)
        "clear_signage" -> getString(R.string.clear_signage)
        "display" -> getString(R.string.display)
        "guiding_blocks" -> getString(R.string.guiding_blocks)
        "parking" -> getString(R.string.parking)
        "ramp" -> getString(R.string.ramp)
        "sign_language" -> getString(R.string.sign_language)
        "tty" -> getString(R.string.tty)
        "sitting_toilet" -> getString(R.string.sitting_toilet)
        "tv_text" -> getString(R.string.tv_text)
        "wheelchair_area" -> getString(R.string.wheelchair_area)
        "wheelchair_service" -> getString(R.string.wheelchair_service)
        else -> name
    }
}

fun Context.translateViewtoDBName(name: String): String {
    return when (name) {
        getString(R.string.accessible_entrance) -> "accessible_entrance"
        getString(R.string.accessible_furniture) -> "accessible_furniture"
        getString(R.string.accessible_space) -> "accessible_space"
        getString(R.string.assistive_listening) -> "assistive_listening_device"
        getString(R.string.audio_control) -> "audio_control"
        getString(R.string.audio_output) -> "audio_output"
        getString(R.string.audio_wayfinder) -> "audio_wayfinder"
        getString(R.string.railing) -> "bar"
        getString(R.string.braille_button) -> "braille_button"
        getString(R.string.braille_signage) -> "braille_signage"
        getString(R.string.clear_signage) -> "clear_signage"
        getString(R.string.display) -> "display"
        getString(R.string.guiding_blocks) -> "guiding_blocks"
        getString(R.string.lift) -> "lift"
        getString(R.string.parking) -> "parking"
        getString(R.string.ramp) -> "ramp"
        getString(R.string.sign_language) -> "sign_language"
        getString(R.string.stair_lift) -> "stair_lift"
        getString(R.string.tty) -> "tty"
        getString(R.string.tv_text) -> "tv_text"
        getString(R.string.wheelchair_area) -> "wheelchair_area"
        getString(R.string.wheelchair_service) -> "wheelchair_service"
        getString(R.string.sitting_toilet) -> "sitting_toilet"
        else -> name
    }
}

fun Context.getFacilityDrawable(name: String) : Drawable? {
    return when (name) {
        getString(R.string.accessible_entrance) -> ContextCompat.getDrawable(this, R.drawable.accessible_entrance)
        getString(R.string.accessible_furniture) ->  ContextCompat.getDrawable(this, R.drawable.accessible_furniture)
        getString(R.string.accessible_space) ->  ContextCompat.getDrawable(this, R.drawable.accessible_space)
        getString(R.string.assistive_listening) ->  ContextCompat.getDrawable(this, R.drawable.assistive_listening_device)
        getString(R.string.audio_control) ->  ContextCompat.getDrawable(this, R.drawable.audio_control)
        getString(R.string.audio_output) ->  ContextCompat.getDrawable(this, R.drawable.audio_output)
        getString(R.string.audio_wayfinder) ->  ContextCompat.getDrawable(this, R.drawable.audio_wayfinder)
        getString(R.string.railing) ->  ContextCompat.getDrawable(this, R.drawable.railing)
        getString(R.string.braille_button) ->  ContextCompat.getDrawable(this, R.drawable.braille_button)
        getString(R.string.braille_signage) ->  ContextCompat.getDrawable(this, R.drawable.braille_signage)
        getString(R.string.clear_signage) ->  ContextCompat.getDrawable(this, R.drawable.clear_signage)
        getString(R.string.display) ->  ContextCompat.getDrawable(this, R.drawable.display)
        getString(R.string.guiding_blocks) ->  ContextCompat.getDrawable(this, R.drawable.guiding_blocks)
        getString(R.string.lift) ->  ContextCompat.getDrawable(this, R.drawable.lift)
        getString(R.string.parking) ->  ContextCompat.getDrawable(this, R.drawable.parking)
        getString(R.string.ramp) ->  ContextCompat.getDrawable(this, R.drawable.ramp)
        getString(R.string.sign_language) ->  ContextCompat.getDrawable(this, R.drawable.sign_language)
        getString(R.string.stair_lift) ->  ContextCompat.getDrawable(this, R.drawable.stair_lift)
        getString(R.string.tty) ->  ContextCompat.getDrawable(this, R.drawable.tty)
        getString(R.string.tv_text) ->  ContextCompat.getDrawable(this, R.drawable.tv_text)
        getString(R.string.wheelchair_area) ->  ContextCompat.getDrawable(this, R.drawable.wheelchair_area)
        getString(R.string.wheelchair_service) ->  ContextCompat.getDrawable(this, R.drawable.wheelchair_service)
        getString(R.string.sitting_toilet) ->  ContextCompat.getDrawable(this, R.drawable.sitting_toilet)
        else -> null
    }
}