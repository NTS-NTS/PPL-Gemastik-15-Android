package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.request.ReviewRequest
import com.raassh.gemastik15.api.response.FacilityQualityItem
import com.raassh.gemastik15.utils.callApi

class ContributionRepository(private val apiService: ApiService) {
    fun addReview(token: String, placeId: String, review: String, facilityReviews: List<FacilityQualityItem>) = callApi {
        val req = ReviewRequest(
            placeId=placeId,
            review=review,
            facilityReviews=facilityReviews,
        )
        apiService.addReview("Bearer $token", req)
    }

    fun editReview(token: String, placeId: String, review: String, facilityReviews: List<FacilityQualityItem>) = callApi {
        val req = ReviewRequest(
            placeId = placeId,
            review = review,
            facilityReviews = facilityReviews,
        )
        apiService.changeReview("Bearer $token", req)
    }

    fun deleteReview(token: String, placeId: String) = callApi {
        apiService.deleteContribution("Bearer $token", placeId)
    }

    fun getContributionCount(token: String) = callApi {
        apiService.getContributionCount("Bearer $token").data
    }
    
    fun getContributionsofPlace(placeId: String) = callApi {
        apiService.getContributionsofPlace(placeId).data
    }

    fun getContributionsofPlacebyUser(placeId: String, userId: String) = callApi {
        apiService.getContributionsofPlacebyUser(placeId, userId).data
    }

    fun getContributionsofUser(userId: String, lat: Double, long: Double) = callApi {
        apiService.getContributionsofUser(userId, lat, long).data
    }

}