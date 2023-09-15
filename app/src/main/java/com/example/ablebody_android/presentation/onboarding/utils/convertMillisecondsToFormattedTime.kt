package com.example.ablebody_android.presentation.onboarding.utils

import com.example.ablebody_android.presentation.onboarding.data.FormattedTimeData
import java.util.concurrent.TimeUnit

fun convertMillisecondsToFormattedTime(milliseconds: Long): FormattedTimeData {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

    return FormattedTimeData(
        minutes = minutes.toInt(),
        seconds = seconds.toInt() - TimeUnit.MINUTES.toSeconds(minutes).toInt()
    )
}
