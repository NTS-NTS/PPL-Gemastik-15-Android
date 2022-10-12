package com.raassh.gemastik15.view.fragments.AddContribution

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raassh.gemastik15.local.db.Facility

class AddContributionViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _currentFacility = MutableLiveData<Facility>()
    val currentFacility: MutableLiveData<Facility> = _currentFacility

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: MutableLiveData<Boolean> = _isSuccess

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: MutableLiveData<Boolean> = _isDone

    private val _facilities = MutableLiveData<List<Facility>>()
    val facilities: MutableLiveData<List<Facility>> = _facilities

    private val _index = MutableLiveData<Int>()
    val index: MutableLiveData<Int> = _index

    private fun nextFacility() {
        if (index.value!! < facilities.value!!.size - 1) {
            _index.value = index.value!! + 1
            _currentFacility.value = facilities.value!![index.value!!]
            _isDone.value = false
        } else {
            _isDone.value = true
        }
    }

    suspend fun submitContribution(quality: Int ) {
        _isLoading.value = true
//        val response = repository.submitContribution(quality, quantity, currentFacility?.id, location.value?.latitude, location.value?.longitude)
//        _isSuccess.value = response.isSuccessful

//        if (response.isSuccessful) {
//            nextFacility()
//        }
        _isLoading.value = false
    }
}