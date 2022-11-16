package com.raassh.gemastik15.view.fragments.reviewhistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.api.response.ContributionUserData
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository

class ReviewHistoryViewModel(private val contributionRepository: ContributionRepository) : ViewModel() {
    private val token = MutableLiveData<String>()

    fun setToken(token: String) {
        this.token.value = token
    }

    fun deleteReview(placeId: String) = contributionRepository.deleteReview(token.value!!, placeId).asLiveData()
}