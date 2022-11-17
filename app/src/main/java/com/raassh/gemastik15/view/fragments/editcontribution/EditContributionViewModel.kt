package com.raassh.gemastik15.view.fragments.editcontribution

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.api.response.ContributionUserPlaceData
import com.raassh.gemastik15.api.response.FacilityQualityItem
import com.raassh.gemastik15.repository.ContributionRepository

class EditContributionViewModel(private val contributionRepository: ContributionRepository) : ViewModel() {
    private var _reviewFacilities = MutableLiveData<List<FacilityQualityItem>>()
    val reviewFacilities: MutableLiveData<List<FacilityQualityItem>> = _reviewFacilities

    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: MutableLiveData<Boolean> = _isChanged

    init {
        _isChanged.value = false
    }

    fun setInitialReview(review: ContributionUserPlaceData) {
        _reviewFacilities.value = review.facilities
    }

    fun updateChange(oldReview: ContributionUserPlaceData, review: String) {
        if (reviewFacilities.value?.isEmpty() == true) {
            _isChanged.value = false
            return
        }
        val isReviewChanged = oldReview.review != review
        val isFacilityChanged = oldReview.facilities != reviewFacilities.value?.sortedBy { it.facility }
        _isChanged.value = isReviewChanged || isFacilityChanged
    }

    fun addFacilityReview(facility: String, quality: Int) {
        val facilityIndex = _reviewFacilities.value?.indexOfFirst { it.facility == facility }

        if (facilityIndex != null && facilityIndex != -1) {
            _reviewFacilities.value =
                _reviewFacilities.value?.find { it.facility == facility }?.let {
                    _reviewFacilities.value?.toMutableList()?.apply {
                        set(facilityIndex, FacilityQualityItem(facility, quality))
                    }
                }
        } else {
            _reviewFacilities.value =
                _reviewFacilities.value?.plus(FacilityQualityItem(facility, quality))
        }
    }

    fun removeFacilityReview(facility: String) {
        _reviewFacilities.value =
            _reviewFacilities.value?.filter { it.facility != facility }
    }

    fun submitReview(token: String, placeId: String, review: String)
    = contributionRepository.editReview(token, placeId, review, reviewFacilities.value!!).asLiveData()

}