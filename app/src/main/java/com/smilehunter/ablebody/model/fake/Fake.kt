package com.smilehunter.ablebody.model.fake

import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.NotificationItemData
import com.smilehunter.ablebody.model.ProductItemData

val fakeProductItemData =
    ProductItemData(
        content = listOf(
            ProductItemData.Item(
                id = 52,
                name = "나이키 스포츠웨어 에센셜",
                price = 35000,
                salePrice = null,
                brandName = "NIKE",
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000052.png",
                isSingleImage = false,
                url = "https://www.nike.com/kr/t/%EC%8A%A4%ED%8F%AC%EC%B8%A0%EC%9B%A8%EC%96%B4-%EC%97%90%EC%84%BC%EC%85%9C-%EC%97%AC%EC%84%B1-%EB%B0%94%EC%9D%B4%ED%81%AC-%EC%87%BC%EC%B8%A0-CATk7i6a/CZ8527-010?utm_source=Google&utm_medium=PS&utm_campaign=365DIGITAL_Google_SP_pMAX_all_all&utm_term=pMax.af1&cp=19808619078_sh_&gclid=Cj0KCQiAyracBhDoARIsACGFcS62du-shK2_3PvFuZiSvl3FIRoD-xJxDJE4SnXfBRJxrYV4Sx5-WA0aAg57EALw_wcB",
                avgStarRating = null
            ),
            ProductItemData.Item(
                id = 39,
                name = "나이키 드라이 핏 런 디비전 챌린저",
                price = 59000,
                salePrice = null,
                brandName = "NIKE",
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000039.png",
                isSingleImage = false,
                url = "https://www.nike.com/kr/t/%EB%93%9C%EB%9D%BC%EC%9D%B4-%ED%95%8F-%EB%9F%B0-%EB%94%94%EB%B9%84%EC%A0%84-%EC%B1%8C%EB%A6%B0%EC%A0%80-%EB%82%A8%EC%84%B1-5%EC%9D%B8%EC%B9%98-%EB%B8%8C%EB%A6%AC%ED%94%84-%EB%9D%AC%EB%8B%9D-%EC%87%BC%EC%B8%A0-jqNurFx0",
                avgStarRating = "5.0(1)"
            )
        ),
        first = true,
        last = true,
        number = 0,
        totalPages = 1
    )

val fakeCodyItemData =
    CodyItemData(
        content = listOf(
            CodyItemData.Item(
                id = 29,
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/29/6246673 bytes_1686277691881.jpg",
                createDate = "2023-07-30",
                comments = 0,
                likes = 1,
                views = 51,
                isSingleImage = false
            ),
            CodyItemData.Item(
                id = 24,
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/24/3911923 bytes_1684756948931.jpg",
                createDate = "2023-07-29",
                comments = 0,
                likes = 0,
                views = 76,
                isSingleImage = true
            ),
        ),
        totalPages = 1,
        last = true,
        number = 0,
        first = true
    )

val fakeNotificationItemData =
    NotificationItemData(
        content = listOf(
            NotificationItemData.Content(
                id = 0L,
                senderNickname = "sooper",
                senderProfileImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Pictures/green.png",
                createDate ="2023-09-19T17:09:37",
                text = "sooper님이 회원님의 코디 게시물을 좋아해요.",
                uri= "",
                checked = false
            ),
            NotificationItemData.Content(
                id = 0L,
                senderNickname = "리코더와누네띠네",
                senderProfileImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Pictures/green.png",
                createDate ="2023-09-10T18:27:19",
                text = "sooper님이 회원님의 코디 게시물을 좋아해요.",
                uri= "",
                checked = true
            )
        ),
        totalPages = 1,
        number = 0,
        first = true,
        last = true
    )