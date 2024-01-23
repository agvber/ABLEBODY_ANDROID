package com.smilehunter.ablebody.presentation.payment.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class PaymentPassthroughDataPreviewParameterProvider : PreviewParameterProvider<PaymentPassthroughData> {
    override val values: Sequence<PaymentPassthroughData>
        get() = sequenceOf(
            PaymentPassthroughData(
                deliveryPrice = 3000,
                items = listOf(
                    PaymentPassthroughData.Item(
                        itemID = 52,
                        brandName = "나이키",
                        itemName = "나이키 스포츠웨어 에센셜",
                        price = 35000,
                        salePrice = 29000,
                        salePercentage = 17,
                        itemImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000052.png",
                        count = 1,
                        options = listOf(
                            PaymentPassthroughData.ItemOptions(
                                id = 1,
                                content = "M",
                                options = PaymentPassthroughData.ItemOptions.Option.SIZE
                            ),
                            PaymentPassthroughData.ItemOptions(
                                id = 2,
                                content = "black",
                                options = PaymentPassthroughData.ItemOptions.Option.COLOR
                            )
                        )
                    )
                )
            )
        )

}