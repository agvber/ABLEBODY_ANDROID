package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.repository.SearchRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecommendKeywordUseCase @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val searchRepository: SearchRepository
) {

    suspend operator fun invoke(): List<String> {
        return withContext(ioDispatcher) {
            searchRepository.uniSearch("").data!!.recommendKeyword.creator
        }
    }
}