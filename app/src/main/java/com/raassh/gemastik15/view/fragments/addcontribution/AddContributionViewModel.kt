package com.raassh.gemastik15.view.fragments.addcontribution

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.api.response.FacilityQualityItem
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddContributionViewModel(
    private val contributionRepository: ContributionRepository
) : ViewModel() {
    private val _currentFacility = MutableLiveData<Facility>()
    val currentFacility: MutableLiveData<Facility> = _currentFacility

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: MutableLiveData<Boolean> = _isDone

    private val _facilities = MutableLiveData<List<Facility>>()
    val facilities: MutableLiveData<List<Facility>> = _facilities

    private val _index = MutableLiveData<Int>()
    val index: MutableLiveData<Int> = _index

    private val _reviewFacilities = MutableLiveData<List<FacilityQualityItem>>()
    val reviewFacilities: MutableLiveData<List<FacilityQualityItem>> = _reviewFacilities

    init {
        index.value = 0
        reviewFacilities.value = listOf()
    }

    fun nextFacility() {
        Log.d("index", index.value.toString())
        Log.d("currentFacility", _currentFacility.value.toString())
        if (index.value!! < facilities.value!!.size - 1) {
            _index.value = index.value!! + 1
            _currentFacility.value = facilities.value!![index.value!!]
            _isDone.value = false
        } else {
            _isDone.value = true
        }
    }

    fun addFacilityReview(quality: Int) {
        val facilityQualityItem = FacilityQualityItem(
            facility = currentFacility.value!!.name,
            quality = quality
        )
        _reviewFacilities.value = reviewFacilities.value?.plus(facilityQualityItem)
        Log.d("reviewFacilitiesvm", reviewFacilities.value.toString())
        nextFacility()
    }

//    fun submitFullReview(token: String, placeId: String, review: String) {
//        isFinished.value = false
//        viewModelScope.launch {
//            coroutineScope {
//                reviewFacilities.value?.forEach {
//                    launch {
//                        val response = contributionRepository.addContribution(
//                            token = token,
//                            placeId = placeId,
//                            facility = it.facility,
//                            quality = it.quality
//                        ).asLiveData().value
//                        when (response) {
//                            is Resource.Success -> {
//                                Log.d("addContribution", it.facility + "success")
//                            }
//                            is Resource.Error -> {
//                                Log.d("addContribution", it.facility + "error")
//                                isError.value = true
//                            }
//                            else -> {}
//                        }
//                    }
//                }
//                launch {
//                    val response = contributionRepository.addReview(
//                        token = token,
//                        placeId = placeId,
//                        review = review
//                    ).asLiveData().value
//                    when (response) {
//                        is Resource.Success -> {
//                            isFinished.value = true
//                        }
//                        is Resource.Error -> {
//                            isError.value = true
//                        }
//                        else -> {}
//                    }
//                }
//            }
//        }
//    }

    fun submitReview(token: String, placeId: String, review: String) =
        contributionRepository.addReview(
            token = token,
            placeId = placeId,
            review = review,
            facilityReviews = reviewFacilities.value!!
        ).asLiveData()

    fun setCurrentFacility() {
        _currentFacility.value = facilities.value!![index.value!!]
    }
}