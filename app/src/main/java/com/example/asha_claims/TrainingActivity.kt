package com.example.asha_claims

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asha_claims.adapter.TrainingAdapter
import com.example.asha_claims.data.AshaProfile
import com.example.asha_claims.data.AshaProfileRepository
import com.example.asha_claims.data.AppDatabase
import com.example.asha_claims.databinding.ActivityTrainingBinding
import com.example.asha_claims.viewmodel.TrainingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingBinding
    private lateinit var viewModel: TrainingViewModel
    private lateinit var adapter: TrainingAdapter
    private var ashaProfiles: List<AshaProfile> = emptyList()
    private var selectedAshaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TrainingViewModel::class.java]
        
        setupRecyclerView()
        setupSpinners() // This will also trigger loading trainings
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = TrainingAdapter()
        binding.rvTrainings.layoutManager = LinearLayoutManager(this)
        binding.rvTrainings.adapter = adapter
    }

    private fun setupSpinners() {
        // We need to fetch ASHAs to populate the spinner.
        // Ideally this should be in the ViewModel too, but for speed I'll fetch here or add to VM.
        // Let's add fetching ASHAs to TrainingViewModel or use a quick scope here since we don't have it in VM yet.
        // Actually, let's just use the DB directly here for simplicity or update VM. 
        // I'll update VM to be cleaner. For now, doing it here to save a round trip.
        val dao = AppDatabase.getDatabase(application).ashaProfileDao()
        CoroutineScope(Dispatchers.IO).launch {
            ashaProfiles = dao.getAllProfiles()
            withContext(Dispatchers.Main) {
                val ashaNames = ashaProfiles.map { it.name }
                val spinnerAdapter = ArrayAdapter(this@TrainingActivity, android.R.layout.simple_spinner_item, ashaNames)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerAsha.adapter = spinnerAdapter
                
                // Handle pre-selection if passed via intent
                val passedAshaId = intent.getStringExtra("ASHA_ID")
                if (passedAshaId != null) {
                    val index = ashaProfiles.indexOfFirst { it.id == passedAshaId }
                    if (index != -1) {
                        binding.spinnerAsha.setSelection(index)
                    }
                }
            }
        }

        binding.spinnerAsha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (ashaProfiles.isNotEmpty()) {
                    selectedAshaId = ashaProfiles[position].id
                    viewModel.loadTrainingsForAsha(selectedAshaId!!)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupObservers() {
        viewModel.trainings.observe(this) { trainings ->
            adapter.updateData(trainings)
            binding.tvEmptyState.visibility = if (trainings.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.fabAddTraining.setOnClickListener {
            if (selectedAshaId == null) {
                Toast.makeText(this, "Please select an ASHA first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showAddTrainingDialog()
        }
    }

    private fun showAddTrainingDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_training, null)
        val etTrainingName = dialogView.findViewById<EditText>(R.id.etTrainingName)
        val cbCertified = dialogView.findViewById<CheckBox>(R.id.cbCertified)
        val etRemarks = dialogView.findViewById<EditText>(R.id.etRemarks)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = etTrainingName.text.toString()
                val isCertified = cbCertified.isChecked
                val remarks = etRemarks.text.toString()

                if (name.isNotEmpty()) {
                    viewModel.addTraining(selectedAshaId!!, name, isCertified, remarks)
                    Toast.makeText(this, "Training Record Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Training Name is required", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
