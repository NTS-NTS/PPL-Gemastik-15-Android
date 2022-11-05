package com.raassh.gemastik15.view.fragments.addcontribution

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.repository.ContributionRepository

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

    init {
        index.value = 0
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

    fun submitContribution(token: String, placeId: String, quality: Int) =
        contributionRepository.addContribution(token, placeId, currentFacility.value!!.name, quality).asLiveData()

    fun setCurrentFacility() {
        _currentFacility.value = facilities.value!![index.value!!]
    }
}