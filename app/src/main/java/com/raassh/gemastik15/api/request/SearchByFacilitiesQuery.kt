package com.raassh.gemastik15.api.request

data class SearchByFacilitiesQuery(
    val facilities: List<String>,
    val lat: Double?,
    val long: Double?
)