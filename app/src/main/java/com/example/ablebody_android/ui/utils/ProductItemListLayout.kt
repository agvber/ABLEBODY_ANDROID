package com.example.ablebody_android.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.AbleLight
import com.example.ablebody_android.ui.theme.SmallTextGrey
import java.text.NumberFormat
import java.util.Locale

@Composable
private fun ProductItemListContent(
    productName: String,
    productPrice: Int,
    productSalePrice: Int?,
    brandName: String,
    averageStarRating: String,
    isSingleImage: Boolean = false
) {
    Column {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/item/ABLE-0000039.png")
                    .placeholder(R.drawable.product_item_test)
                    .build(),
                contentDescription = "product image"
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
                    text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(productSalePrice ?: productPrice),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    )
                )
                if (productSalePrice != null) {
                    Text(
                        text = "36,000",
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
                        text = "19%",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            color = AbleBlue,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
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

@Preview(showBackground = true)
@Composable
private fun ProductItemListContentPreview() {
    ABLEBODY_AndroidTheme {
        ProductItemListContent(
            productName = "데일리 크롭 슬리브리스 그레이 옐로우",
            productPrice = 36000,
            productSalePrice = 29000,
            brandName = "오옴",
            averageStarRating = "0.0(0)"
        )
    }
}

@Composable
fun ProductItemListLayout() {
    LazyVerticalGrid(columns = GridCells.Adaptive(195.dp)) {
        items(items = (0..30).toList()) {
            ProductItemListContent(
                productName = "데일리 크롭 슬리브리스 그레이 옐로우",
                productPrice = 36000,
                productSalePrice = 29000,
                brandName = "오옴",
                averageStarRating = "0.0(0)"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemListLayoutPreview() {
    ABLEBODY_AndroidTheme {
        ProductItemListLayout()
    }
}