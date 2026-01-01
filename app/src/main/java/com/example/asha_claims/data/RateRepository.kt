package com.example.asha_claims.data

/**
 * Repository for managing Activity Rates.
 * Core Module 3: Incentive Calculation Module
 */
class RateRepository {

    // Simulating a database of predefined rates
    private val rates = listOf(
        ActivityRate("R001", ActivityCategory.RMNCHA_N, "Complete Antenatal Checkup (4 visits)", 200.0),
        ActivityRate("R002", ActivityCategory.RMNCHA_N, "Institutional Delivery", 300.0),
        ActivityRate("R003", ActivityCategory.IMMUNIZATION, "Full Immunization (1st year)", 100.0),
        ActivityRate("R004", ActivityCategory.FAMILY_PLANNING, "Sterilization Motivator", 150.0),
        ActivityRate("R005", ActivityCategory.TB_CASE, "DOTS Provider (Treatment Completion)", 500.0),
        ActivityRate("R006", ActivityCategory.MALARIA_VECTOR, "Slide Preparation", 15.0),
        ActivityRate("R007", ActivityCategory.SANITATION, "Toilet Usage Promotion", 75.0),
        ActivityRate("R008", ActivityCategory.COMMUNITY_MOBILIZATION, "VHND Organization", 100.0)
    )

    fun getAllRates(): List<ActivityRate> {
        return rates
    }

    fun getRatesByCategory(category: ActivityCategory): List<ActivityRate> {
        return rates.filter { it.category == category }
    }
}
