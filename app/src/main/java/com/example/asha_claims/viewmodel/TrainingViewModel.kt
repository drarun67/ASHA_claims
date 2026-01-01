package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfile
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.TrainingRecord
import com.example.asha_claims.data.TrainingRepository
import kotlinx.coroutines.launch
import java.util.UUID

class TrainingViewModel(application: Application) : AndroidViewModel(application) {

    private val trainingRepository: TrainingRepository
    private val ashaRepository: AshaProfileRepository

    private val _trainings = MutableLiveData<List<TrainingRecord>>()
    val trainings: LiveData<List<TrainingRecord>> = _trainings
    
    // To display ASHA name if needed
    private val _ashaName = MutableLiveData<String>()
    val ashaName: LiveData<String> = _ashaName

    init {
        val database = AppDatabase.getDatabase(application)
        trainingRepository = TrainingRepository(database.trainingDao())
        ashaRepository = AshaProfileRepository(database.ashaProfileDao())
    }

    fun loadTrainingsForAsha(ashaId: String) {
        viewModelScope.launch {
            _trainings.value = trainingRepository.getTrainingsForAsha(ashaId)
            
            // Also fetch ASHA name
            val profile = ashaRepository.getProfileById(ashaId)
            _ashaName.value = profile?.name ?: "Unknown ASHA"
        }
    }

    fun addTraining(ashaId: String, trainingName: String, isCertified: Boolean, remarks: String) {
        val training = TrainingRecord(
            id = UUID.randomUUID().toString(),
            ashaId = ashaId,
            trainingName = trainingName,
            trainingDate = System.currentTimeMillis(),
            isCertified = isCertified,
            remarks = remarks
        )
        viewModelScope.launch {
            trainingRepository.addTraining(training)
            loadTrainingsForAsha(ashaId) // Refresh list
        }
    }
}
