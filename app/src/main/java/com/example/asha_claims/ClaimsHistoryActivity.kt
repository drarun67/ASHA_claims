package com.example.asha_claims

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asha_claims.adapter.ClaimsHistoryAdapter
import com.example.asha_claims.databinding.ActivityClaimsHistoryBinding
import com.example.asha_claims.viewmodel.ClaimsHistoryViewModel

class ClaimsHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClaimsHistoryBinding
    private lateinit var viewModel: ClaimsHistoryViewModel
    private lateinit var adapter: ClaimsHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ClaimsHistoryViewModel::class.java]

        setupRecyclerView()
        setupObservers()
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh when returning (e.g., after editing)
        viewModel.loadClaims()
    }

    private fun setupRecyclerView() {
        adapter = ClaimsHistoryAdapter(
            getAshaName = { id -> viewModel.getAshaName(id) },
            onEdit = { claim ->
                // Navigate to ClaimsEntryActivity with claim ID
                val intent = Intent(this, ClaimsEntryActivity::class.java)
                intent.putExtra("CLAIM_ID", claim.id)
                startActivity(intent)
            }
        )
        binding.rvClaimsHistory.layoutManager = LinearLayoutManager(this)
        binding.rvClaimsHistory.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.claims.observe(this) { claims ->
            adapter.updateData(claims)
            binding.tvEmptyState.visibility = if (claims.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
