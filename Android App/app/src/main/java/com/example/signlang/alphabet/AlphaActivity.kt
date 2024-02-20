package com.example.signlang.alphabet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
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
import com.example.signlang.Model.Frames
import com.example.signlang.Model.Output
import com.example.signlang.R
import com.example.signlang.databinding.ActivityAlphaBinding
import com.example.signlang.remote.RetrofitConfig
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class AlphaActivity : AppCompatActivity() {
    lateinit var tts: TextToSpeech
    private lateinit var binding: ActivityAlphaBinding
    private val TAG = "AlphaActivity"
    private lateinit var handler: Handler
    private lateinit var textureView: TextureView
    private lateinit var handlerThread: HandlerThread
    private lateinit var cameraManager: CameraManager

    private var recordFlag = false
    private var timerFlag = false
    private var frames = mutableListOf<String>()
    private var playFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlphaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set the language and voice
                tts.language = Locale.US
                tts.voice = Voice("en-us-x-sfg#male_2-local", Locale.US, 1, 1, false, null)
            }
        }
    }

    override fun onStart()
    {
        super.onStart()
        val back = binding.ivback
        back.setOnClickListener {
            textureView.surfaceTexture!!.release()
            handlerThread.quitSafely()
            finish()

        }
        textureView = binding.TXVideo
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
                if (recordFlag&&frames.size<=1){
                    recordFlag = false
                    var bitmap = textureView.bitmap
                    if (bitmap != null ){
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val imageBytes = baos.toByteArray()
                        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                        frames.add(encodedImage)
                        upload(frames)
                    }
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
                playFlag = true
                recordFlag = true
                binding.ivStartIcon.setImageResource(R.drawable.pause)
            }
            else{
                playFlag = false
                recordFlag = false
                binding.ivStartIcon.setImageResource(R.drawable.start)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun openCamera() {
        cameraManager.openCamera(cameraManager.cameraIdList[0],object : CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                val surfaceTexture = textureView.surfaceTexture
                val surface = Surface(surfaceTexture)
                val captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)
                camera.createCaptureSession(
                    listOf(surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            session.setRepeatingRequest(captureRequest.build(), null, null)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {

                        }

                    },
                    handler
                )
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                TODO("Not yet implemented")
            }
        },handler)
    }
    private fun reset(){
        recordFlag = playFlag
        frames.clear()

    }
    private fun print(message :String){
        Toast.makeText(this@AlphaActivity,message, Toast.LENGTH_LONG).show()
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
            RetrofitConfig.getServiceInstance().fetchSignPic(Frames(json)).enqueue(object :
                Callback<Output> {
                override fun onResponse(call: Call<Output>, response: Response<Output>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            viewModel(it.placement)
                            tts.speak(it.placement, TextToSpeech.QUEUE_FLUSH, null, null)
                            CoroutineScope(Dispatchers.Default).launch {
                                delay(2000)
                                reset()
                            }

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
    override fun onDestroy() {
        super.onDestroy()
        textureView.surfaceTexture!!.release()
        handlerThread.quitSafely()
    }
    private fun viewModel(it: String) {
        if(it != "nothing") {
            binding.tvResult.append(" $it")
        }
    }
}