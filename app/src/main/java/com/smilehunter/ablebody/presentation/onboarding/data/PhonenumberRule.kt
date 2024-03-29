package com.smilehunter.ablebody.presentation.onboarding.data

enum class PhonenumberRule(val positive: Boolean, val description: String) {
    Correct(true, "사용 가능한 닉네임이에요."),
    Unstyled(false, "휴대폰 번호 양식에 맞지 않아요."),
    Nothing(true, "분 초 남음")
}