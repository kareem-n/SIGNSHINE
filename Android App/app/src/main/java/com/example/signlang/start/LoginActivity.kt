package com.example.signlang.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signlang.PreStartActivity
import com.example.signlang.StartActivity
import com.example.signlang.databinding.ActivityLoginBinding
//import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,PreStartActivity::class.java))
        }
        val reg = binding.btnReg
        reg.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }
}