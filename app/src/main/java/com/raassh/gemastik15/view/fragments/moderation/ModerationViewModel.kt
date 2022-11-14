package com.raassh.gemastik15.view.fragments.moderation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ReportRepository

class ModerationViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private val token = MutableLiveData<String>()

    fun setToken(token: String) {
        this.token.value = token
    }

    val contributionsReport = Transformations.switchMap(token) {
        reportRepository.getAllContributionReport(it).asLiveData()
    }

    val usersReport = Transformations.switchMap(token) {
        reportRepository.getAllUserReport(it).asLiveData()
    }
}