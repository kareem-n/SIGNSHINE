package com.example.signlang.words.arabcat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Base64
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.signlang.Model.Frames
import com.example.signlang.Model.Output
import com.example.signlang.R
import com.example.signlang.databinding.ActivityHospBinding
import com.example.signlang.databinding.ActivityTrainBinding
import com.example.signlang.remote.RetrofitConfig
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class TrainActivity : AppCompatActivity() {
    lateinit var tts: TextToSpeech
    private lateinit var handlerThread: HandlerThread
    private val TAG = "MainActivity"
    private var playFlag = false
    private var frames = mutableListOf<String>()
    private var recordFlag : Boolean = false
    private var counter = 1
    private var timerFlag = false
    private lateinit var binding : ActivityTrainBinding
    private lateinit var textureView: TextureView
    private lateinit var cameraManager: CameraManager
    private lateinit var handler: Handler
    private lateinit var cameraDevice: CameraDevice
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textureView = binding.TXVideo
        recordFlag = false
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set the language and voice
                tts.language = Locale.US
                tts.voice = Voice("en-us-x-sfg#male_2-local", Locale.US, 1, 1, false, null)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val back = binding.ivback
        back.setOnClickListener {
            textureView.surfaceTexture!!.release()
            handlerThread.quitSafely()
            finish()
        }
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera()
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
            }
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//                take 2 frames leave 1
                if (recordFlag){
                    if (timerFlag) {
                        timer()
                        timerFlag = false
                    }
                    var bitmap = textureView.bitmap
                    if (bitmap != null ){
                        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val imageBytes = baos.toByteArray()
                        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                        frames.add(encodedImage)
                    }
                    counter++
                }
            }
        }
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        val play = binding.ivStartIcon
        play.setOnClickListener {
            if(!playFlag){
                //for my use onlyyyyyyyyyy
//                CoroutineScope(Dispatchers.Default).launch{
//                    delay(5000)
//                }
                play.setImageResource(R.drawable.pause)
                recordFlag = true
                timerFlag = true
                playFlag = true
            }
            else{
                recordFlag = false
                timerFlag = false
                playFlag = false
                play.setImageResource(R.drawable.start)
            }
        }
    }
    private fun upload(frames : List<String>){
        try{
            val json = mutableListOf<JSONObject>()
            for ((i, frame) in frames.withIndex()){
                val jsonObject = JSONObject()
                jsonObject.put("Image", frame)
                jsonObject.put("name", i)
                json.add(jsonObject)
            }
            RetrofitConfig.getServiceInstance().fetchSignVideoTrain(Frames(json)).enqueue(object :
                Callback<Output> {
                override fun onResponse(call: Call<Output>, response: Response<Output>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            viewModel(it)
                            tts.speak(it.placement, TextToSpeech.QUEUE_FLUSH, null, null)

                            recordFlag = playFlag
                            timerFlag = playFlag
                        }
                    }
                }

                override fun onFailure(call: Call<Output>, t: Throwable) {
                    t.message?.let {
                        print(it)
                    }
                }
            })
        }catch (e:Exception){
            Log.e(TAG, e.message!! )
        }
    }


    private fun viewModel(it: Output) {
        binding.tvResult.append(" ${it.placement}")
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        cameraManager.openCamera(cameraManager.cameraIdList[0],object : CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                val surfaceTexture = textureView.surfaceTexture
                val surface = Surface(surfaceTexture)
                val captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)
                cameraDevice.createCaptureSession(listOf(surface),object : CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        session.setRepeatingRequest(captureRequest.build(),null,null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }

                },handler)
            }

            override fun onDisconnected(camera: CameraDevice) {
                TODO("Not yet implemented")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                TODO("Not yet implemented")
            }
        },handler)
    }
    private fun getPermission(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA),101)
            }
            else{
                print("fuck your phone")
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
    }


    override fun onDestroy() {
        super.onDestroy()
        textureView.surfaceTexture!!.release()
        handlerThread.quitSafely()
    }
    private fun print(message :String){
        Toast.makeText(this@TrainActivity,message, Toast.LENGTH_LONG).show()
    }
    private fun timer(){
        CoroutineScope(Dispatchers.Default).launch {
            delay(timeMillis = 2000)
            withContext(Dispatchers.Main) {
                print(frames.size.toString())
            }
            recordFlag =  false
            counter = 1
            upload(frames)
            frames.clear()

        }
    }
}