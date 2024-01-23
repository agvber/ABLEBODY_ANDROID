package com.smilehunter.ablebody.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.smilehunter.ablebody.utils.CalculateUserElapsedTime.*

sealed interface CalculateUserElapsedTime {
    object Recent: CalculateUserElapsedTime
    data class Minutes(val minutes: Long): CalculateUserElapsedTime
    data class Hour(val hour: Long): CalculateUserElapsedTime
    data class Date(val year: Int, val month: Int, val day: Int): CalculateUserElapsedTime
}

fun calculateUserElapsedTime(dateTime: String): CalculateUserElapsedTime {
    val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    val targetDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    val duration = Duration.between(targetDateTime, currentDateTime)
    val secondsPassed = duration.toSeconds()

    return when {
        secondsPassed < 60 -> Recent
        secondsPassed < 3600 -> Minutes(duration.toMinutes())
        secondsPassed < 86400 -> Hour(duration.toHours())
        else -> Date(
            year = targetDateTime.year,
            month = targetDateTime.month.value,
            day = targetDateTime.dayOfMonth
        )
    }
}

sealed interface CalculateSportElapsedTime {
    data class Second(val second: Long): CalculateSportElapsedTime
    data class Minutes(val minutes: Long): CalculateSportElapsedTime
    data class Hour(val hour: Long): CalculateSportElapsedTime
    data class Day(val day: Long): CalculateSportElapsedTime
    data class Week(val week: Long): CalculateSportElapsedTime
    data class Month(val month: Long): CalculateSportElapsedTime
    data class Year(val year: Long): CalculateSportElapsedTime
}

fun calculateSportElapsedTime(dateTime: String): CalculateSportElapsedTime {
    val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    val targetDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    val duration = Duration.between(targetDateTime, currentDateTime)
    val secondsPassed = duration.toSeconds()

    return when {
        secondsPassed < 0 -> CalculateSportElapsedTime.Second(0)
        secondsPassed < 60 ->  CalculateSportElapsedTime.Second(secondsPassed)
        secondsPassed < 3600 -> CalculateSportElapsedTime.Minutes(duration.toMinutes())
        secondsPassed < 86400 -> CalculateSportElapsedTime.Hour(duration.toHours())
        secondsPassed < 604800 -> CalculateSportElapsedTime.Day(duration.toDays())
        secondsPassed < 2629800 -> CalculateSportElapsedTime.Week(duration.toDays() / 7)
        secondsPassed < 31556952 -> CalculateSportElapsedTime.Month(duration.toDays() / 30)
        else -> CalculateSportElapsedTime.Year(duration.toDays() / 365)
    }
}