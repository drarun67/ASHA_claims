package com.example.asha_claims.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asha_claims.R
import com.example.asha_claims.data.ClaimStatus
import com.example.asha_claims.data.IncentiveClaim

class ClaimsHistoryAdapter(
    private var claims: List<IncentiveClaim> = emptyList(),
    private val getAshaName: (String) -> String,
    private val onEdit: (IncentiveClaim) -> Unit
) : RecyclerView.Adapter<ClaimsHistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAshaName: TextView = view.findViewById(R.id.tvAshaName)
        val tvPeriod: TextView = view.findViewById(R.id.tvPeriod)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTotalAmount: TextView = view.findViewById(R.id.tvTotalAmount)
        val btnAction: Button = view.findViewById(R.id.btnAction) // Assuming I reuse item_claim or make a similar one
        // Wait, item_claim has two buttons (Approve/Reject). I should probably hide them or reuse one for "Edit"
        // Let's check item_claim.xml again. It has btnApprove and btnReject.
        // I might need a slightly different layout or manipulate visibility.
        // For simplicity, let's assume I'll use a new layout or modify the existing one dynamically.
        // Let's create a specific layout for history items: item_claim_history.xml
    }
    
    // Changing plan: I will create item_claim_history.xml first to avoid confusion with the approval item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_claim_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val claim = claims[position]
        holder.tvAshaName.text = getAshaName(claim.ashaId)
        holder.tvPeriod.text = "Month: ${claim.month}/${claim.year}"
        holder.tvStatus.text = claim.status.name
        holder.tvTotalAmount.text = "₹${claim.totalAmount}"

        // Color coding status
        when (claim.status) {
            ClaimStatus.APPROVED_BY_BLOCK, ClaimStatus.APPROVED_BY_DISTRICT -> {
                holder.tvStatus.setTextColor(Color.parseColor("#388E3C")) // Green
                holder.btnAction.visibility = View.GONE
            }
            ClaimStatus.REJECTED -> {
                holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")) // Red
                holder.btnAction.visibility = View.VISIBLE
                holder.btnAction.text = "Edit / Resubmit"
                holder.btnAction.setOnClickListener { onEdit(claim) }
            }
            else -> {
                holder.tvStatus.setTextColor(Color.parseColor("#F57C00")) // Orange
                holder.btnAction.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = claims.size

    fun updateData(newClaims: List<IncentiveClaim>) {
        claims = newClaims
        notifyDataSetChanged()
    }
}
