package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.repository.OnboardingRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckNicknameUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val onboardingRepository: OnboardingRepository
) {

    suspend operator fun invoke(nickname: String): Boolean =
        withContext(ioDispatcher) {
            onboardingRepository.checkNickname(nickname).body()!!.success
        }
}