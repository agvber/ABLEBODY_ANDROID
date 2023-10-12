package com.smilehunter.ablebody.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.smilehunter.ablebody.utils.CalculateElapsedTime.*

sealed interface CalculateElapsedTime {
    data class Second(val second: Long): CalculateElapsedTime
    data class Minutes(val minutes: Long): CalculateElapsedTime
    data class Hour(val hour: Long): CalculateElapsedTime
    data class Day(val day: Long): CalculateElapsedTime
    data class Week(val week: Long): CalculateElapsedTime
    data class Month(val month: Long): CalculateElapsedTime
    data class Year(val year: Long): CalculateElapsedTime
}

fun calculateElapsedTime(dateTime: String): CalculateElapsedTime {
    val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    val targetDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    val duration = Duration.between(targetDateTime, currentDateTime)
    val secondsPassed = duration.toSeconds()

    return when {
        secondsPassed < 60 ->  Second(secondsPassed)
        secondsPassed < 3600 -> Minutes(duration.toMinutes())
        secondsPassed < 86400 -> Hour(duration.toHours())
        secondsPassed < 604800 -> Day(duration.toDays())
        secondsPassed < 2629800 -> Week(duration.toDays() / 7)
        secondsPassed < 31556952 -> Month(duration.toDays() / 30)
        else -> Year(duration.toDays() / 365)
    }
}