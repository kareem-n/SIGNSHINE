package com.example.signlang.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.signlang.PreStartActivity
import com.example.signlang.databinding.ActivityRegisterBinding
//import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
//    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        mAuth =

    }

    override fun onStart() {
        super.onStart()
        val reg = binding.btnReg
        reg.setOnClickListener {
            reg.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
            var email = binding.etUser.text.toString()
            val pass = binding.etPass.text.toString()
            val cPass = binding.etPass2.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && cPass.isNotEmpty()){
                if (pass == cPass) {
                    if (!email.contains("@gmail.com")){
                        email+="@gmail.com"
                    }
//                    mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener {
//                        reg.visibility = View.VISIBLE
//                        binding.pbLoading.visibility = View.GONE
//                        print("User registered successfully")
//                        startActivity(Intent(this@RegisterActivity, PreStartActivity::class.java))
//                        finish()
//
//                    }.addOnFailureListener {
//                        reg.visibility = View.VISIBLE
//                        binding.pbLoading.visibility = View.GONE
//                        print(it.message)
//                    }
                } else {
                    reg.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    print("Password and confirm password should match")
                }
            }else{
                reg.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
                print("Please enter valid information")
            }


        }
    }
private fun print(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

}