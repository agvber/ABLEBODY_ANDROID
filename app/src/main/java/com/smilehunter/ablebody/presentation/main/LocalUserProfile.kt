package com.smilehunter.ablebody.presentation.main

import com.smilehunter.ablebody.model.LocalUserInfoData

object LocalUserProfile {

    private var instance: LocalUserInfoData? = null

    fun initialize(user: LocalUserInfoData) {
        instance = user
    }

    fun getInstance(): LocalUserInfoData {
        return instance!!
    }
}