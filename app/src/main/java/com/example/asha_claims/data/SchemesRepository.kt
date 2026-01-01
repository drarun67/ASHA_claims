package com.example.asha_claims.data

class SchemesRepository(private val dao: SchemeDao) {

    // Predefined welfare schemes
    private val availableSchemes = listOf(
        WelfareScheme("S001", "Pradhan Mantri Suraksha Bima Yojana (PMSBY)", "Accident Insurance Scheme", "All ASHAs"),
        WelfareScheme("S002", "Pradhan Mantri Jeevan Jyoti Bima Yojana (PMJJBY)", "Life Insurance Scheme", "ASHAs aged 18-50"),
        WelfareScheme("S003", "ASHA Education Support", "Financial aid for higher education", "ASHAs completing 10th/12th"),
        WelfareScheme("S004", "Smartphone Provision Scheme", "Free smartphone for digital reporting", "Active ASHAs"),
        WelfareScheme("S005", "Bicycle Allowance", "Support for mobility", "Rural ASHAs")
    )

    suspend fun getEnrollmentsForAsha(ashaId: String): List<SchemeEnrollment> {
        return dao.getEnrollmentsForAsha(ashaId)
    }

    suspend fun addEnrollment(enrollment: SchemeEnrollment) {
        dao.insertEnrollment(enrollment)
    }

    fun getAvailableSchemes(): List<WelfareScheme> {
        return availableSchemes
    }
}
