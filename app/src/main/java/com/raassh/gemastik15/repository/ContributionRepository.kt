package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.request.ContributionRequest
import com.raassh.gemastik15.utils.callApi

class ContributionRepository(private val apiService: ApiService) {
    fun addContribution(token: String, placeId: String, facility: String, quality: Int) = callApi {
        val req = ContributionRequest(
            placeId=placeId,
            facility=facility,
            quality=quality
        )
        apiService.addContribution("Bearer $token", req)
    }

    fun getContributionCount(token: String) = callApi {
        apiService.getContributionCount("Bearer $token").data
    }
    
    fun getContributionsofPlace(placeId: String) = callApi {
        apiService.getContributionsofPlace(placeId).data
    }
}