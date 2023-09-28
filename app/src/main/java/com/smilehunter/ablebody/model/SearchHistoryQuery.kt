package com.smilehunter.ablebody.model

import com.smilehunter.ablebody.database.model.SearchHistoryEntity

data class SearchHistoryQuery(
    val query: String,
    val queriedTime: Long
)

fun SearchHistoryEntity.asExternalModel() = SearchHistoryQuery(
    query = query,
    queriedTime = queriedTime
)