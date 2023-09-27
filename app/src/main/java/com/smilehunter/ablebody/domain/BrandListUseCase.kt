package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.data.BrandMainResponseData
import com.smilehunter.ablebody.data.repository.BrandRepository
import javax.inject.Inject

class BrandListUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {

    suspend operator fun invoke(
        sortingMethod: SortingMethod
    ): List<BrandMainResponseData>? =
        brandRepository.brandMain(sortingMethod).body()?.data

}