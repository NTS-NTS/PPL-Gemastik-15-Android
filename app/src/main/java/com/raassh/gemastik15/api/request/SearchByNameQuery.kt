package com.raassh.gemastik15.api.request

data class SearchByNameQuery(
    val name: String,
    val lat: Double?,
    val long: Double?
)