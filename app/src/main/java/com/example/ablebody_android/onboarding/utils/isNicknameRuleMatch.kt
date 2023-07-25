package com.example.ablebody_android.onboarding.utils

fun isNicknameRuleMatch(path: String, regex: Regex): Boolean = path.matches(regex)