package com.raassh.gemastik15.view.fragments.reportuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ReportRepository

class ReportUserViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    val token = MutableLiveData<String>()

    fun setToken(token: String) {
        this.token.value = token
    }

    fun reportUser(token: String, userId: String, reason: String) = reportRepository.reportUser(token, userId, reason).asLiveData()
}