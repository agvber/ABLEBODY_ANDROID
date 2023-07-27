package com.example.ablebody_android.onboarding.utils

import com.example.ablebody_android.onboarding.data.FormattedTimeTimeData
import java.util.concurrent.TimeUnit

fun convertMillisecondsToFormattedTime(milliseconds: Long): FormattedTimeTimeData {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

    return FormattedTimeTimeData(
        minutes = minutes.toInt(),
        seconds = seconds.toInt() - TimeUnit.MINUTES.toSeconds(minutes).toInt()
    )
}
