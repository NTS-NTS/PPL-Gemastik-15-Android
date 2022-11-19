package com.raassh.gemastik15.view.fragments.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.ReportRepository

class UserProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val reportRepository: ReportRepository
) : ViewModel() {
    var token = ""

    fun getUser(userId: String) = authenticationRepository.getUserDetailbyId(userId).asLiveData()

    fun reportUser(userId: String, reason: String) = reportRepository.reportUser(token, userId, reason).asLiveData()
}