package com.smilehunter.ablebody.presentation.payment.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class PaymentPassthroughDataPreviewParameterProvider : PreviewParameterProvider<PaymentPassthroughData> {
    override val values: Sequence<PaymentPassthroughData>
        get() = sequenceOf(
            PaymentPassthroughData(
                deliveryPrice = 3000,
                items = listOf(
                    PaymentPassthroughData.Item(
                        itemID = 0,
                        brandName = "brand",
                        itemName = "item",
                        price = 38000,
                        salePrice = 18000,
                        salePercentage = 30,
                        itemImageURL = "imageURL",
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