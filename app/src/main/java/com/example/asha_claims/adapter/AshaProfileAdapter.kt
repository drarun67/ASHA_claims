package com.example.asha_claims.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asha_claims.R
import com.example.asha_claims.data.AshaProfile

class AshaProfileAdapter(
    private var profiles: List<AshaProfile> = emptyList(),
    private val onViewTrainings: ((String) -> Unit)? = null
) : RecyclerView.Adapter<AshaProfileAdapter.AshaProfileViewHolder>() {

    class AshaProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAshaName)
        val tvPhone: TextView = view.findViewById(R.id.tvPhoneNumber)
        val tvVillage: TextView = view.findViewById(R.id.tvVillage)
        val btnViewTrainings: Button = view.findViewById(R.id.btnViewTrainings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AshaProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asha_profile, parent, false)
        return AshaProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: AshaProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.tvName.text = profile.name
        holder.tvPhone.text = "Phone: ${profile.phoneNumber}"
        holder.tvVillage.text = "Village ID: ${profile.villageId}"
        
        holder.btnViewTrainings.setOnClickListener {
            onViewTrainings?.invoke(profile.id)
        }
    }

    override fun getItemCount(): Int = profiles.size

    fun updateData(newProfiles: List<AshaProfile>) {
        profiles = newProfiles
        notifyDataSetChanged()
    }
}
