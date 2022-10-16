package com.raassh.gemastik15.view.fragments.addcontribution

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
    private val index: MutableLiveData<Int> = _index

    fun nextFacility() {
        if (index.value!! < facilities.value!!.size - 1) {
            _index.value = index.value!! + 1
            _currentFacility.value = facilities.value!![index.value!!]
            _isDone.value = false
        } else {
            _isDone.value = true
        }
    }

    fun submitContribution(userId: String, placeId: String, quality: Int) =
        contributionRepository.addContribution(userId, placeId, currentFacility.value!!.name, quality).asLiveData()
}