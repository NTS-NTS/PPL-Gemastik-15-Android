package com.raassh.gemastik15.view.fragments.searchfacilityoption

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchFacilityOptionViewModel : ViewModel() {
    private val _checkedMobility = MutableLiveData<MutableList<String>>()
    val checkedMobility: MutableLiveData<MutableList<String>> = _checkedMobility

    private val _checkedVisual = MutableLiveData<MutableList<String>>()
    val checkedVisual: MutableLiveData<MutableList<String>> = _checkedVisual

    private val _checkedHearing = MutableLiveData<MutableList<String>>()
    val checkedHearing: MutableLiveData<MutableList<String>> = _checkedHearing
}