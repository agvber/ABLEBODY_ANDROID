package com.example.ablebody_android.onboarding.utils

import com.example.ablebody_android.onboarding.data.NicknameRule

fun checkNicknameRule(
    nickname: String
) : NicknameRule {
    val regex1 = "[0-9a-z_.]{1,20}".toRegex()
    val regex3 = "^[.].*\$".toRegex()
    val regex4 = "^[0-9]*\$".toRegex()
    val regex7 = "^[._]*\$".toRegex()

    return if (nickname.isEmpty()) {
        NicknameRule.Nothing
    } else if (!isNicknameRuleMatch(nickname, regex1)) {
        NicknameRule.UnAvailable
    } else if (isNicknameRuleMatch(nickname, regex3)) {
        NicknameRule.StartsWithDot
    } else if (isNicknameRuleMatch(nickname, regex4)) {
        NicknameRule.OnlyNumber
    } else if(isNicknameRuleMatch(nickname, regex7)) {
        NicknameRule.UnAvailable
    } else {
        NicknameRule.Available
    }
}