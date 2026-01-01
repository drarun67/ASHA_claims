package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.ClaimStatus
import com.example.asha_claims.data.ClaimsRepository
import com.example.asha_claims.data.IncentiveClaim
import kotlinx.coroutines.launch

class ApprovalViewModel(application: Application) : AndroidViewModel(application) {

    private val claimsRepository: ClaimsRepository
    private val ashaRepository: AshaProfileRepository

    private val _pendingClaims = MutableLiveData<List<IncentiveClaim>>()
    val pendingClaims: LiveData<List<IncentiveClaim>> = _pendingClaims

    init {
        val database = AppDatabase.getDatabase(application)
        claimsRepository = ClaimsRepository(database.incentiveClaimDao())
        ashaRepository = AshaProfileRepository(database.ashaProfileDao())
        loadPendingClaims()
    }

    private fun loadPendingClaims() {
        viewModelScope.launch {
            _pendingClaims.value = claimsRepository.getAllSubmittedClaims()
        }
    }

    fun approveClaim(claimId: String) {
        viewModelScope.launch {
            claimsRepository.updateClaimStatus(claimId, ClaimStatus.APPROVED_BY_BLOCK)
            loadPendingClaims()
        }
    }

    fun rejectClaim(claimId: String, reason: String) {
        viewModelScope.launch {
            claimsRepository.updateClaimStatus(claimId, ClaimStatus.REJECTED, reason)
            loadPendingClaims()
        }
    }
    
    // This needs to be a suspend function or use LiveData if we want to fetch name dynamically
    // For simplicity in the UI adapter, we might need to pre-fetch names or change how the adapter works.
    // However, since we are inside a coroutine scope in the Activity/Fragment usually, 
    // let's keep it simple. But wait, the adapter calls this synchronously. 
    // A better approach for Room is to fetch the name within the claim object (via a Relation)
    // or fetch all profiles and cache them here.
    
    // For now, let's fetch all profiles into a map for quick lookup
    private val _ashaNameMap = mutableMapOf<String, String>()
    
    init {
        viewModelScope.launch {
            val profiles = ashaRepository.getAllProfiles()
            profiles.forEach { _ashaNameMap[it.id] = it.name }
            // Re-trigger claims load to ensure UI updates if names were missing? 
            // actually the adapter calls getAshaName dynamically.
        }
    }

    fun getAshaName(ashaId: String): String {
        return _ashaNameMap[ashaId] ?: "Loading..."
    }
}
