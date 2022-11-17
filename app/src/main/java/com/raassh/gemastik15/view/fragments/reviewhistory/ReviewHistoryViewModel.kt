package com.raassh.gemastik15.view.fragments.reviewhistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.ContributionRepository

class ReviewHistoryViewModel(private val contributionRepository: ContributionRepository) : ViewModel() {
    private val token = MutableLiveData<String>()

    fun setToken(token: String) {
        this.token.value = token
    }

    fun deleteReview(placeId: String) = contributionRepository.deleteReview(token.value!!, placeId).asLiveData()
}