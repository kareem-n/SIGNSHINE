package com.example.signlang.words

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.databinding.ActivityWordsPicBinding

class WordsPicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordsPicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordsPicBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val btnBack = binding.ivback
        btnBack.setOnClickListener {
            finish()
        }
        val btnAr = binding.btnArab
        btnAr.setOnClickListener {
            startActivity(Intent(this, ArabCatActivity::class.java))
        }
        val btnInd = binding.btnEng
        btnInd.setOnClickListener {
            startActivity(Intent(this, EnglishWordsActivity::class.java))
        }
        val btnTur = binding.btnTur
        btnTur.setOnClickListener {
            startActivity(Intent(this, TurkishWordActivity::class.java))
        }
        val btnGre = binding.btnGreece
        btnGre.setOnClickListener {
            startActivity(Intent(this, GreekWordActivity::class.java))
        }
    }
}