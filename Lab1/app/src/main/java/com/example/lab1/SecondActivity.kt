package com.example.lab1
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab1.databinding.ActivitySecondBinding
class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonText = intent.getStringExtra("button_text")
        binding.nameBtn.text = buttonText ?: "Unknown"

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}