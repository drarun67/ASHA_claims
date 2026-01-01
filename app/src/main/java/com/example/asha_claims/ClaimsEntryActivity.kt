package com.example.asha_claims

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asha_claims.adapter.ActivityAdapter
import com.example.asha_claims.data.ActivityCategory
import com.example.asha_claims.data.ActivityRate
import com.example.asha_claims.data.AshaProfile
import com.example.asha_claims.databinding.ActivityClaimsEntryBinding
import com.example.asha_claims.viewmodel.ClaimsViewModel
import java.util.Calendar

class ClaimsEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClaimsEntryBinding
    private lateinit var viewModel: ClaimsViewModel
    private lateinit var adapter: ActivityAdapter
    private var ashaProfiles: List<AshaProfile> = emptyList()
    private var currentRates: List<ActivityRate> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ClaimsViewModel::class.java]

        setupSpinners()
        setupRecyclerView()
        setupObservers()
        setupListeners()
        
        // Check for edit intent
        val editClaimId = intent.getStringExtra("CLAIM_ID")
        if (editClaimId != null) {
            viewModel.loadClaimForEditing(editClaimId)
            binding.btnSubmitClaim.text = "Update & Resubmit Claim"
            // We'll also need to set the ASHA spinner once the claim and profiles are loaded
            // This logic is handled in setupObservers
        }
    }

    private fun setupSpinners() {
        // Setup Category Spinner
        val categories = ActivityCategory.values().map { it.name }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        // Handle Category Selection
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = ActivityCategory.values()[position]
                updateActivityRateSpinner(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateActivityRateSpinner(category: ActivityCategory) {
        currentRates = viewModel.getRatesByCategory(category)

        if (currentRates.isNotEmpty()) {
            binding.spinnerActivityRate.visibility = View.VISIBLE
            val rateNames = currentRates.map { it.name }.toMutableList()
            // Add an "Other" option if users need to enter manually
            rateNames.add("Other / Custom Activity")

            val rateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rateNames)
            rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerActivityRate.adapter = rateAdapter
            
            // Trigger selection of first item
            if (rateNames.isNotEmpty()) {
                handleRateSelection(0)
            }
        } else {
            binding.spinnerActivityRate.visibility = View.GONE
            // Clear or reset fields if no rates are available
            binding.etDescription.setText("")
            binding.etAmount.setText("")
        }

        binding.spinnerActivityRate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                handleRateSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handleRateSelection(position: Int) {
        if (position < currentRates.size) {
            val rate = currentRates[position]
            binding.etDescription.setText(rate.name)
            binding.etAmount.setText(rate.rate.toString())
        } else {
            // Selected "Other"
            binding.etDescription.setText("")
            binding.etAmount.setText("")
        }
    }

    private fun setupRecyclerView() {
        adapter = ActivityAdapter()
        binding.rvActivities.layoutManager = LinearLayoutManager(this)
        binding.rvActivities.adapter = adapter
    }

    private fun setupObservers() {
        // Observe ASHA Profiles
        viewModel.ashaProfiles.observe(this) { profiles ->
            ashaProfiles = profiles
            val ashaNames = ashaProfiles.map { it.name }
            val ashaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ashaNames)
            ashaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAsha.adapter = ashaAdapter
            
            // If we are editing, we might need to select the correct ASHA
            // This is tricky because profiles might load after or before the claim
            // Ideally we check both conditions
            viewModel.editingClaim.value?.let { claim ->
                 selectAshaInSpinner(claim.ashaId)
            }
        }
        
        // Observe Editing Claim
        viewModel.editingClaim.observe(this) { claim ->
            if (claim != null && ashaProfiles.isNotEmpty()) {
                selectAshaInSpinner(claim.ashaId)
                // Disable spinner to prevent changing ASHA while editing a claim (optional but good practice)
                binding.spinnerAsha.isEnabled = false
            }
        }

        viewModel.currentActivities.observe(this) { activities ->
            adapter.updateData(activities)
        }

        viewModel.totalAmount.observe(this) { total ->
            binding.tvTotalAmount.text = "Total: ₹$total"
        }
    }
    
    private fun selectAshaInSpinner(ashaId: String) {
        val index = ashaProfiles.indexOfFirst { it.id == ashaId }
        if (index != -1) {
            binding.spinnerAsha.setSelection(index)
        }
    }

    private fun setupListeners() {
        binding.btnAddActivity.setOnClickListener {
            val selectedAshaIndex = binding.spinnerAsha.selectedItemPosition
            if (selectedAshaIndex == -1 || ashaProfiles.isEmpty()) {
                Toast.makeText(this, "Please select an ASHA", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedAsha = ashaProfiles[selectedAshaIndex]
            val category = ActivityCategory.values()[binding.spinnerCategory.selectedItemPosition]
            val description = binding.etDescription.text.toString()
            val amountStr = binding.etAmount.text.toString()

            if (description.isNotEmpty() && amountStr.isNotEmpty()) {
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                viewModel.addActivity(selectedAsha.id, category, description, amount)

                // Check if custom rate or predefined was used
                if (binding.spinnerActivityRate.selectedItemPosition >= currentRates.size) {
                     binding.etDescription.text.clear()
                     binding.etAmount.text.clear()
                }
            } else {
                Toast.makeText(this, "Please fill in description and amount", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSubmitClaim.setOnClickListener {
            val selectedAshaIndex = binding.spinnerAsha.selectedItemPosition
            if (selectedAshaIndex != -1 && ashaProfiles.isNotEmpty()) {
                val selectedAsha = ashaProfiles[selectedAshaIndex]
                val calendar = Calendar.getInstance()
                // If editing, preserve the original month/year or allow user to change?
                // For now, defaulting to current month/year which might be okay for resubmission
                // Ideally, we should parse the claim date
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)

                viewModel.submitClaim(selectedAsha.id, month, year)
                
                val message = if (binding.btnSubmitClaim.text.contains("Update")) 
                    "Claim Updated & Resubmitted!" else "Monthly Claim Submitted Successfully!"
                
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
