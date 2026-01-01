package com.example.asha_claims.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AshaProfileDao {
    @Query("SELECT * FROM asha_profiles")
    suspend fun getAllProfiles(): List<AshaProfile>

    @Query("SELECT * FROM asha_profiles WHERE id = :id")
    suspend fun getProfileById(id: String): AshaProfile?

    @Query("SELECT COUNT(*) FROM asha_profiles")
    suspend fun getProfileCount(): Int

    @Query("SELECT * FROM asha_profiles WHERE name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%'")
    suspend fun searchProfiles(query: String): List<AshaProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: AshaProfile)
}
