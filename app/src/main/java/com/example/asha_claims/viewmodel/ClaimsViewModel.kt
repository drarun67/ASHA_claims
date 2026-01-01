package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.ActivityCategory
import com.example.asha_claims.data.ActivityEntry
import com.example.asha_claims.data.ActivityRate
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfile
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.ClaimsRepository
import com.example.asha_claims.data.ClaimStatus
import com.example.asha_claims.data.IncentiveClaim
import com.example.asha_claims.data.RateRepository
import kotlinx.coroutines.launch
import java.util.UUID

class ClaimsViewModel(application: Application) : AndroidViewModel(application) {

    private val claimsRepository: ClaimsRepository
    private val ashaProfileRepository: AshaProfileRepository
    private val rateRepository = RateRepository()

    private val _currentActivities = MutableLiveData<List<ActivityEntry>>()
    val currentActivities: LiveData<List<ActivityEntry>> = _currentActivities

    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> = _totalAmount

    private val _ashaProfiles = MutableLiveData<List<AshaProfile>>()
    val ashaProfiles: LiveData<List<AshaProfile>> = _ashaProfiles

    // For editing mode
    private var editingClaimId: String? = null
    private val _editingClaim = MutableLiveData<IncentiveClaim?>()
    val editingClaim: LiveData<IncentiveClaim?> = _editingClaim

    init {
        val database = AppDatabase.getDatabase(application)
        claimsRepository = ClaimsRepository(database.incentiveClaimDao())
        ashaProfileRepository = AshaProfileRepository(database.ashaProfileDao())
        
        updateCurrentState()
        loadAshaProfiles()
    }

    private fun loadAshaProfiles() {
        viewModelScope.launch {
            _ashaProfiles.value = ashaProfileRepository.getAllProfiles()
        }
    }

    private fun updateCurrentState() {
        val activities = claimsRepository.getCurrentActivities()
        _currentActivities.value = activities
        _totalAmount.value = activities.sumOf { it.incentiveAmount }
    }

    fun loadClaimForEditing(claimId: String) {
        viewModelScope.launch {
            val claim = claimsRepository.getClaimById(claimId)
            if (claim != null) {
                editingClaimId = claim.id
                _editingClaim.value = claim
                
                // Load activities into current session
                claimsRepository.clearCurrentActivities()
                claim.activities.forEach { claimsRepository.addActivityToCurrentSession(it) }
                updateCurrentState()
            }
        }
    }
    
    fun getRatesByCategory(category: ActivityCategory): List<ActivityRate> {
        return rateRepository.getRatesByCategory(category)
    }

    fun addActivity(ashaId: String, category: ActivityCategory, description: String, amount: Double) {
        val activity = ActivityEntry(
            id = UUID.randomUUID().toString(),
            ashaId = ashaId,
            category = category,
            description = description,
            datePerformed = System.currentTimeMillis(),
            incentiveAmount = amount
        )
        claimsRepository.addActivityToCurrentSession(activity)
        updateCurrentState()
    }

    fun submitClaim(ashaId: String, month: Int, year: Int) {
        val activities = claimsRepository.getCurrentActivities()
        if (activities.isEmpty()) return

        val total = activities.sumOf { it.incentiveAmount }
        
        if (editingClaimId != null) {
            // Update existing claim
            val updatedClaim = IncentiveClaim(
                id = editingClaimId!!,
                ashaId = ashaId,
                month = month,
                year = year,
                activities = activities,
                totalAmount = total,
                submissionDate = System.currentTimeMillis(),
                status = ClaimStatus.SUBMITTED, // Reset status to Submitted
                approverComments = null // Clear previous rejection comments
            )
            viewModelScope.launch {
                claimsRepository.addClaim(updatedClaim) // addClaim uses OnConflictStrategy.REPLACE
                claimsRepository.clearCurrentActivities()
                editingClaimId = null
                _editingClaim.value = null
                updateCurrentState()
            }
        } else {
            // Create new claim
            val claim = IncentiveClaim(
                id = UUID.randomUUID().toString(),
                ashaId = ashaId,
                month = month,
                year = year,
                activities = activities,
                totalAmount = total,
                submissionDate = System.currentTimeMillis(),
                status = ClaimStatus.SUBMITTED
            )

            viewModelScope.launch {
                claimsRepository.addClaim(claim)
                claimsRepository.clearCurrentActivities()
                updateCurrentState()
            }
        }
    }
}
