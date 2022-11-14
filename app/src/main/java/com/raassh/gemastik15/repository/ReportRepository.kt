package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.request.DismissContributionReportRequest
import com.raassh.gemastik15.api.request.DismissUserReportRequest
import com.raassh.gemastik15.api.request.ReportContributionRequest
import com.raassh.gemastik15.api.request.ReportUserRequest
import com.raassh.gemastik15.utils.callApi

class ReportRepository(private val apiService: ApiService) {
    suspend fun reportContribution(token: String, placeId: String, userId: String, reason: String) =
        callApi {
            val req = ReportContributionRequest(
                userId = userId,
                placeId = placeId,
                reason = reason
            )

            apiService.reportContribution("Bearer $token", req)
        }

    suspend fun getAllContributionReport(token: String) = callApi {
        apiService.getContributionReports("Bearer $token").data
    }

    suspend fun getContributionReportDetails(token: String, placeId: String, userId: String) =
        callApi {
            apiService.getContributionReportDetails("Bearer $token", placeId, userId).data
        }

    suspend fun dismissContributionReport(
        token: String,
        placeId: String,
        userId: String
    ) = callApi {
        val req = DismissContributionReportRequest(
            userId = userId,
            placeId = placeId
        )

        apiService.dismissContributionReport("Bearer $token", req)
    }

    suspend fun moderateContributionReport(
        token: String,
        placeId: String,
        userId: String,
        reason: String
    ) = callApi {
        val req = ReportContributionRequest(
            userId = userId,
            placeId = placeId,
            reason = reason
        )

        apiService.moderateContributionReport("Bearer $token", req)
    }

    suspend fun reportUser(token: String, userId: String, reason: String) = callApi {
        val req = ReportUserRequest(
            userId = userId,
            reason = reason
        )

        apiService.reportUser("Bearer $token", req)
    }

    suspend fun getAllUserReport(token: String) = callApi {
        apiService.getUserReports("Bearer $token").data
    }

    suspend fun getUserReportDetails(token: String, userId: String) = callApi {
        apiService.getUserReportDetails("Bearer $token", userId).data
    }

    suspend fun dismissUserReport(token: String, userId: String) = callApi {
        val req = DismissUserReportRequest(
            userId = userId
        )

        apiService.dismissUserReport("Bearer $token", req)
    }

    suspend fun moderateUserReport(token: String, userId: String, reason: String) = callApi {
        val req = ReportUserRequest(
            userId = userId,
            reason = reason
        )

        apiService.moderateUserReport("Bearer $token", req)
    }
}