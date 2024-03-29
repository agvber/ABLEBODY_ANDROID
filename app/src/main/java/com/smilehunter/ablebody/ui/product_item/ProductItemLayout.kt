package com.smilehunter.ablebody.ui.product_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductItemLayout(
    modifier: Modifier = Modifier,
    productName: String,
    productPrice: Int,
    productSalePrice: Int?,
    productSalePercent: Int?,
    brandName: String,
    thumbnail: Any?,
    averageStarRating: String?,
    isSingleImage: Boolean
) {
    Column(modifier = modifier) {
        Box {
            AsyncImage(
                model = thumbnail,
                contentDescription = "product image",
                placeholder = previewPlaceHolder(id = R.drawable.product_item_test),
                filterQuality = FilterQuality.Low,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1f)
            )
            if (!isSingleImage) {
                Image(
                    painter = painterResource(id = R.drawable.ic_product_item_squaremultiple),
                    contentDescription = "square multiple",
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = brandName,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight(500),
                    color = AbleDark,
                )
            )
            Text(
                text = productName,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight(400),
                    color = SmallTextGrey,
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${NumberFormat.getInstance(Locale.KOREA).format(productSalePrice ?: productPrice)}원",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    )
                )
                if (productSalePrice != null) {
                    Text(
                        text = NumberFormat.getInstance(Locale.KOREA).format(productPrice),
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight(400),
                            color = AbleLight,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.LineThrough,
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = "$productSalePercent%",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            color = AbleBlue,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
            if (averageStarRating != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_product_item_star),
                        contentDescription = "product start"
                    )
                    Text(
                        text = averageStarRating,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemListContentPreview() {
    ABLEBODY_AndroidTheme {
        ProductItemLayout(
            productName = "데일리 크롭 슬리브리스 그레이 옐로우",
            productPrice = 36000,
            productSalePrice = 29000,
            productSalePercent = 19,
            brandName = "오옴",
            averageStarRating = "0.0(0)",
            thumbnail = R.drawable.product_item_test,
            isSingleImage = false
        )
    }
}