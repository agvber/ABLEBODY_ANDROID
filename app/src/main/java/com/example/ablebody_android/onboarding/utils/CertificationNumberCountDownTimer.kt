package com.example.ablebody_android.onboarding.utils

import android.os.CountDownTimer

class CertificationNumberCountDownTimer {
    private var currentTime = 0L

    private var countDownTimer: CountDownTimer? = null

    fun startCertificationNumberTimer() {
        if (countDownTimer == null) {
            currentTime = 180000L
            countDownTimer = object : CountDownTimer(180000L, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    currentTime -= 1000L
                    callback?.onChangeValue(currentTime)
                }
                override fun onFinish() {
                    countDownTimer = null
                }
            }.start()
        }
    }

    fun cancelCertificationNumberCountDownTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    interface Callback {
        fun onChangeValue(value: Long)
    }

    private var callback: Callback? = null
    fun registerOnChangeListener(callback: Callback) { this.callback = callback }

    fun unRegisterOnChangeListener() { this.callback = null }
}