package com.example.asha_claims.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.SchemeEnrollment
import com.example.asha_claims.data.SchemesRepository
import com.example.asha_claims.data.WelfareScheme
import kotlinx.coroutines.launch
import java.util.UUID

class SchemesViewModel(application: Application) : AndroidViewModel(application) {

    private val schemesRepository: SchemesRepository
    private val ashaRepository: AshaProfileRepository

    private val _enrollments = MutableLiveData<List<SchemeEnrollment>>()
    val enrollments: LiveData<List<SchemeEnrollment>> = _enrollments
    
    private val _ashaName = MutableLiveData<String>()
    val ashaName: LiveData<String> = _ashaName

    init {
        val database = AppDatabase.getDatabase(application)
        schemesRepository = SchemesRepository(database.schemeDao())
        ashaRepository = AshaProfileRepository(database.ashaProfileDao())
    }

    fun loadEnrollmentsForAsha(ashaId: String) {
        viewModelScope.launch {
            _enrollments.value = schemesRepository.getEnrollmentsForAsha(ashaId)
            val profile = ashaRepository.getProfileById(ashaId)
            _ashaName.value = profile?.name ?: "Unknown ASHA"
        }
    }

    fun getAvailableSchemes(): List<WelfareScheme> {
        return schemesRepository.getAvailableSchemes()
    }

    fun enrollAsha(ashaId: String, schemeName: String, status: String, remarks: String) {
        val enrollment = SchemeEnrollment(
            id = UUID.randomUUID().toString(),
            ashaId = ashaId,
            schemeName = schemeName,
            enrollmentDate = System.currentTimeMillis(),
            status = status,
            benefitAmount = 0.0,
            remarks = remarks
        )
        viewModelScope.launch {
            schemesRepository.addEnrollment(enrollment)
            loadEnrollmentsForAsha(ashaId)
        }
    }
}
