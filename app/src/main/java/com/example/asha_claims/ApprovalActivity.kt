package com.example.asha_claims

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asha_claims.adapter.ClaimAdapter
import com.example.asha_claims.databinding.ActivityApprovalBinding
import com.example.asha_claims.util.NotificationUtil
import com.example.asha_claims.viewmodel.ApprovalViewModel
import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class ApprovalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApprovalBinding
    private lateinit var viewModel: ApprovalViewModel
    private lateinit var adapter: ClaimAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Notification Channel
        NotificationUtil.createNotificationChannel(this)
        
        // Request Permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                 requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
             }
        }

        viewModel = ViewModelProvider(this)[ApprovalViewModel::class.java]

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = ClaimAdapter(
            getAshaName = { id -> viewModel.getAshaName(id) },
            onApprove = { claim ->
                viewModel.approveClaim(claim.id)
                NotificationUtil.showNotification(
                    this, 
                    "Claim Approved", 
                    "Claim for ${viewModel.getAshaName(claim.ashaId)} has been approved."
                )
                Toast.makeText(this, "Claim Approved", Toast.LENGTH_SHORT).show()
            },
            onReject = { claim ->
                showRejectDialog(claim.id, claim.ashaId)
            }
        )
        binding.rvPendingClaims.layoutManager = LinearLayoutManager(this)
        binding.rvPendingClaims.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.pendingClaims.observe(this) { claims ->
            adapter.updateData(claims)
            binding.tvEmptyState.visibility = if (claims.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showRejectDialog(claimId: String, ashaId: String) {
        val input = EditText(this)
        input.hint = "Enter rejection reason"

        AlertDialog.Builder(this)
            .setTitle("Reject Claim")
            .setView(input)
            .setPositiveButton("Reject") { dialog, _ ->
                val reason = input.text.toString()
                if (reason.isNotEmpty()) {
                    viewModel.rejectClaim(claimId, reason)
                    NotificationUtil.showNotification(
                        this, 
                        "Claim Rejected", 
                        "Claim for ${viewModel.getAshaName(ashaId)} rejected: $reason"
                    )
                    Toast.makeText(this, "Claim Rejected", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Reason is required", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
