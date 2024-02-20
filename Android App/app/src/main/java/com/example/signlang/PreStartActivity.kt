package com.example.signlang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.databinding.ActivityPreStartBinding
import com.example.signlang.texttovids.SelectTextLangActivity

class PreStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val toVideo = binding.btnVid
        val toText = binding.btnText
        toVideo.setOnClickListener {
            startActivity(Intent(this,SelectTextLangActivity::class.java))
        }
        toText.setOnClickListener {
            startActivity(Intent(this,StartActivity::class.java))
        }
    }
}