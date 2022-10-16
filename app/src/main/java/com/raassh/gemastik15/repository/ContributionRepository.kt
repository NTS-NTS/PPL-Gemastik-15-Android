package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.request.ContributionRequest
import com.raassh.gemastik15.utils.callApi

class ContributionRepository(private val apiService: ApiService) {
    fun addContribution(userId: String, placeId: String, facility: String, quality: Int) = callApi {
        val req = ContributionRequest(
            userId=userId,
            placeId=placeId,
            facility=facility,
            quality=quality
        )
        apiService.addContribution(req)
    }

    fun getContributionCount(userId: String) = callApi {
        apiService.getContributionCount(userId).data
    }
}