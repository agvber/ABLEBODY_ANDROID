package com.smilehunter.ablebody.data.dto.request

data class ReportRequest(
    val contentType: ContentType,
    val id: Long,
    val reason: String,
    val content: String
) {

    enum class ContentType {
        HomeBoard,
        HomeComment,
        HomeReply,
        ItemDetail,
        ItemComment,
        ItemReply,
        User,
        Bookmark
    }
    // https://spiffy-vegetarian-7f4.notion.site/ContentType-a8143d61cb10438fb5635ded95667322
}
