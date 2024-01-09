package com.smilehunter.ablebody.model.fake

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.model.ItemDetailData

val fakeItemDetailData = ItemDetailData(
        id = 1453,
        item = ItemDetailData.Item(
            id = 1453,
            name = "버뮤다 와이드 팬츠 2Color",
            price = 59000,
            salePrice = 42000,
            redirectUrl = "https://jeleve.co.kr/product/233/category/97/display/1/",
            redirectText = "jeleve.co.kr",
            brand = ItemDetailData.Item.Brand(
                id = 194,
                name = "JELEVE",
                subName = "제이엘브",
                thumbnail = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/brand-logo/JELEVE.png",
                isLaunched = true
            ),
            images = listOf(
                "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0001462.png",
                "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0001462_1.png",
                "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0001462_10.png",
                "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0001462_11.png"
            ),
            avgStarRating = "4.9(17)",
            deliveryFee = null,
            deleted = false
        ),
        itemReviews = listOf(
            ItemDetailData.ItemReview(
                createDate = "2023-09-13T13:02:48",
                modifiedDate = "2023-09-13T13:02:48",
                id = 1022,
                creator = ItemDetailData.ItemReview.User(
                    createDate = "2023-07-03T18:03:19",
                    modifiedDate = "2023-12-07T17:19:38",
                    gender = Gender.MALE,
                    uid = "4309509",
                    nickname = "burninggg___",
                    name = "박한수",
                    height = 180,
                    weight = 78,
                    job = "스포츠영양코치",
                    profileUrl = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/user/4309509/37150 bytes_1688656648412.jpg",
                    introduction = null
                ),
                images = listOf(
                    "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1077/1978768 bytes_1694577767735.jpg"
                ),
                review = "안감부드러워서 살닿는 느낌도 좋고 땀도 금방말라요 널널해서 상체운동, 하체운동, 유산소 할때 가리지않고 다 입어요 일상복에도 편하게 입기좋아요",
                size = "FREE",
                starRating = 5.0f
            ),
            ItemDetailData.ItemReview(
                createDate = "2023-09-13T13:02:48",
                modifiedDate = "2023-09-13T13:02:48",
                id = 1022,
                creator = ItemDetailData.ItemReview.User(
                    createDate = "2023-07-03T18:03:19",
                    modifiedDate = "2023-12-07T17:19:38",
                    gender = Gender.MALE,
                    uid = "4309509",
                    nickname = "burninggg___",
                    name = "박한수",
                    height = 180,
                    weight = 78,
                    job = "스포츠영양코치",
                    profileUrl = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/user/4309509/37150 bytes_1688656648412.jpg",
                    introduction = null
                ),
                images = listOf(
                    "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1077/1978768 bytes_1694577767735.jpg"
                ),
                review = "안감부드러워서 살닿는 느낌도 좋고 땀도 금방말라요 널널해서 상체운동, 하체운동, 유산소 할때 가리지않고 다 입어요 일상복에도 편하게 입기좋아요",
                size = "FREE",
                starRating = 5.0f
            )
        ),
        homePosts = listOf(
            ItemDetailData.HomePost(
                id = 1221,
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1228/1558945 bytes_1701061171166.jpg",
                createDate = "2023-11-27",
                comments = 171,
                likes = 171,
                views = 44,
                plural = true
            ),
            ItemDetailData.HomePost(
                id = 1222,
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1228/1558945 bytes_1701061171166.jpg",
                createDate = "2023-11-27",
                comments = 171,
                likes = 171,
                views = 44,
                plural = true
            ),
            ItemDetailData.HomePost(
                id = 1223,
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1228/1558945 bytes_1701061171166.jpg",
                createDate = "2023-11-27",
                comments = 171,
                likes = 171,
                views = 44,
                plural = true
            ),
            ItemDetailData.HomePost(
                id = 1224,
                imageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1228/1558945 bytes_1701061171166.jpg",
                createDate = "2023-11-27",
                comments = 171,
                likes = 171,
                views = 44,
                plural = true
            )
        ),
        itemOptionList = listOf(
            ItemDetailData.ItemOptionData(
                id = 1,
                content = "색상",
                itemId = 1453,
                itemOptionDetailList = listOf(
                    ItemDetailData.ItemOptionData.ItemOptionDetailData(
                        id = 101,
                        content = "레드",
                        stock = 50,
                        price = 42000
                    ),
                    // 다른 색상들도 추가할 수 있습니다.
                )
            ),
            ItemDetailData.ItemOptionData(
                id = 2,
                content = "사이즈",
                itemId = 1453,
                itemOptionDetailList = listOf(
                    ItemDetailData.ItemOptionData.ItemOptionDetailData(
                        id = 101,
                        content = "레드",
                        stock = 50,
                        price = 42000
                    )
                )
            )
        ),
    bookmarked = true,
    deleted = false,
    seller = ItemDetailData.Seller(
        businessName = "(주)코스모스코리아그룹 / 김재훈",
        brand = "JELEVE",
        businessNumber = "507-86-00975",
        reportNumber = "2020-서울서초-2913호",
        contactNumber = "070-4903-0880",
        emailAddress = "law@kosmerce.kr",
        roadAddress = "06633 서울특별시 서초구 서초대로 320 (서초동) 4층(하림인터내셔날 빌딩)"
    ),
    detailImageUrls = listOf(
        "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_1.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_2.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_3.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_4.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_5.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_6.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_7.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_8.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_9.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_10.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_11.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_12.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_13.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_14.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_15.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_16.png", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/itemDetailImage/jeleve/ABLE-0001462_17.png"
    )
)

