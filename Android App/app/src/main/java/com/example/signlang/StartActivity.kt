package com.example.signlang

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.signlang.alphabet.ArEnPicActivity
import com.example.signlang.databinding.ActivityStartBinding
import com.example.signlang.words.WordsPicActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val btnBack = binding.ivback
        getPermission()
        btnBack.setOnClickListener {
            finish()
        }
        val video =  binding.btnWords
        video.setOnClickListener {
            startActivity(Intent(this, WordsPicActivity::class.java))
        }
        val alpha =  binding.btnAlpha
        alpha.setOnClickListener {
            startActivity(Intent(this, ArEnPicActivity::class.java))
        }
    }
    private fun getPermission(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.RECORD_AUDIO),101)
            }
            else{
                print("your phone is not good enough")
                finish()
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]!= PackageManager.PERMISSION_GRANTED ){
            getPermission()
        }
        else if (grantResults[1]!= PackageManager.PERMISSION_GRANTED){
            getPermission()
        }
        else{
            print("Permission is accepted")
        }
    }
}