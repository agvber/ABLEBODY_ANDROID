package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.data.BrandMainResponseData
import com.smilehunter.ablebody.data.repository.BrandRepository
import com.smilehunter.ablebody.model.BrandListData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBrandListUseCase @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val brandRepository: BrandRepository
) {

    suspend operator fun invoke(
        sortingMethod: SortingMethod
    ): List<BrandListData> = withContext(ioDispatcher) {
        brandRepository.brandMain(sortingMethod).body()!!.data!!.map { it.toDomain() }
    }

}

private fun BrandMainResponseData.toDomain() = BrandListData(
    name = name,
    id = id,
    thumbnail = thumbnail,
    subName = subName,
    brandGender = brandGender,
    maxDiscount = maxDiscount
)