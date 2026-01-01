package com.example.asha_claims.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an ASHA's enrollment in an empowerment scheme.
 * Core Module 7: ASHA Empowerment Schemes Module
 */
@Entity(tableName = "scheme_enrollments")
data class SchemeEnrollment(
    @PrimaryKey val id: String,
    val ashaId: String,
    val schemeName: String,
    val enrollmentDate: Long, // Timestamp
    val status: String, // e.g., "Active", "Pending", "Benefit Received"
    val benefitAmount: Double? = 0.0,
    val remarks: String? = null
)

/**
 * Static definition for available schemes (Information)
 */
data class WelfareScheme(
    val id: String,
    val name: String,
    val description: String,
    val eligibility: String
)
