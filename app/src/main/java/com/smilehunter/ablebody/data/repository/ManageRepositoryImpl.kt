package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.request.ReportRequest
import com.smilehunter.ablebody.data.dto.response.ReportResponse
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class ManageRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): ManageRepository {

    override suspend fun report(reportRequest: ReportRequest): ReportResponse {
        return networkService.report(reportRequest)
    }
}