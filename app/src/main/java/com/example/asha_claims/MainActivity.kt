package com.example.asha_claims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnManageAsha = findViewById<Button>(R.id.btnManageAsha)
        btnManageAsha.setOnClickListener {
            val intent = Intent(this, AshaListActivity::class.java)
            startActivity(intent)
        }

        val btnEnterClaims = findViewById<Button>(R.id.btnEnterClaims)
        btnEnterClaims.setOnClickListener {
            val intent = Intent(this, ClaimsEntryActivity::class.java)
            startActivity(intent)
        }

        val btnApproveClaims = findViewById<Button>(R.id.btnApproveClaims)
        btnApproveClaims.setOnClickListener {
            val intent = Intent(this, ApprovalActivity::class.java)
            startActivity(intent)
        }
    }
}
