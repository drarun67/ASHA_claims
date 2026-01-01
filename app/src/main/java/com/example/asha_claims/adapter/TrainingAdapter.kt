package com.example.asha_claims.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asha_claims.R
import com.example.asha_claims.data.TrainingRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrainingAdapter(
    private var trainings: List<TrainingRecord> = emptyList()
) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    class TrainingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTrainingName: TextView = view.findViewById(R.id.tvTrainingName)
        val tvTrainingDate: TextView = view.findViewById(R.id.tvTrainingDate)
        val tvCertified: TextView = view.findViewById(R.id.tvCertified)
        val tvRemarks: TextView = view.findViewById(R.id.tvRemarks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainings[position]
        holder.tvTrainingName.text = training.trainingName
        
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvTrainingDate.text = "Date: ${dateFormat.format(Date(training.trainingDate))}"
        
        if (training.isCertified) {
            holder.tvCertified.text = "Certified"
            holder.tvCertified.setTextColor(Color.parseColor("#2E7D32")) // Green
            holder.tvCertified.setBackgroundColor(Color.parseColor("#E8F5E9"))
        } else {
            holder.tvCertified.text = "Pending"
            holder.tvCertified.setTextColor(Color.parseColor("#EF6C00")) // Orange
            holder.tvCertified.setBackgroundColor(Color.parseColor("#FFF3E0"))
        }

        holder.tvRemarks.text = "Remarks: ${training.remarks ?: "N/A"}"
    }

    override fun getItemCount(): Int = trainings.size

    fun updateData(newTrainings: List<TrainingRecord>) {
        trainings = newTrainings
        notifyDataSetChanged()
    }
}
