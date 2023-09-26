package com.smilehunter.ablebody.presentation.onboarding.data

enum class NicknameRule(val positive: Boolean, val description: String) {
    Available(true, "사용 가능한 닉네임이에요."),
    InUsed(false, "이미 사용 중인 닉네임이에요."),
    StartsWithDot(false, "닉네임은 마침표로 시작할 수 없어요."),
    OnlyNumber(false, "닉네임은 숫자로만 이뤄질 수 없어요."),
    UnAvailable(false, "사용할 수 없는 닉네임이에요."),
    Nothing(true, "20자 이내 영문, 숫자, 밑줄 및 마침표만 사용 가능해요.")
}
