package com.example.ablebody_android.onboarding.utils

import android.os.CountDownTimer

class CertificationNumberCountDownTimer {
    private var currentTime = 0L
    private var timerIsRunning: Boolean = false

    private val countDownTimer = object : CountDownTimer(180000L, 1000L) {
        override fun onTick(millisUntilFinished: Long) {
            currentTime -= 1000L
            callback?.onChangeValue(currentTime)
        }
        override fun onFinish() {
            timerIsRunning = false
        }
    }

    fun startCertificationNumberTimer() {
        if (!timerIsRunning) {
            currentTime = 180000L
            timerIsRunning = true
            countDownTimer.start()
        }
    }

    fun cancelCertificationNumberCountDownTimer() {
        if (timerIsRunning) {
            countDownTimer.cancel()
            timerIsRunning = false
        }
    }

    interface Callback {
        fun onChangeValue(value: Long)
    }

    private var callback: Callback? = null
    fun registerOnChangeListener(callback: Callback) { this.callback = callback }

    fun unRegisterOnChangeListener() { this.callback = null }
}