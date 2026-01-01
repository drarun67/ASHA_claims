package com.example.asha_claims.data

import java.util.Date

/**
 * Represents a single activity performed by an ASHA.
 * Core Module 2: Monthly Claims Entry Module
 */
data class ActivityEntry(
    val id: String,
    val ashaId: String,
    val category: ActivityCategory,
    val description: String,
    val datePerformed: Long, // Timestamp
    val incentiveAmount: Double,
    val remarks: String? = null
)

/**
 * Categories defined in Module 2
 */
enum class ActivityCategory {
    RMNCHA_N, // Reproductive, Maternal, Newborn, Child Health and Adolescent
    IMMUNIZATION,
    FAMILY_PLANNING,
    TB_CASE,
    MALARIA_VECTOR, // Malaria and vector-borne disease
    NCD_SCREENING, // Non-Communicable Disease
    SANITATION,
    COMMUNITY_MOBILIZATION,
    OTHER
}
