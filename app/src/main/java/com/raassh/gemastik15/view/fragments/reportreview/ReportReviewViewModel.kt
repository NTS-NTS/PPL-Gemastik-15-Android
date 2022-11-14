package com.raassh.gemastik15.view.fragments.reportreview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ReportRepository

class ReportReviewViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    val token = MutableLiveData<String>()

    fun setToken(token: String) {
        this.token.value = token
    }

    fun submitReport(token: String, placeId: String, userId: String, reason: String) =
        reportRepository.reportContribution(token, placeId, userId, reason).asLiveData()
}