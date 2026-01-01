package com.example.asha_claims.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrainingDao {
    @Query("SELECT * FROM training_records WHERE ashaId = :ashaId ORDER BY trainingDate DESC")
    suspend fun getTrainingsForAsha(ashaId: String): List<TrainingRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: TrainingRecord)
}
