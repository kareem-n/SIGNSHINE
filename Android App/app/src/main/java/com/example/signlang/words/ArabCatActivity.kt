package com.example.signlang.words

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.databinding.ActivityArabCatBinding
import com.example.signlang.words.arabcat.BankActivity
import com.example.signlang.words.arabcat.CafeActivity
import com.example.signlang.words.arabcat.HospActivity
import com.example.signlang.words.arabcat.TrainActivity

class ArabCatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArabCatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityArabCatBinding.inflate(layoutInflater)
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
            startActivity(Intent(this,TrainActivity::class.java))
        }
        hosp.setOnClickListener {
            startActivity(Intent(this,HospActivity::class.java))
        }
        cafe.setOnClickListener {
            startActivity(Intent(this,CafeActivity::class.java))
        }
        bank.setOnClickListener {
            startActivity(Intent(this,BankActivity::class.java))
        }
    }
}