package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.request.ReportRequest
import com.smilehunter.ablebody.data.dto.response.ReportResponse

interface ManageRepository {

    suspend fun report(reportRequest: ReportRequest): ReportResponse
}