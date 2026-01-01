package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.ClaimsRepository
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val ashaRepository: AshaProfileRepository
    private val claimsRepository: ClaimsRepository

    private val _ashaCount = MutableLiveData<Int>()
    val ashaCount: LiveData<Int> = _ashaCount

    private val _pendingClaimsCount = MutableLiveData<Int>()
    val pendingClaimsCount: LiveData<Int> = _pendingClaimsCount

    private val _totalApprovedAmount = MutableLiveData<Double>()
    val totalApprovedAmount: LiveData<Double> = _totalApprovedAmount

    init {
        val database = AppDatabase.getDatabase(application)
        ashaRepository = AshaProfileRepository(database.ashaProfileDao())
        claimsRepository = ClaimsRepository(database.incentiveClaimDao())
        
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _ashaCount.value = ashaRepository.getProfileCount()
            _pendingClaimsCount.value = claimsRepository.getPendingClaimsCount()
            _totalApprovedAmount.value = claimsRepository.getTotalApprovedAmount()
        }
    }
}
