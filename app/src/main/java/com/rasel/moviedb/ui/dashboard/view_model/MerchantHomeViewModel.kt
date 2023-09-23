package com.rasel.moviedb.ui.dashboard.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paperflymerchantapp.data.network.Resource
import com.rasel.moviedb.data.preference.AppPrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MerchantHomeViewModel @Inject constructor(
    private val preferences: AppPrefsManager
//    private val repository: MerchantRepository
) : ViewModel() {

}