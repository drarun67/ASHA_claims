package com.example.asha_claims.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a training record for an ASHA worker.
 * Core Module 6: Training Management Module
 */
@Entity(tableName = "training_records")
data class TrainingRecord(
    @PrimaryKey val id: String,
    val ashaId: String,
    val trainingName: String,
    val trainingDate: Long, // Timestamp
    val isCertified: Boolean,
    val remarks: String? = null
)
