package com.smilehunter.ablebody.data.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    val hasToken: Boolean

    val registerOnClearedListener: Flow<Unit>

    suspend fun deleteToken()
}

