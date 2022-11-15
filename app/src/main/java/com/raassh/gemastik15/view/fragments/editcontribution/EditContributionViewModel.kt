package com.raassh.gemastik15.view.fragments.editcontribution

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.api.response.FacilityQualityItem
import com.raassh.gemastik15.api.response.ReviewData
import com.raassh.gemastik15.repository.ContributionRepository

class EditContributionViewModel(private val contributionRepository: ContributionRepository) : ViewModel() {
    private val _reviewFacilities = MutableLiveData<List<FacilityQualityItem>>()
    val reviewFacilities: MutableLiveData<List<FacilityQualityItem>> = _reviewFacilities

    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: MutableLiveData<Boolean> = _isChanged

    fun updateChange(oldReview: ReviewData, review: String) {
        _isChanged.value =
        ((oldReview.review != review) || (oldReview.facilities != reviewFacilities.value))
    }

    fun addFacilityReview(facility: String, quality: Int) {
        val facilityQualityItem = FacilityQualityItem(
            facility = facility,
            quality = quality
        )
        _reviewFacilities.value = reviewFacilities.value?.plus(facilityQualityItem) }

    fun submitReview(token: String, placeId: String, review: String)
    = contributionRepository.editReview(token, placeId, review, reviewFacilities.value!!).asLiveData()

}