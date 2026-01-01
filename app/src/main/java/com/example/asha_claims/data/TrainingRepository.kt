package com.example.asha_claims.data

class TrainingRepository(private val dao: TrainingDao) {
    
    suspend fun getTrainingsForAsha(ashaId: String): List<TrainingRecord> {
        return dao.getTrainingsForAsha(ashaId)
    }

    suspend fun addTraining(training: TrainingRecord) {
        dao.insertTraining(training)
    }
}
