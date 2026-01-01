package com.example.asha_claims.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IncentiveClaimDao {
    @Query("SELECT * FROM incentive_claims")
    suspend fun getAllClaims(): List<IncentiveClaim>

    @Query("SELECT * FROM incentive_claims WHERE ashaId = :ashaId")
    suspend fun getClaimsByAsha(ashaId: String): List<IncentiveClaim>
    
    // Fetch claims that are either SUBMITTED or VERIFIED_BY_ANM
    @Query("SELECT * FROM incentive_claims WHERE status IN ('SUBMITTED', 'VERIFIED_BY_ANM')")
    suspend fun getPendingClaims(): List<IncentiveClaim>

    @Query("SELECT COUNT(*) FROM incentive_claims WHERE status IN ('SUBMITTED', 'VERIFIED_BY_ANM')")
    suspend fun getPendingClaimsCount(): Int

    @Query("SELECT SUM(totalAmount) FROM incentive_claims WHERE status = 'APPROVED_BY_DISTRICT' OR status = 'APPROVED_BY_BLOCK'")
    suspend fun getTotalApprovedAmount(): Double?
    
    @Query("SELECT * FROM incentive_claims WHERE id = :id")
    suspend fun getClaimById(id: String): IncentiveClaim?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: IncentiveClaim)

    @Query("UPDATE incentive_claims SET status = :status, approverComments = :comments WHERE id = :claimId")
    suspend fun updateClaimStatus(claimId: String, status: ClaimStatus, comments: String?)
}
