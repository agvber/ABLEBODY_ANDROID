package com.smilehunter.ablebody.presentation.brand.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout

@Composable
fun ItemDetailScreen() {
    val imageList = listOf(
        R.drawable.card_image_thumbnail_test,
        R.drawable.detail_page_image_test,
        R.drawable.detail_page_image_test
    )
    Column{
        BackButtonTopBarLayout(onBackRequest = { /*TODO*/ })
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = R.drawable.detail_page_image_test)
                .placeholder(R.drawable.detail_page_image_test) // 로딩 중에 표시될 이미지
                .build(),
            contentDescription = "Detailed image description",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(1.dp)
                .width(390.dp)
                .height(390.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(color = Color(0xFFFFFFFF))
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)

        ) {
            Row(
                modifier = Modifier
                    .width(105.dp)
                    .height(44.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.nike_store_test)
                        .placeholder(R.drawable.nike_store_test) // 로딩 중에 표시될 이미지
                        .build(),
                    contentDescription = "Detailed image description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(1.dp)
                        .width(44.dp)
                        .height(44.dp)
                )
                Text(
                    text = "오옴",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF191E29),
                    ),
                    modifier = Modifier
                        .padding(start = 12.dp, end = 4.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.chevronforward),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .fillMaxHeight()
                )

            }
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_bookmark_empty),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Text(
                text = "[SIGNATURE] - mustard",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = AbleDark,
                ),
                modifier = Modifier
                    .width(358.dp)
                    .height(24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = "38,000원",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
                Text(
                    text = "41,000원",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textDecoration = TextDecoration.LineThrough,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
                Text(
                    text = "7%",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(500),
                        color = AbleBlue,
                        textAlign = TextAlign.Center,
                    )
                )

            }

        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .width(390.dp)
                .height(189.dp)
                .background(color = Color(0xFFFFFFFF))
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "크리에이터 리뷰",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF505863),
                        textAlign = TextAlign.Right,
                    ),
                    modifier = Modifier
                        .padding(start = 20.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_product_item_star),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
                Text(
                    text = "4.5",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF8C959E),
                    ),
                    modifier = Modifier
                        .padding(start = 2.dp)
                )
                Text(
                    text = "(3)",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF8C959E),
                    ),
                    modifier = Modifier
                        .padding(start = 2.dp)
                )
            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
//                verticalAlignment = Alignment.Top,
//                modifier = Modifier
//                    .width(325.dp)
//                    .height(133.dp)
//                    .padding(start = 16.dp, end = 10.dp)
//                    .background(color = Color(0xFFF3F4F6), shape = RoundedCornerShape(size = 15.dp))
//            ) {
//
//            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .width(325.dp)
                    .height(133.dp)
                    .padding(start = 16.dp, end = 10.dp)
                    .background(color = Color(0xFFF3F4F6), shape = RoundedCornerShape(size = 15.dp))
            ) {
//                items(imageList) { imageResId ->
//                    Image(
//                        painter = painterResource(id = imageResId),
//                        contentDescription = "Image description",
//                        modifier = Modifier.size(133.dp) // 이미지 크기를 조정하려면 이 값을 수정하세요.
//                    )
//                }
            }
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun ItemDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
        ItemDetailScreen()
    }
}
