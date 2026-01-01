package com.example.asha_claims.data

/**
 * Represents a predefined activity with a fixed incentive rate.
 * Core Module 3: Incentive Calculation Module
 */
data class ActivityRate(
    val id: String,
    val category: ActivityCategory,
    val name: String,
    val rate: Double,
    val description: String? = null
)
