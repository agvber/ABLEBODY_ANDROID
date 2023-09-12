package com.example.ablebody_android.bookmark.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.AbleLight
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.utils.previewPlaceHolder
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun BookmarkProductItemLayout(
    requestWebPage: () -> Unit,
    bookmarkClick: () -> Unit,
    selected: Boolean,
    productName: String,
    productPrice: Int,
    productSalePrice: Int?,
    brandName: String,
    imageURL: String,
    isSingleImage: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box {
            AsyncImage(
                model = imageURL,
                contentDescription = "bookmark item",
                contentScale = ContentScale.FillHeight,
                placeholder = previewPlaceHolder(id = R.drawable.product_item_test),
                filterQuality = FilterQuality.Low,
                modifier = Modifier.aspectRatio(.9f)
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = brandName,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight(500),
                            color = AbleDark,
                        )
                    )
                    Icon(
                        painter = painterResource(id = if (selected) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty),
                        contentDescription = "bookmark",
                        tint = Color.Black,
                        modifier = Modifier.clickable { bookmarkClick() }
                    )
                }
                Text(
                    text = productName,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(productSalePrice ?: productPrice)}원",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                            textAlign = TextAlign.Center,
                        )
                    )
                    if (productSalePrice != null) {
                        Text(
                            text = productPrice.toString(),
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
                            text = "${((productPrice - productSalePrice).toDouble() / productPrice * 100).roundToInt()}%",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                                color = AbleBlue,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { requestWebPage() }
                ,
                shape = RoundedCornerShape(size = 5.dp),
                border = BorderStroke(width = 1.dp, color = AbleLight)
            ) {
                Text(
                    text = "구매링크 이동",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight(500),
                        color = AbleDark,
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarkProductItemLayoutPreview() {
    ABLEBODY_AndroidTheme {
        BookmarkProductItemLayout(
            requestWebPage = {  },
            bookmarkClick = {  },
            productName = "ONYOURON SWEATS(BLACk",
            productPrice = 39000,
            productSalePrice = 34000,
            brandName = "온유어오운",
            imageURL = "",
            isSingleImage = false,
            selected = false
        )
    }
}