package com.jadalai.reinavalera1960.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TextToSpeechManager private constructor(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking = _isSpeaking.asStateFlow()

    private val _currentUtteranceId = MutableStateFlow<String?>(null)
    val currentUtteranceId = _currentUtteranceId.asStateFlow()

    init {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
                _currentUtteranceId.value = utteranceId
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                _currentUtteranceId.value = null
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                _currentUtteranceId.value = null
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isSpeaking.value = false
                _currentUtteranceId.value = null
            }
        })
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
        }
    }

    fun speak(text: String, isEnglish: Boolean, utteranceId: String = "bible_tts") {
        if (!isInitialized || tts == null) return
        
        val locale = if (isEnglish) Locale.US else Locale("es", "ES")
        tts?.language = locale

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        if (tts != null && isInitialized) {
            tts?.stop()
            _isSpeaking.value = false
            _currentUtteranceId.value = null
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        _isSpeaking.value = false
        _currentUtteranceId.value = null
    }

    companion object {
        @Volatile
        private var INSTANCE: TextToSpeechManager? = null

        fun getInstance(context: Context): TextToSpeechManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TextToSpeechManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
