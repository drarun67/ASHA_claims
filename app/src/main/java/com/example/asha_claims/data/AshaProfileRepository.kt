package com.example.asha_claims.data

/**
 * Repository for managing ASHA profiles.
 * Uses Room Database for persistence.
 */
class AshaProfileRepository(private val dao: AshaProfileDao) {

    suspend fun getAllProfiles(): List<AshaProfile> {
        return dao.getAllProfiles()
    }

    suspend fun addProfile(profile: AshaProfile) {
        dao.insertProfile(profile)
    }

    suspend fun getProfileById(id: String): AshaProfile? {
        return dao.getProfileById(id)
    }

    suspend fun getProfileCount(): Int {
        return dao.getProfileCount()
    }
    
    suspend fun searchProfiles(query: String): List<AshaProfile> {
        return dao.searchProfiles(query)
    }
}
