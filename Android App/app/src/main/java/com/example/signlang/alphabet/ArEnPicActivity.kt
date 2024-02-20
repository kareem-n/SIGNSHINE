package com.example.signlang.alphabet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.databinding.ActivityArEnPicBinding

class ArEnPicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArEnPicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArEnPicBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        val btnBack = binding.ivback
        btnBack.setOnClickListener {
            finish()
        }
        super.onStart()
        val btnArab = binding.btnArab
        btnArab.setOnClickListener {
            startActivity(Intent(this, AlphaActivity::class.java))
        }
        val btnTur = binding.btnTur
        btnTur.setOnClickListener {
            startActivity(Intent(this, AlphaTurActivity::class.java))
        }
        val btnEn = binding.btnEn
        btnEn.setOnClickListener {
            startActivity(Intent(this, EnAlphaActivity::class.java))
        }
    }
}