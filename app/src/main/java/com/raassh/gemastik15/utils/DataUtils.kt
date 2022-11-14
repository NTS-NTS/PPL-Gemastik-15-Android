package com.raassh.gemastik15.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.FacilitiesItem
import com.raassh.gemastik15.api.response.PlacesItem
import com.raassh.gemastik15.local.db.PlaceEntity
import java.text.SimpleDateFormat
import java.util.*

fun placeItemToEntity(placesItem: PlacesItem): PlaceEntity {
    return PlaceEntity(
        placesItem.id,
        placesItem.name,
        placesItem.kind,
        placesItem.image,
        placesItem.distance,
        placesItem.facilities.joinToString(",") {
            StringBuilder().append(it.name).append(":").append(it.quality.toInt()).toString()
        },
        placesItem.latitude,
        placesItem.longitude,
        Calendar.getInstance().timeInMillis
    )
}

// this looks bad, should be refactored later if possible
// update: this can use Context.resources.getIdentifier but that method is discouraged
// so I'll leave it as is for now
fun Context.translateFacilitytoView(name: String): String {
    return when (name) {
        "stair_lift" -> getString(R.string.stair_lift)
        "accessible_entrance" -> getString(R.string.accessible_entrance)
        "accessible_furniture" -> getString(R.string.accessible_furniture)
        "escalator" -> getString(R.string.escalator)
        "lift" -> getString(R.string.lift)
        "accessible_space" -> getString(R.string.accessible_space)
        "assistive_listening_device" -> getString(R.string.assistive_listening_device)
        "audio_control" -> getString(R.string.audio_control)
        "audio_output" -> getString(R.string.audio_output)
        "audio_wayfinder" -> getString(R.string.audio_wayfinder)
        "railing" -> getString(R.string.railing)
        "braille_button" -> getString(R.string.braille_button)
        "braille_signage" -> getString(R.string.braille_signage)
        "clear_signage" -> getString(R.string.clear_signage)
        "display" -> getString(R.string.display)
        "guiding_blocks" -> getString(R.string.guiding_blocks)
        "parking" -> getString(R.string.disabled_parking)
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

fun Context.translateFacilityFromView(name: String): String {
    return when (name) {
        getString(R.string.accessible_entrance) -> "accessible_entrance"
        getString(R.string.accessible_furniture) -> "accessible_furniture"
        getString(R.string.escalator) -> "escalator"
        getString(R.string.accessible_space) -> "accessible_space"
        getString(R.string.assistive_listening_device) -> "assistive_listening_device"
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
        getString(R.string.disabled_parking) -> "parking"
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
        getString(R.string.assistive_listening_device) ->  ContextCompat.getDrawable(this, R.drawable.assistive_listening_device)
        getString(R.string.audio_control) ->  ContextCompat.getDrawable(this, R.drawable.audio_control)
        getString(R.string.audio_output) ->  ContextCompat.getDrawable(this, R.drawable.audio_output)
        getString(R.string.audio_wayfinder) ->  ContextCompat.getDrawable(this, R.drawable.audio_wayfinder)
        getString(R.string.escalator) ->  ContextCompat.getDrawable(this, R.drawable.escalator)
        getString(R.string.railing) ->  ContextCompat.getDrawable(this, R.drawable.railing)
        getString(R.string.braille_button) ->  ContextCompat.getDrawable(this, R.drawable.braille_button)
        getString(R.string.braille_signage) ->  ContextCompat.getDrawable(this, R.drawable.braille_signage)
        getString(R.string.clear_signage) ->  ContextCompat.getDrawable(this, R.drawable.clear_signage)
        getString(R.string.display) ->  ContextCompat.getDrawable(this, R.drawable.display)
        getString(R.string.guiding_blocks) ->  ContextCompat.getDrawable(this, R.drawable.guiding_blocks)
        getString(R.string.lift) ->  ContextCompat.getDrawable(this, R.drawable.lift)
        getString(R.string.disabled_parking) ->  ContextCompat.getDrawable(this, R.drawable.parking)
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

fun Context.getFacilitiesGroup(facilities: List<FacilitiesItem>) : List<List<FacilitiesItem>> {
    val facilitiesGroup = mutableListOf<List<FacilitiesItem>>()
    val mobilityFacilities = mutableListOf<FacilitiesItem>()
    val visionFacilities = mutableListOf<FacilitiesItem>()
    val audioFacilities = mutableListOf<FacilitiesItem>()

    facilities.forEach {
        when (translateFacilitytoView(it.name)) {
            getString(R.string.accessible_entrance),
            getString(R.string.accessible_furniture),
            getString(R.string.accessible_space),
            getString(R.string.disabled_parking),
            getString(R.string.escalator),
            getString(R.string.railing),
            getString(R.string.sitting_toilet),
            getString(R.string.lift),
            getString(R.string.ramp),
            getString(R.string.stair_lift),
            getString(R.string.wheelchair_area),
            getString(R.string.wheelchair_service) -> mobilityFacilities.add(it)
            getString(R.string.braille_button),
            getString(R.string.braille_signage),
            getString(R.string.clear_signage),
            getString(R.string.audio_wayfinder),
            getString(R.string.audio_output),
            getString(R.string.guiding_blocks) -> visionFacilities.add(it)
            getString(R.string.assistive_listening_device),
            getString(R.string.audio_control),
            getString(R.string.sign_language),
            getString(R.string.display),
            getString(R.string.tty),
            getString(R.string.tv_text) -> audioFacilities.add(it)
        }
    }

    mobilityFacilities.sortByDescending { it.quality }
    visionFacilities.sortByDescending { it.quality }
    audioFacilities.sortByDescending { it.quality }

    facilitiesGroup.add(mobilityFacilities)
    facilitiesGroup.add(visionFacilities)
    facilitiesGroup.add(audioFacilities)

    return facilitiesGroup
}

fun Context.getFacilityReviewDrawable(quality: Int, isColored: Boolean = true) : Drawable? {
    var drawable: Drawable? = null
    when (quality) {
        0 -> drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_not_exist_20)
        1 -> drawable = ContextCompat.getDrawable(this, R.drawable.ic_outline_thumb_down_20)
        2 -> drawable = ContextCompat.getDrawable(this, R.drawable.ic_outline_thumb_up_20)
    }

    if (isColored) drawable?.setTint(getFacilityReviewColor(quality))

    return drawable
}

fun Context.getFacilityReviewColor(quality: Int) = when (quality) {
    0 -> MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurfaceVariant, Color.BLACK)
    1 -> MaterialColors.getColor(this, com.google.android.material.R.attr.colorError, Color.BLACK)
    2 -> MaterialColors.getColor(this, R.attr.colorGreen, Color.BLACK)
    else -> MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurfaceVariant, Color.BLACK)
}

fun Context.getFacilityReviewDescription(quality: Int) : String {
    return when (quality) {
        0 -> getString(R.string.not_exist)
        1 -> getString(R.string.quality) + " " + getString(R.string.bad)
        2 -> getString(R.string.quality) + " " + getString(R.string.good)
        else -> ""
    }
}

fun Context.translatePlaceTypeNameToView(type: String) : String {
    return when (type) {
        "accounting" -> getString(R.string.accounting)
        "airport" -> getString(R.string.airport)
        "amusement_park" -> getString(R.string.amusement_park)
        "aquarium" -> getString(R.string.aquarium)
        "art_gallery" -> getString(R.string.art_gallery)
        "atm" -> getString(R.string.atm)
        "bakery" -> getString(R.string.bakery)
        "bank" -> getString(R.string.bank)
        "bar" -> getString(R.string.bar)
        "beauty_salon" -> getString(R.string.beauty_salon)
        "bicycle_store" -> getString(R.string.bicycle_store)
        "book_store" -> getString(R.string.book_store)
        "bowling_alley" -> getString(R.string.bowling_alley)
        "bus_station" -> getString(R.string.bus_station)
        "cafe" -> getString(R.string.cafe)
        "campground" -> getString(R.string.campground)
        "car_dealer" -> getString(R.string.car_dealer)
        "car_rental" -> getString(R.string.car_rental)
        "car_repair" -> getString(R.string.car_repair)
        "car_wash" -> getString(R.string.car_wash)
        "casino" -> getString(R.string.casino)
        "cemetery" -> getString(R.string.cemetery)
        "church" -> getString(R.string.church)
        "city_hall" -> getString(R.string.city_hall)
        "clothing_store" -> getString(R.string.clothing_store)
        "convenience_store" -> getString(R.string.convenience_store)
        "courthouse" -> getString(R.string.courthouse)
        "dentist" -> getString(R.string.dentist)
        "department_store" -> getString(R.string.department_store)
        "doctor" -> getString(R.string.doctor)
        "drugstore" -> getString(R.string.drugstore)
        "electrician" -> getString(R.string.electrician)
        "electronics_store" -> getString(R.string.electronics_store)
        "embassy" -> getString(R.string.embassy)
        "fire_station" -> getString(R.string.fire_station)
        "florist" -> getString(R.string.florist)
        "funeral_home" -> getString(R.string.funeral_home)
        "furniture_store" -> getString(R.string.furniture_store)
        "gas_station" -> getString(R.string.gas_station)
        "grocery", "grocery_or_supermarket" -> getString(R.string.grocery_or_supermarket)
        "gym" -> getString(R.string.gym)
        "hair_care" -> getString(R.string.hair_care)
        "hardware_store" -> getString(R.string.hardware_store)
        "hindu_temple" -> getString(R.string.hindu_temple)
        "home_goods_store" -> getString(R.string.home_goods_store)
        "hospital" -> getString(R.string.hospital)
        "insurance_agency" -> getString(R.string.insurance_agency)
        "jewelry_store" -> getString(R.string.jewelry_store)
        "laundry" -> getString(R.string.laundry)
        "lawyer" -> getString(R.string.lawyer)
        "library" -> getString(R.string.library)
        "light_rail_station" -> getString(R.string.light_rail_station)
        "liquor_store" -> getString(R.string.liquor_store)
        "local_government_office" -> getString(R.string.local_government_office)
        "locksmith" -> getString(R.string.locksmith)
        "lodging" -> getString(R.string.lodging)
        "meal_delivery" -> getString(R.string.meal_delivery)
        "meal_takeaway" -> getString(R.string.meal_takeaway)
        "mosque" -> getString(R.string.mosque)
        "movie_rental" -> getString(R.string.movie_rental)
        "movie_theater" -> getString(R.string.movie_theater)
        "moving_company" -> getString(R.string.moving_company)
        "museum" -> getString(R.string.museum)
        "night_club" -> getString(R.string.night_club)
        "painter" -> getString(R.string.painter)
        "park" -> getString(R.string.park)
        "parking" -> getString(R.string.parking)
        "pet_store" -> getString(R.string.pet_store)
        "pharmacy" -> getString(R.string.pharmacy)
        "physiotherapist" -> getString(R.string.physiotherapist)
        "plumber" -> getString(R.string.plumber)
        "police" -> getString(R.string.police)
        "post_office" -> getString(R.string.post_office)
        "primary_school" -> getString(R.string.primary_school)
        "real_estate_agency" -> getString(R.string.real_estate_agency)
        "restaurant" -> getString(R.string.restaurant)
        "roofing_contractor" -> getString(R.string.roofing_contractor)
        "rv_park" -> getString(R.string.rv_park)
        "school" -> getString(R.string.school)
        "secondary_school" -> getString(R.string.secondary_school)
        "shoe_store" -> getString(R.string.shoe_store)
        "shopping_mall" -> getString(R.string.shopping_mall)
        "spa" -> getString(R.string.spa)
        "stadium" -> getString(R.string.stadium)
        "storage" -> getString(R.string.storage)
        "store" -> getString(R.string.store)
        "subway_station" -> getString(R.string.subway_station)
        "supermarket" -> getString(R.string.supermarket)
        "synagogue" -> getString(R.string.synagogue)
        "taxi_stand" -> getString(R.string.taxi_stand)
        "tourist_attraction" -> getString(R.string.tourist_attraction)
        "train_station" -> getString(R.string.train_station)
        "transit_station" -> getString(R.string.transit_station)
        "travel_agency" -> getString(R.string.travel_agency)
        "university" -> getString(R.string.university)
        "veterinary_care" -> getString(R.string.veterinary_care)
        "zoo" -> getString(R.string.zoo)
        else -> type
    }
}

fun Context.translateErrorMessage(message: String?): String {
    return when(message) {
        "User not found!" -> getString(R.string.user_not_found)
        "Incorrect password!" -> getString(R.string.incorrect_password)
        "Email already exists" -> getString(R.string.email_already_exists)
        "Invalid token" -> getString(R.string.invalid_token)
        "Username already exists" -> getString(R.string.username_already_exists)
        "User is not verified" -> getString(R.string.user_is_not_verified)
        "not a moderator" -> getString(R.string.not_a_moderator)
        "User is banned" -> getString(R.string.user_is_banned)
        else -> getString(R.string.unknown_error)
    }
}

fun Context.translateDisabilityToView(disabilityType: String) = when(disabilityType) {
    "wheelchair_user" -> getString(R.string.wheelchair_user)
    "limited_walking" -> getString(R.string.limited_walking)
    "limited_hand" -> getString(R.string.limited_hand)
    "limited_physical" -> getString(R.string.limited_physical)
    "blindness" -> getString(R.string.blindness)
    "low_vision" -> getString(R.string.low_vision)
    "deafness" -> getString(R.string.deafness)
    "hard_hearing" -> getString(R.string.hard_hearing)
    "speech_impairment" -> getString(R.string.speech_impairment)
    else -> disabilityType
}

fun Context.translateDisabilityFromView(disabilityType: String) = when(disabilityType) {
    getString(R.string.wheelchair_user) -> "wheelchair_user"
    getString(R.string.limited_walking) -> "limited_walking"
    getString(R.string.limited_hand) -> "limited_hand"
    getString(R.string.limited_physical) -> "limited_physical"
    getString(R.string.blindness) -> "blindness"
    getString(R.string.low_vision) -> "low_vision"
    getString(R.string.deafness) -> "deafness"
    getString(R.string.hard_hearing) -> "hard_hearing"
    getString(R.string.speech_impairment) -> "speech_impairment"
    else -> disabilityType
}

fun Context.translateArticleTypeFromDB(articleType: String) = when(articleType) {
    "news" -> getString(R.string.news).uppercase()
    "article" -> getString(R.string.article).uppercase()
    "guideline" -> getString(R.string.guideline).uppercase()
    else -> getString(R.string.article).uppercase()
}

fun Context.getElapsedTime(startingTime: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = sdf.parse(startingTime)
    val now = Date()
    val diff = now.time - (date?.time ?: 0)

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 0 -> date?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) } ?: ""
        seconds < 60 -> resources.getQuantityString(R.plurals.seconds_ago, seconds.toInt(), seconds.toInt())
        minutes < 60 -> resources.getQuantityString(R.plurals.minutes_ago, minutes.toInt(), minutes.toInt())
        hours < 24 -> resources.getQuantityString(R.plurals.hours_ago, hours.toInt(), hours.toInt())
        days < 8 -> if (days == 1L) getString(R.string.yesterday) else getString(R.string.days_ago, days)
        else -> date?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) } ?: ""
    }
}