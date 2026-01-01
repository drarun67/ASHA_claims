package com.example.asha_claims

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asha_claims.databinding.ActivityDashboardBinding
import com.example.asha_claims.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        setupObservers()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to the dashboard
        viewModel.loadDashboardData()
    }

    private fun setupObservers() {
        viewModel.ashaCount.observe(this) { count ->
            binding.tvAshaCount.text = count.toString()
        }

        viewModel.pendingClaimsCount.observe(this) { count ->
            binding.tvPendingClaims.text = count.toString()
        }

        viewModel.totalApprovedAmount.observe(this) { amount ->
            binding.tvApprovedAmount.text = "₹$amount"
        }
    }

    private fun setupListeners() {
        binding.btnManageAsha.setOnClickListener {
            startActivity(Intent(this, AshaListActivity::class.java))
        }

        binding.btnEnterClaims.setOnClickListener {
            startActivity(Intent(this, ClaimsEntryActivity::class.java))
        }

        binding.btnClaimsHistory.setOnClickListener {
            startActivity(Intent(this, ClaimsHistoryActivity::class.java))
        }

        binding.btnApproveClaims.setOnClickListener {
            startActivity(Intent(this, ApprovalActivity::class.java))
        }
    }
}
