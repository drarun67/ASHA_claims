package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfile
import com.example.asha_claims.data.AshaProfileRepository
import kotlinx.coroutines.launch

class AshaProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AshaProfileRepository
    
    private val _profiles = MutableLiveData<List<AshaProfile>>()
    val profiles: LiveData<List<AshaProfile>> = _profiles

    init {
        val dao = AppDatabase.getDatabase(application).ashaProfileDao()
        repository = AshaProfileRepository(dao)
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            _profiles.value = repository.getAllProfiles()
        }
    }
    
    fun searchProfiles(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                _profiles.value = repository.getAllProfiles()
            } else {
                _profiles.value = repository.searchProfiles(query)
            }
        }
    }

    fun addProfile(name: String, phoneNumber: String, address: String) {
        val newId = (System.currentTimeMillis() / 1000).toString()
        
        val newProfile = AshaProfile(
            id = newId,
            name = name,
            phoneNumber = phoneNumber,
            address = address,
            villageId = "V001", // Default for now
            subCenterId = "SC001", // Default
            blockId = "B001", // Default
            districtId = "D001", // Default
            dateOfJoining = System.currentTimeMillis()
        )
        
        viewModelScope.launch {
            repository.addProfile(newProfile)
            loadProfiles() // Refresh the list
        }
    }
}
