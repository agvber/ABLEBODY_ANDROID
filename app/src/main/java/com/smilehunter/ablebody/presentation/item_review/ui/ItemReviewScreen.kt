package com.smilehunter.ablebody.presentation.item_review.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.item_review.ItemReviewViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout

@ExperimentalMaterial3Api
@Composable
fun ItemReviewScreen(
    onBackRequest: () -> Unit,
    itemReviewViewModel: ItemReviewViewModel = hiltViewModel(),
) {
    val itemReview by itemReviewViewModel.itemReview.collectAsStateWithLifecycle()

    val mainImageList = itemReview?.images ?: listOf()

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "크리에이터 리뷰",
                modifier = Modifier.padding(horizontal = 80.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = itemReview?.creator?.profileUrl)
//                            .data(data = R.drawable.card_image_thumbnail_test)
                            .build(),
                        contentDescription = "Detailed image description",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(44.dp),  // use the calculated height
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
//                                text = "vamonos_es",
                                text = itemReview?.creator?.nickname.toString(),
                                style = TextStyle(
                                    fontWeight = FontWeight(500),
                                    fontSize = 14.sp
                                )
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_creator_badge),
                                contentDescription = "profile"
                            )
                        }
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            val description = makeUserDescription(
                                itemReview?.creator?.height?.toString(),
                                itemReview?.creator?.weight?.toString(),
                                itemReview?.creator?.job,
//                                "수정 필요"
                                itemReview?.creator?.modifiedDate,
                                // itemReview?.creator?.modifiedDate?.toString() // 날짜 형식을 원하는 형태로 변환해야 할 수 있습니다.
                            )

                            if (description.isNotBlank()) {
                                Text(
                                    text = description,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(400),
                                        color = SmallTextGrey,
                                    )
                                )
                            }
                        }
                    }
                }

                CreatorReViewPagerPage(mainImageList)
//                val sizeText = "S 착용"
//                val reviewText = "좋아요 좋아요 좋아요좋아요 좋아요 좋아요 좋아요\n 좋아요좋아요좋아요좋아요좋아요좋아요좋아요좋아요"

                val sizeText = itemReview?.size.toString() + " 착용"
                val reviewText = itemReview?.review.toString()
                // AnnotatedString 생성
                val text = buildAnnotatedString {
                    // 특정 스타일 적용
                    withStyle(
                        style = SpanStyle(
                            color = AbleBlue,
                            fontWeight = FontWeight(700),
                        )
                    ) {
                        append(sizeText)
                    }
                    append(" / ")
                    append(reviewText)
                }

                BasicText(
                    text = text,
                    modifier = Modifier.padding(20.dp)
                )

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatorReViewPagerPage(mainImageList: List<String>) {
    val pagerState = rememberPagerState {
        mainImageList.size
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = mainImageList[page])
                        .build(),
                    contentDescription = "Detailed image description",
                    modifier = Modifier
                        .fillMaxSize(), // makes the image as wide as the parent
//                        .aspectRatio(3f / 4f),
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}
fun makeUserDescription(height: String?, weight: String?, job: String?, date: String?): String {

    val parts = listOfNotNull(height?.plus("cm"), weight?.plus("kg"), job, date)
    return parts.joinToString(separator = " · ")
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    ItemReviewScreen()
}