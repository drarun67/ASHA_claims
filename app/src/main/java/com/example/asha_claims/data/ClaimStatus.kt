package com.example.asha_claims.data

/**
 * Represents the status of a claim in the approval workflow.
 * Core Module 4: Claims Submission and Approval Workflow Module
 */
enum class ClaimStatus {
    DRAFT,                 // Created but not submitted
    SUBMITTED,             // Submitted by ANM
    VERIFIED_BY_ANM,       // Technically redundant if ANM submits, but useful if ASHA enters data
    APPROVED_BY_BLOCK,     // Block level approval
    APPROVED_BY_DISTRICT,  // District level final approval
    REJECTED,              // Sent back for modification
    PAYMENT_PROCESSED      // Money disbursed
}
