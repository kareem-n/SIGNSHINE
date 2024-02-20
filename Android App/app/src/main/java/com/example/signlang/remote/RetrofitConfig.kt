package com.example.signlang.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitConfig {
    private fun getRetrofitInstance(): Retrofit{
        return Retrofit.Builder().baseUrl("http://192.168.43.177:5000/").addConverterFactory(GsonConverterFactory.create()).build()
    }
    fun getServiceInstance():APIInterface{
        return getRetrofitInstance().create(APIInterface::class.java)
    }
}
