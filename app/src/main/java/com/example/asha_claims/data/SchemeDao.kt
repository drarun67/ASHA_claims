package com.example.asha_claims.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SchemeDao {
    @Query("SELECT * FROM scheme_enrollments WHERE ashaId = :ashaId")
    suspend fun getEnrollmentsForAsha(ashaId: String): List<SchemeEnrollment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnrollment(enrollment: SchemeEnrollment)
}
