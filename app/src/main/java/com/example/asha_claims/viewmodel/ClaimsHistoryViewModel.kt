package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.ClaimsRepository
import com.example.asha_claims.data.IncentiveClaim
import kotlinx.coroutines.launch

class ClaimsHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val claimsRepository: ClaimsRepository
    private val ashaRepository: AshaProfileRepository

    private val _claims = MutableLiveData<List<IncentiveClaim>>()
    val claims: LiveData<List<IncentiveClaim>> = _claims

    // Caching names for display
    private val _ashaNameMap = mutableMapOf<String, String>()

    init {
        val database = AppDatabase.getDatabase(application)
        claimsRepository = ClaimsRepository(database.incentiveClaimDao())
        ashaRepository = AshaProfileRepository(database.ashaProfileDao())
        
        loadClaims()
        loadAshaNames()
    }

    fun loadClaims() {
        viewModelScope.launch {
            _claims.value = claimsRepository.getAllClaims()
        }
    }

    private fun loadAshaNames() {
        viewModelScope.launch {
            val profiles = ashaRepository.getAllProfiles()
            profiles.forEach { _ashaNameMap[it.id] = it.name }
        }
    }

    fun getAshaName(ashaId: String): String {
        return _ashaNameMap[ashaId] ?: "Loading..."
    }
}
