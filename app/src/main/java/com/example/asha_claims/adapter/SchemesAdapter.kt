package com.example.asha_claims.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asha_claims.R
import com.example.asha_claims.data.SchemeEnrollment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchemesAdapter(
    private var enrollments: List<SchemeEnrollment> = emptyList()
) : RecyclerView.Adapter<SchemesAdapter.SchemeViewHolder>() {

    class SchemeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSchemeName: TextView = view.findViewById(R.id.tvSchemeName)
        val tvEnrollmentDate: TextView = view.findViewById(R.id.tvEnrollmentDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvRemarks: TextView = view.findViewById(R.id.tvRemarks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchemeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scheme, parent, false)
        return SchemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SchemeViewHolder, position: Int) {
        val enrollment = enrollments[position]
        holder.tvSchemeName.text = enrollment.schemeName
        
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvEnrollmentDate.text = "Enrolled: ${dateFormat.format(Date(enrollment.enrollmentDate))}"
        
        holder.tvStatus.text = enrollment.status
        
        // Color coding based on status
        when (enrollment.status) {
            "Active" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#1976D2")) // Blue
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E3F2FD"))
            }
            "Pending" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#F57C00")) // Orange
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0"))
            }
            "Benefit Received" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#388E3C")) // Green
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"))
            }
            else -> {
                holder.tvStatus.setTextColor(Color.DKGRAY)
                holder.tvStatus.setBackgroundColor(Color.LTGRAY)
            }
        }

        holder.tvRemarks.text = "Remarks: ${enrollment.remarks ?: "N/A"}"
    }

    override fun getItemCount(): Int = enrollments.size

    fun updateData(newEnrollments: List<SchemeEnrollment>) {
        enrollments = newEnrollments
        notifyDataSetChanged()
    }
}
