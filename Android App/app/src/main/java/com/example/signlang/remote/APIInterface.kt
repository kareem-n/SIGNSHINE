package com.example.signlang.remote

import com.example.signlang.Model.Frames
import com.example.signlang.Model.Output
import com.example.signlang.Model.Text
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {
    @POST("predictBank")
    fun fetchSignVideoBank(@Body frames : Frames):Call<Output>
    @POST("predictCafe")
    fun fetchSignVideoCafe(@Body frames : Frames):Call<Output>
    @POST("predictTrain")
    fun fetchSignVideoTrain(@Body frames : Frames):Call<Output>
    @POST("predictHospital")
    fun fetchSignVideoHospital(@Body frames : Frames):Call<Output>
    @POST("predictEng")
    fun fetchSignVideoEng(@Body frames : Frames):Call<Output>
    @POST("predictGreek")
    fun fetchSignVideoGreek(@Body frames : Frames):Call<Output>
    @POST("predictTur")
    fun fetchSignVideoTurkish(@Body frames : Frames):Call<Output>
    @POST("words")
    fun fetchSignPic(@Body frames : Frames):Call<Output>
    @POST("wordsen")
    fun fetchSignEnPic(@Body frames : Frames):Call<Output>
    @POST("wordstur")
    fun fetchSignTurPic(@Body frames : Frames):Call<Output>
    @POST("text_to_sign")
    fun fetchVideo(@Body frames : Text):Call<Output>
}
interface APIVidsInterface{
    @POST("text_to_sign")
    fun fetchSignVideoBank(@Body frames : Frames):Call<Output>
}