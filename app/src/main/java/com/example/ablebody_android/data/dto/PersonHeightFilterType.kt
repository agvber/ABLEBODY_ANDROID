package com.example.ablebody_android.data.dto

enum class PersonHeightFilterType(val rangeStart: Int?, val rangeEnd: Int?) {
    ALL(rangeStart = null, rangeEnd = null),
    FROM_150_TO_160(rangeStart = 150, rangeEnd = 160),
    FROM_160_TO_170(rangeStart = 160, rangeEnd = 170),
    FROM_170_TO_180(rangeStart = 170, rangeEnd = 180),
    FROM_180_TO_190(rangeStart = 180, rangeEnd = 190),
}