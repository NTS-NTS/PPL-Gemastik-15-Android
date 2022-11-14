package com.raassh.gemastik15.view.fragments.detailuserreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ReportRepository

class DetailUserReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private var token = ""
    private var userId = ""
    private var reason = ""

    fun setParams(token: String, userId: String) {
        this.token = token
        this.userId = userId
    }

    fun setReason(reason: String) {
        this.reason = reason
    }

    fun getDetail() =
        reportRepository.getUserReportDetails(token, userId).asLiveData()

    fun moderate() =
        reportRepository.moderateUserReport(token, userId, reason).asLiveData()

    fun dismiss() =
        reportRepository.dismissUserReport(token, userId).asLiveData()
}