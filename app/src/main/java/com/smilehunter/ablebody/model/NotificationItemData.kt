package com.smilehunter.ablebody.model

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class NotificationItemData(
    val content: List<Content>,
    val totalPages: Int,
    val last: Boolean,
    val number: Int,
    val first: Boolean,
) {
    data class Content(
        val id: Long,
        val senderNickname: String,
        val senderProfileImageURL: String,
        val createDate: String,
        val text: String,
        val uri: String,
        val checked: Boolean,
    ) {
        private val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        private val targetDateTime = LocalDateTime.parse(createDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        private val duration = Duration.between(targetDateTime, currentDateTime)
        private val secondsPassed = duration.toSeconds()

        val passedTime: NotificationPassedTime = when {
            secondsPassed < 60 ->  NotificationPassedTime.Second(secondsPassed)
            secondsPassed < 3600 -> NotificationPassedTime.Minutes(duration.toMinutes())
            secondsPassed < 86400 -> NotificationPassedTime.Hour(duration.toHours())
            secondsPassed < 604800 -> NotificationPassedTime.Day(duration.toDays())
            secondsPassed < 2629800 -> NotificationPassedTime.Week(duration.toDays() / 7)
            secondsPassed < 31556952 -> NotificationPassedTime.Month(duration.toDays() / 30)
            else -> NotificationPassedTime.Year(duration.toDays() / 365)
        }
    }
}

sealed interface NotificationPassedTime {
    data class Second(val second: Long): NotificationPassedTime
    data class Minutes(val minutes: Long): NotificationPassedTime
    data class Hour(val hour: Long): NotificationPassedTime
    data class Day(val day: Long): NotificationPassedTime
    data class Week(val week: Long): NotificationPassedTime
    data class Month(val month: Long): NotificationPassedTime
    data class Year(val year: Long): NotificationPassedTime
}