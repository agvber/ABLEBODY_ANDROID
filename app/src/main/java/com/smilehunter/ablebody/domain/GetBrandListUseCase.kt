package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.data.BrandMainResponseData
import com.smilehunter.ablebody.data.repository.BrandRepository
import com.smilehunter.ablebody.model.BrandListData
import javax.inject.Inject

class GetBrandListUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {

    suspend operator fun invoke(
        sortingMethod: SortingMethod
    ): List<BrandListData> =
        brandRepository.brandMain(sortingMethod).body()!!.data!!.map { it.toDomain() }

}

private fun BrandMainResponseData.toDomain() = BrandListData(
    name, id, thumbnail, subName, brandGender, maxDiscount
)