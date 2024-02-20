package com.example.signlang.texttovids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.databinding.ActivitySelectTextLangBinding
import com.example.signlang.texttovids.arabic.SelectCatsActivity
import com.example.signlang.texttovids.english.TextEngActivity
import com.example.signlang.texttovids.greek.TextGreekActivity
import com.example.signlang.texttovids.turkish.TextTurkishActivity
import com.example.signlang.words.ArabCatActivity
import com.example.signlang.words.EnglishWordsActivity
import com.example.signlang.words.GreekWordActivity
import com.example.signlang.words.TurkishWordActivity

class SelectTextLangActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectTextLangBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectTextLangBinding.inflate(layoutInflater)
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
            startActivity(Intent(this, SelectCatsActivity::class.java))
        }
        val btnInd = binding.btnEng
        btnInd.setOnClickListener {
            startActivity(Intent(this, TextEngActivity::class.java))
        }
        val btnTur = binding.btnTur
        btnTur.setOnClickListener {
            startActivity(Intent(this, TextTurkishActivity::class.java))
        }
        val btnGre = binding.btnGreece
        btnGre.setOnClickListener {
            startActivity(Intent(this, TextGreekActivity::class.java))
        }
    }
}