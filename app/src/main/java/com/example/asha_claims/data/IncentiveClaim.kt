package com.example.asha_claims.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a monthly compiled claim for an ASHA.
 * Core Module 4: Claims Submission and Approval Workflow Module
 */
@Entity(tableName = "incentive_claims")
data class IncentiveClaim(
    @PrimaryKey val id: String,
    val ashaId: String,
    val month: Int, // 1-12
    val year: Int,
    val activities: List<ActivityEntry>, // Needs TypeConverter
    val totalAmount: Double,
    val submissionDate: Long?, // Timestamp
    val status: ClaimStatus, // Needs TypeConverter
    val approverComments: String? = null
)
