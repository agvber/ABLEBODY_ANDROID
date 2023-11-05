package com.smilehunter.ablebody.presentation.item_detail.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.main.ui.LocalMainScaffoldPaddingValue
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.theme.White

@Composable
fun ItemDetailScreen(
    viewModel: ItemDetailViewModel = hiltViewModel(),
    id: Long
) {
    val itemDetailData by viewModel.itemDetailLiveData.observeAsState()
    Log.d("LOGid", id.toString())
    LaunchedEffect(key1 = true) {
        viewModel.getData(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_appbar_back_button),
                    contentDescription = "back button",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { }
                        )
                )
            },
            backgroundColor = White,
        )

        val mainImageList = itemDetailData?.data?.item?.images ?: listOf()
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        LazyRow {
            items(mainImageList) { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = imageUrl)
//                        .placeholder(R.drawable.detail_page_image_test) // 로딩 중에 표시될 이미지
                        .build(),
                    contentDescription = "Detailed image description",
//                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(1.dp)
                        .width(screenWidth)  // use the screen's width
                        .height(screenWidth)  // use the calculated height
                )
            }
        }

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
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = itemDetailData?.data?.item?.brand?.thumbnail)
                        .placeholder(R.drawable.nike_store_test) // 로딩 중에 표시될 이미지
                        .build(),
                    contentDescription = "Detailed image description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(1.dp)
                        .width(44.dp)
                        .height(44.dp)
                )
                Log.d("LOGdata", itemDetailData?.data.toString())
                Text(
                    text = itemDetailData?.data?.item?.brand?.name ?: "",
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
                text = itemDetailData?.data?.item?.name ?: "",
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
                val salePrice = itemDetailData?.data?.item?.salePrice
                val price = itemDetailData?.data?.item?.price

                val displayText =
                    salePrice?.let { "${it}원" } ?: price?.let { "${it}원" } ?: "Unknown"
                Text(
                    text = displayText,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .padding(end = 5.dp)
                )

                if (salePrice != null && price != null) {
                    val salePercentage = ((price - salePrice) / price.toDouble()) * 100
                    val roundedPercentage = salePercentage.toInt()
                    Log.d(
                        "LOGPRICE",
                        "price: ${price}, ${salePrice}, ${salePercentage}, ${roundedPercentage} "
                    )

                    Text(
                        text = "${price}원",
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
                        text = "$roundedPercentage%",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(500),
                            color = AbleBlue,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
        val avgStarRating = itemDetailData?.data?.item?.avgStarRating.toString()
        val regex = """\((\d+)\)""".toRegex()
        val matchResult = regex.find(avgStarRating)
        val numberInsideParentheses = matchResult?.groups?.get(1)?.value
        val defaultSize = 0
        val size = numberInsideParentheses?.toInt() ?: defaultSize

        Log.d("size", size.toString())
        if (size != 0) {
//            val creatorReviewImage = itemDetailData?.data?.itemReviews?.get(0)?.images?.get(0)
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
                        text = itemDetailData?.data?.item?.avgStarRating.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF8C959E),
                        ),
                        modifier = Modifier
                            .padding(start = 2.dp)
                    )
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.Top
                ) {
                    items(items = List(size) { Unit }) {
                        Box(
                            modifier = Modifier
                                .width(350.dp)
                                .height(155.dp)
                                .padding(start = 16.dp, end = 10.dp)
                                .background(
                                    color = Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(size = 15.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(13.dp)
                            ) {
                                val creatorReviewImage =
                                    itemDetailData?.data?.itemReviews?.get(0)?.images?.get(0)
                                Log.d("creatorReviewImage", creatorReviewImage.toString())
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(data = creatorReviewImage)
//                                        .placeholder(R.drawable.card_image_thumbnail_test) // 로딩 중에 표시될 이미지
                                        .build(),
                                    contentDescription = "Detailed image description",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                )
                                Column(
                                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                                ) {
                                    Row() {
                                        Text(
                                            text = itemDetailData?.data?.itemReviews?.getOrNull(0)?.creator?.height.toString() + "cm / ",
                                            fontSize = 13.sp,
                                            color = SmallTextGrey
                                        )
                                        Text(
                                            text = itemDetailData?.data?.itemReviews?.getOrNull(0)?.creator?.weight.toString() + " kg / ",
                                            fontSize = 13.sp,
                                            color = SmallTextGrey
                                        )
                                        Text(
                                            text = itemDetailData?.data?.itemReviews?.getOrNull(0)?.size.toString() + " 착용",
                                            fontSize = 13.sp,
                                            color = SmallTextGrey
                                        )
                                    }
                                    Text(
                                        text = itemDetailData?.data?.itemReviews?.getOrNull(0)?.review.toString(),
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.height(63.dp)

                                    )
                                    Text(
                                        text = itemDetailData?.data?.itemReviews?.getOrNull(0)?.creator?.nickname.toString(),
                                        color = SmallTextGrey,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 5.dp),
                                        textAlign = TextAlign.End
                                    )

                                }

                            }

                        }
                    }
                }
            }
        }
//        val homePost = itemDetailData?.data?.homePosts?.get(0)?.imageURL
//        val numOfHomePost = itemDetailData?.data?.homePosts?.size
//        Log.d("LOGhomePost", "${homePost}, ${numOfHomePost}")
//        Text(
//            text = "이 제품을 활용한 코디",
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight(500),
//                color = Color(0xFF505863),
//                textAlign = TextAlign.Right,
//            ),
//            modifier = Modifier.padding(start = 20.dp, top = 10.dp)
//        )


//        val homePosts1 = itemDetailData?.data?.homePosts?.get(0)?.imageURL
//        val homePosts2 = itemDetailData?.data?.homePosts?.get(1)?.imageURL

//        Log.d("LOGhomPosts", homePosts1.toString()+", "+homePosts2.toString())

        val numOfHomePost = itemDetailData?.data?.homePosts?.size
//        val homePosts = itemDetailData?.data?.homePosts.orEmpty()
        Log.d("LOGhomPosts", numOfHomePost.toString())

        val imageUrls = mutableListOf<String>()
        if (numOfHomePost != null && numOfHomePost != 0) {
            for (i in 0 until numOfHomePost) {
                itemDetailData?.data?.homePosts?.get(i)?.imageURL?.let { imageUrl ->
                    imageUrls.add(imageUrl) // imageUrl이 null이 아닌 경우에만 리스트에 추가합니다.
                    Log.d("imageUrls.size", imageUrls.toString())
                }
            }
            Log.d("LOGnumOfHomePost", numOfHomePost.toString())
            Text(
                text = "이 제품을 활용한 코디",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF505863),
                    textAlign = TextAlign.Right,
                ),
                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
            )
        }

        // numOfHomePost는 homePosts 리스트의 크기입니다.

//        val numOfHomePost = homePosts.size
//        Log.d("LOGnumOfHomePost", numOfHomePost.toString())

        // 그리드 뷰를 생성합니다. 한 행에 3개의 셀을 표시합니다.
//        if (numOfHomePost > 0) {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier.padding(top = 10.dp)
//                    .fillMaxSize()
//            ) {
////                items(homePosts) { homePost ->
////                    // 여기서 homePost는 데이터 리스트의 개별 항목입니다.
////                    Image(
////                        painter = rememberImagePainter(
////                            data = homePost.imageURL,
////                            builder = {
////                                transformations(CircleCropTransformation())
////                            }
////                        ),
////                        contentDescription = null, // 시각적 설명을 제공합니다.
////                        modifier = Modifier
////                            .size(120.dp) // 적절한 크기로 조정합니다.
////                            .padding(5.dp), // 이미지 간의 공간을 위한 패딩을 추가합니다.
////                        contentScale = ContentScale.Crop // 이미지가 올바르게 잘리거나 채워지도록 합니다.
////                    )
////                }
//            }
//        } else {
//            // numOfHomePost가 0인 경우, 즉 이미지가 없는 경우 처리할 내용을 여기에 추가합니다.
//        }
//    }

//        Text(text = "2",
//            fontSize = 80.sp
//        )
//        Text(text = "3",
//            fontSize = 80.sp
//        )
//        Text(text = "4",
//            fontSize = 80.sp
//        )
//        Text(text = "5",
//            fontSize = 80.sp
//        )

        Box(
            modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current)
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun ItemDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
//        ItemDetailScreen()
    }
}