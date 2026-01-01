package com.example.asha_claims.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asha_claims.R
import com.example.asha_claims.data.ActivityEntry

class ActivityAdapter(
    private var activities: List<ActivityEntry> = emptyList()
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.tvCategory.text = activity.category.name
        holder.tvDescription.text = activity.description
        holder.tvAmount.text = "₹${activity.incentiveAmount}"
    }

    override fun getItemCount(): Int = activities.size

    fun updateData(newActivities: List<ActivityEntry>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}
