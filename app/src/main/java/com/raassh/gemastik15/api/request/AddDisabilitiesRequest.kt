package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class AddDisabilitiesRequest(
    @field:SerializedName("disability_types")
    val disabilities: List<String>
)