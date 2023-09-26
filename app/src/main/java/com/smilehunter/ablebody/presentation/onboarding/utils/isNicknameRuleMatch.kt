package com.smilehunter.ablebody.presentation.onboarding.utils

fun isNicknameRuleMatch(path: String, regex: Regex): Boolean = path.matches(regex)