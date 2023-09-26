package com.example.ablebody_android.data.dto.response.data

import com.example.ablebody_android.data.dto.NetworkAuthorityName
import com.example.ablebody_android.data.dto.NetworkNotificationType

data class GetMyNotiResponseData(
    val content: List<Content>,
    val pageable: Pageable,
    val totalPages: Int,
    val totalElements: Int,
    val last: Boolean,
    val number: Int,
    val sort: Sort,
    val size: Int,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
) {
    data class Content(
        val id: Long,
        val createDate: String,
        val notiType: NetworkNotificationType,
        val title: String?,
        val body: String?,
        val from: NotificationUser,
        val to: NotificationUser,
        val content: String,
        val url: String,
        val checked: Boolean
    )

    data class NotificationUser(
        val createDate: String,
        val modifiedDate: String,
        val gender: String,
        val uid: String,
        val nickname: String,
        val name: String,
        val height: Int,
        val weight: Int?,
        val job: String?,
        val profileUrl: String,
        val introduction: String?,
        val creatorPoint: Int,
        val authorities: List<Authority>
    )

    data class Authority(
        val authorityName: NetworkAuthorityName
    )

    data class Pageable(
        val sort: Sort,
        val offset: Int,
        val pageNumber: Int,
        val pageSize: Int,
        val paged: Boolean,
        val unpaged: Boolean
    )

    data class Sort(
        val empty: Boolean,
        val sorted: Boolean,
        val unsorted: Boolean
    )

}