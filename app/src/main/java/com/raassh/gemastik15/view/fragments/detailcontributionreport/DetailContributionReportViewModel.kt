package com.raassh.gemastik15.view.fragments.detailcontributionreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ReportRepository

class DetailContributionReportViewModel(private val reportRepository: ReportRepository) :
    ViewModel() {
    private var token = ""
    private var placeId = ""
    private var userId = ""
    private var reason = ""

    fun setParams(token: String, placeId: String, userId: String) {
        this.token = token
        this.placeId = placeId
        this.userId = userId
    }

    fun setReason(reason: String) {
        this.reason = reason
    }

    fun getDetail() =
        reportRepository.getContributionReportDetails(token, placeId, userId).asLiveData()

    fun moderate() =
        reportRepository.moderateContributionReport(token, placeId, userId, reason).asLiveData()

    fun dismiss() =
        reportRepository.dismissContributionReport(token, placeId, userId).asLiveData()
}