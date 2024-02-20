package com.example.signlang.texttovids.arabic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.databinding.ActivitySelectCatsBinding
import com.example.signlang.words.arabcat.BankActivity
import com.example.signlang.words.arabcat.CafeActivity
import com.example.signlang.words.arabcat.HospActivity
import com.example.signlang.words.arabcat.TrainActivity

class SelectCatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectCatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        val btnBack = binding.ivback
        btnBack.setOnClickListener {
            finish()
        }
        val train = binding.btnTrain
        val hosp = binding.btnHosp
        val cafe = binding.btnCafe
        val bank = binding.btnBank
        train.setOnClickListener {
            startActivity(Intent(this, TextTrainActivity::class.java))
        }
        hosp.setOnClickListener {
            startActivity(Intent(this, TextHospActivity::class.java))
        }
        cafe.setOnClickListener {
            startActivity(Intent(this, TextCafeActivity::class.java))
        }
        bank.setOnClickListener {
            startActivity(Intent(this, TextBankActivity::class.java))
        }
    }
}