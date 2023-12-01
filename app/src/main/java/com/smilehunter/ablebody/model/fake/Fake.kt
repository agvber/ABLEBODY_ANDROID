package com.smilehunter.ablebody.model.fake

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.model.BrandListData
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.CreatorDetailData
import com.smilehunter.ablebody.model.NotificationItemData
import com.smilehunter.ablebody.model.OrderItemData
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.model.ReceiptData
import com.smilehunter.ablebody.model.UserInfoData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
val fakeBrandListData = listOf(
    BrandListData(
        name = "NIKE",
        id = 3,
        thumbnail = "",
        subName = "나이키",
        brandGender = ItemGender.UNISEX,
        maxDiscount = 0
    ),
    BrandListData(
        name = "Positive Me",
        id = 36,
        thumbnail = "",
        subName = "포지티브미",
        brandGender = ItemGender.FEMALE,
        maxDiscount = 0
    ),
    BrandListData(
        name = "MAVRK",
        id = 30,
        thumbnail = "",
        subName = "매버릭",
        brandGender = ItemGender.MALE,
        maxDiscount = 46
    ),
    BrandListData(
        name = "adidas",
        id = 1,
        thumbnail = "",
        subName = "아디다스",
        brandGender = ItemGender.UNISEX,
        maxDiscount = 0
    )
)

val fakeCreatorDetailData = CreatorDetailData(
    id = 1,
    createDate = "2023-07-13T21:28:40",
    category = HomeCategory.GYMWEAR,
    userInfo = CreatorDetailData.UserInfo(
        uid = "123",
        nickname = "nick_nick",
        name = "John Doe",
        height = 180,
        weight = 75,
        gender = Gender.MALE,
        job = "Fitness Trainer",
        profileImageURL = "",
        instagramDeepLink = "https://www.instagram.com/johndoe/",
        instagramWebLink = "https://www.instagram.com/johndoe/",
        youtubeDeepLink = "https://www.youtube.com/c/johndoe/",
        youtubeWebLink = "https://www.youtube.com/c/johndoe/",
        favoriteExercise = "Running",
        experienceExercise = "2023-05-04"
    ),
    imageURLList = listOf(
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg"
    ),
    postItems = listOf(
        CreatorDetailData.PositionItem(
            id = 1,
            category = CreatorDetailData.PositionItem.Category.BELT,
            xPosition = 10.0,
            yPosition = 20.0,
            size = "M",
            item = CreatorDetailData.PositionItem.Item(
                id = 123,
                name = "Stylish Shirt",
                price = 49000,
                salePrice = 39000,
                brand = CreatorDetailData.PositionItem.Item.Brand(
                    id = 0L,
                    name = "가나다",
                    thumbnailURL = "",
                    subName = null,
                    isLaunched = true
                ),
                imageURLList = listOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg",
                    "https://example.com/image3.jpg"
                ),
                deleted = false
            ),
            hasReview = true
        ),
        CreatorDetailData.PositionItem(
            id = 2,
            category = CreatorDetailData.PositionItem.Category.CARDIGAN,
            xPosition = 0.14371980676328502,
            yPosition = 0.5301932367149759,
            size = "L",
            item = CreatorDetailData.PositionItem.Item(
                id = 123,
                name = "Stylish Shirt",
                price = 49000,
                salePrice = 39000,
                brand = CreatorDetailData.PositionItem.Item.Brand(
                    id = 0L,
                    name = "가나다",
                    thumbnailURL = "",
                    subName = null,
                    isLaunched = true
                ),
                imageURLList = listOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg",
                    "https://example.com/image3.jpg"
                ),
                deleted = false
            ),
            hasReview = false
        )
    ),
    likes = 100,
    comments = 20,
    views = 1000,
    exerciseExperience = 5,
    isLiked = true,
    bookmarked = false
)

val fakeOrderItemData = listOf(
    OrderItemData(
        id = "202310200001",
        itemName = "나이키 스포츠웨어 에센셜",
        itemImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000052.png",
        amountOfPayment = 38000,
        brandName = "NIKE",
        itemOptionDetailList = listOf(
            OrderItemData.ItemOptionDetail(id = 127, orderNumber = "202310200001", itemOption = "색상", itemOptionDetail = "블랙"),
            OrderItemData.ItemOptionDetail(id = 128, orderNumber = "202310200001", itemOption = "사이즈", itemOptionDetail = "M"),
            OrderItemData.ItemOptionDetail(id = 133, orderNumber = "202310200001", itemOption = "색상", itemOptionDetail = "레드"),
            OrderItemData.ItemOptionDetail(id = 134, orderNumber = "202310200001", itemOption = "사이즈", itemOptionDetail = "L")
        ),
        orderStatus = OrderItemData.OrderStatus.DELIVERY_COMPLETED,
        orderedDate = "2023.10.20"
    ),
    OrderItemData(
        id = "202310200002",
        itemName = "나이키 스포츠웨어 에센셜",
        itemImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000052.png",
        amountOfPayment = 38000,
        brandName = "NIKE",
        itemOptionDetailList = listOf(
            OrderItemData.ItemOptionDetail(id = 127, orderNumber = "202310200001", itemOption = "색상", itemOptionDetail = "블랙"),
            OrderItemData.ItemOptionDetail(id = 128, orderNumber = "202310200001", itemOption = "사이즈", itemOptionDetail = "M"),
            OrderItemData.ItemOptionDetail(id = 133, orderNumber = "202310200001", itemOption = "색상", itemOptionDetail = "레드"),
            OrderItemData.ItemOptionDetail(id = 134, orderNumber = "202310200001", itemOption = "사이즈", itemOptionDetail = "L")
        ),
        orderStatus = OrderItemData.OrderStatus.ORDER_CANCELED,
        orderedDate = "2023.10.20"
    )
)

val fakeReceiptData = ReceiptData(
    orderID = "202310270003",
    orderedDate = LocalDateTime.parse("2023-10-27T10:31:42.000+09:00", DateTimeFormatter.ISO_DATE_TIME),
    itemName = "나이키 스포츠웨어 에센셜",
    itemImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000052.png",
    orderStatus = ReceiptData.OrderStatus.ORDER_CANCELED,
    brandName = "NIKE",
    receiverName = "이재휘",
    roadAddress = "경기도 용인시 처인구 낙은로 11",
    roadAddressDetail = "103-404",
    itemOptionDetailList = listOf(
        ReceiptData.ItemOptionDetailList(137, "202310270003", "색상", "블랙"),
        ReceiptData.ItemOptionDetailList(138, "202310270003", "사이즈", "M")
    ),
    phoneNumber = "01024328575",
    depositDeadline = LocalDateTime.parse("2023-11-03T10:31:42.000+09:00", DateTimeFormatter.ISO_DATE_TIME),
    price = 35000,
    itemDiscount = 0,
    couponDiscount = 0,
    pointDiscount = 3500,
    deliveryPrice = 0,
    amountOfPayment = 3000
)

val fakeUserInfo = UserInfoData(
    createDate = "2022-07-28T16:06:15",
    modifiedDate = "2023-07-05T17:53:24",
    gender = Gender.MALE,
    uid = "9999999",
    phoneNumber = "01712341234",
    nickname = "ablebody_official",
    name = "애블바디",
    height = null,
    weight = null,
    job = null,
    profileUrl = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/user/9999999/17544 bytes_1672466587267.jpg",
    introduction = "더욱 더 편리한 운동생활을 위해 노력하고 있어요🦄",
    creatorPoint = 10400,
    userType = UserInfoData.UserType.USER
)