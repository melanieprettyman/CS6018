package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()

    }

    private fun setupButtonListeners() {
        binding.shrekBtn.setOnClickListener {
            navigateToSecondActivity(binding.shrekBtn.text.toString())
        }
        binding.donkeyBtn.setOnClickListener {
            navigateToSecondActivity(binding.donkeyBtn.text.toString())
        }
        binding.lfBtn.setOnClickListener {
            navigateToSecondActivity(binding.lfBtn.text.toString())
        }
        binding.miceBtn.setOnClickListener {
            navigateToSecondActivity(binding.miceBtn.text.toString())
        }
        binding.gingyBtn.setOnClickListener {
            navigateToSecondActivity(binding.gingyBtn.text.toString())
        }
    }

    private fun navigateToSecondActivity(buttonText: String) {
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("button_text", buttonText)
        }
        startActivity(intent)
    }

}