package com.example.asha_claims.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the profile of an ASHA (Accredited Social Health Activist) worker.
 * Core Module 1: ASHA Profile Management
 */
@Entity(tableName = "asha_profiles")
data class AshaProfile(
    @PrimaryKey val id: String,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val villageId: String,
    val subCenterId: String,
    val blockId: String,
    val districtId: String,
    val dateOfJoining: Long, // Timestamp
    val isActive: Boolean = true,
    val bankAccountDetails: BankDetails? = null
)

data class BankDetails(
    val accountNumber: String,
    val bankName: String,
    val ifscCode: String,
    val accountHolderName: String
)
