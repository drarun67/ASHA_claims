package com.example.asha_claims.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asha_claims.R
import com.example.asha_claims.data.IncentiveClaim

class ClaimAdapter(
    private var claims: List<IncentiveClaim> = emptyList(),
    private val getAshaName: (String) -> String,
    private val onApprove: (IncentiveClaim) -> Unit,
    private val onReject: (IncentiveClaim) -> Unit
) : RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder>() {

    class ClaimViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAshaName: TextView = view.findViewById(R.id.tvAshaName)
        val tvPeriod: TextView = view.findViewById(R.id.tvPeriod)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTotalAmount: TextView = view.findViewById(R.id.tvTotalAmount)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_claim, parent, false)
        return ClaimViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val claim = claims[position]
        holder.tvAshaName.text = getAshaName(claim.ashaId)
        holder.tvPeriod.text = "Month: ${claim.month}/${claim.year}"
        holder.tvStatus.text = claim.status.name
        holder.tvTotalAmount.text = "₹${claim.totalAmount}"

        holder.btnApprove.setOnClickListener { onApprove(claim) }
        holder.btnReject.setOnClickListener { onReject(claim) }
    }

    override fun getItemCount(): Int = claims.size

    fun updateData(newClaims: List<IncentiveClaim>) {
        claims = newClaims
        notifyDataSetChanged()
    }
}
