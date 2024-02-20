package com.example.signlang.texttovids.turkish

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Base64
import android.widget.MediaController
import android.widget.Toast
import com.example.signlang.Model.Output
import com.example.signlang.Model.Text
import com.example.signlang.R
import com.example.signlang.databinding.ActivityTextHospBinding
import com.example.signlang.databinding.ActivityTextTurkishBinding
import com.example.signlang.remote.RetrofitConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TextTurkishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextTurkishBinding
    private lateinit var speechRecognizer: SpeechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextTurkishBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    var startFlag = false
    var recordFlag = false
    override fun onStart() {
        super.onStart()

        val btnRecord = binding.btnRecord
        btnRecord.setOnClickListener {
            if (!recordFlag) {
                recordFlag = true
                btnRecord.setImageResource(R.drawable.start__recording)
                startSpeechToText()
            } else {
                recordFlag = false
                btnRecord.setImageResource(R.drawable.record)
                speechRecognizer.stopListening()
            }
        }
        val mediaController = MediaController(this)
        val btnBack = binding.ivback
        btnBack.setOnClickListener {
            finish()
        }
        var btnFlag = false
        val btnSend = binding.btnSend
        btnSend.setOnClickListener {
            val text = binding.tvText.text.toString()
            if (text.isEmpty()) {
                print("Please Enter Some Text To Be Translate")
            } else {
                btnFlag = true
                binding.tvTextAfterSent.text = text
                upload(text)


            }
        }

        val btnStart = binding.btnStat
        btnStart.setOnClickListener {
            if (btnFlag) {
                if (!startFlag) {
                    btnStart.setImageResource(com.example.signlang.R.drawable.pause)
                    startFlag = true
                    binding.videoPlayer.start()

                } else {
                    btnStart.setImageResource(com.example.signlang.R.drawable.start)
                    startFlag = false
                    binding.videoPlayer.pause()

                }
            }
        }
    }

    private fun startSpeechToText() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                speechRecognizer.stopListening()
                recordFlag = false
                binding.btnRecord.setImageResource(R.drawable.record)
            }

            override fun onError(error: Int) {
                print(error.toString())
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val result = matches[0]
                    val text = binding.tvText.text
                    binding.tvText.setText(text.append(" $result"))
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        try {
            speechRecognizer.startListening(speechIntent)
        } catch (e: ActivityNotFoundException) {
            print(e.stackTrace)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (recordFlag) {
            speechRecognizer.stopListening()
        }

    }

    private fun print(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun upload(word: String) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("q", word)
            jsonObject.put("cat", "turkish")
            RetrofitConfig.getServiceInstance().fetchVideo(Text(jsonObject)).enqueue(object :
                Callback<Output> {
                override fun onResponse(call: Call<Output>, response: Response<Output>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val videoData = Base64.decode(it.placement, Base64.DEFAULT)
                            saveVideoToFile(videoData)
                            binding.videoPlayer.setVideoPath(getFileStreamPath("temp.mp4").path)
                            binding.videoPlayer.setMediaController(MediaController(this@TextTurkishActivity))
                            binding.videoPlayer.start()

                            binding.btnStat.setImageResource(R.drawable.pause)
                            startFlag = true
                            binding.videoPlayer.setOnCompletionListener {
                                binding.btnStat.setImageResource(R.drawable.start)
                                startFlag = false
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
        } catch (e: Exception) {
            print(e.message!!)
        }
    }

    private fun saveVideoToFile(videoData: ByteArray) {
        val outputStream = openFileOutput("temp.mp4", MODE_PRIVATE)
        outputStream.write(videoData)
        outputStream.close()
    }

}