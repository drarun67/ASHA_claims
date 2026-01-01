package com.example.asha_claims

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asha_claims.adapter.AshaProfileAdapter
import com.example.asha_claims.databinding.ActivityAshaListBinding
import com.example.asha_claims.viewmodel.AshaProfileViewModel

class AshaListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAshaListBinding
    private lateinit var viewModel: AshaProfileViewModel
    private lateinit var adapter: AshaProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAshaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[AshaProfileViewModel::class.java]

        // Initialize RecyclerView and Adapter
        adapter = AshaProfileAdapter(
            onViewTrainings = { ashaId ->
                val intent = Intent(this, TrainingActivity::class.java)
                intent.putExtra("ASHA_ID", ashaId)
                startActivity(intent)
            }
        )
        binding.rvAshaList.layoutManager = LinearLayoutManager(this)
        binding.rvAshaList.adapter = adapter

        // Observe data changes
        viewModel.profiles.observe(this) { profiles ->
            adapter.updateData(profiles)
        }

        // Add Button Click Listener
        binding.fabAddAsha.setOnClickListener {
            showAddAshaDialog()
        }

        // Setup Search
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchProfiles(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchProfiles(newText ?: "")
                return true
            }
        })
    }

    private fun showAddAshaDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_asha, null)
        val etName = dialogView.findViewById<EditText>(R.id.etAshaName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhoneNumber)
        val etAddress = dialogView.findViewById<EditText>(R.id.etAddress)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val address = etAddress.text.toString()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    viewModel.addProfile(name, phone, address)
                    Toast.makeText(this, "ASHA Added Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Name and Phone are required", Toast.LENGTH_SHORT).show()
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
