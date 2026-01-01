package com.example.asha_claims.data

/**
 * Repository for managing Claims and Activities.
 * Uses Room Database for persistence.
 */
class ClaimsRepository(private val dao: IncentiveClaimDao) {

    // Temporary storage for activities being added to a new claim
    // This can stay in-memory as it represents a "Draft" state
    private val currentActivities = mutableListOf<ActivityEntry>()

    suspend fun getClaimsForAsha(ashaId: String): List<IncentiveClaim> {
        return dao.getClaimsByAsha(ashaId)
    }

    suspend fun getAllClaims(): List<IncentiveClaim> {
        return dao.getAllClaims()
    }
    
    suspend fun getClaimById(id: String): IncentiveClaim? {
        return dao.getClaimById(id)
    }

    suspend fun getAllSubmittedClaims(): List<IncentiveClaim> {
        return dao.getPendingClaims()
    }

    suspend fun getPendingClaimsCount(): Int {
        return dao.getPendingClaimsCount()
    }
    
    suspend fun getTotalApprovedAmount(): Double {
        return dao.getTotalApprovedAmount() ?: 0.0
    }

    suspend fun addClaim(claim: IncentiveClaim) {
        dao.insertClaim(claim)
    }

    suspend fun updateClaimStatus(claimId: String, status: ClaimStatus, comment: String? = null) {
        dao.updateClaimStatus(claimId, status, comment)
    }

    // Methods for building a claim currently in progress
    fun addActivityToCurrentSession(activity: ActivityEntry) {
        currentActivities.add(activity)
    }

    fun getCurrentActivities(): List<ActivityEntry> {
        return currentActivities.toList()
    }

    fun clearCurrentActivities() {
        currentActivities.clear()
    }
}
