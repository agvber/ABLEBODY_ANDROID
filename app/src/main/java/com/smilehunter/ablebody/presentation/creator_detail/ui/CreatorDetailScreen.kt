package com.smilehunter.ablebody.presentation.creator_detail.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.CreatorDetailData
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.fake.fakeCreatorDetailData
import com.smilehunter.ablebody.presentation.creator_detail.CreatorDetailViewModel
import com.smilehunter.ablebody.presentation.creator_detail.data.CreatorDetailUiState
import com.smilehunter.ablebody.presentation.main.ui.LocalMainScaffoldPaddingValue
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.theme.White
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.CalculateSportElapsedTime
import com.smilehunter.ablebody.utils.CalculateUserElapsedTime
import com.smilehunter.ablebody.utils.NonReplyIconButton
import com.smilehunter.ablebody.utils.nonReplyClickable
import retrofit2.HttpException
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun CreatorDetailRoute(
    onBackRequest: () -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    profileRequest: (String) -> Unit,
    commentButtonOnClick: (Long) -> Unit,
    likeCountButtonOnClick: (Long) -> Unit,
    productItemOnClick: (Long) -> Unit,
    creatorDetailViewModel: CreatorDetailViewModel = hiltViewModel()
) {
    val creatorDetailUiState by creatorDetailViewModel.creatorDetailData.collectAsStateWithLifecycle()

    CreatorDetailScreen(
        onBackRequest = onBackRequest,
        profileIconOnClick = profileRequest,
        likeButtonOnClick = { creatorDetailViewModel.toggleLike(it) },
        bookmarkButtonOnClick = creatorDetailViewModel::toggleBookmark,
        commentButtonOnClick = commentButtonOnClick,
        likeCountButtonOnClick = likeCountButtonOnClick,
        productItemOnClick = productItemOnClick,
        creatorDetailUiState = creatorDetailUiState
    )

    var isNetworkDisConnectedDialogShow by remember { mutableStateOf(false) }
    if (isNetworkDisConnectedDialogShow) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { creatorDetailViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }

    if (creatorDetailUiState is CreatorDetailUiState.LoadFail) {
        val throwable = (creatorDetailUiState as CreatorDetailUiState.LoadFail).t
        val httpException = throwable as? HttpException
        if (httpException?.code() == 404) {
            onErrorRequest(ErrorHandlerCode.NOT_FOUND_ERROR)
            return
        }
        if (httpException != null) {
            onErrorRequest(ErrorHandlerCode.INTERNAL_SERVER_ERROR)
            return
        }
        isNetworkDisConnectedDialogShow = true
    }

    if (creatorDetailUiState is CreatorDetailUiState.Success) {
        if (isNetworkDisConnectedDialogShow) {
            isNetworkDisConnectedDialogShow = false
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatorDetailScreen(
    onBackRequest: () -> Unit,
    profileIconOnClick: (String) -> Unit,
    likeButtonOnClick: (Long)  -> Unit,
    bookmarkButtonOnClick: (Long, Boolean) -> Unit,
    commentButtonOnClick: (Long) -> Unit,
    likeCountButtonOnClick: (Long) -> Unit,
    productItemOnClick: (Long) -> Unit,
    creatorDetailUiState: CreatorDetailUiState,
) {
    Scaffold(
        topBar = { BackButtonTopBarLayout(onBackRequest = onBackRequest) }
    ) { paddingValue ->
        if (creatorDetailUiState is CreatorDetailUiState.Success) {
            val creatorDetailData = creatorDetailUiState.data
            var isLiked by rememberSaveable { mutableStateOf(creatorDetailData.isLiked) }
            var isBookmarked by rememberSaveable { mutableStateOf(creatorDetailData.bookmarked) }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(paddingValue)
            ) {
                item {
                    CreatorInfo(
                        profileRequest = { profileIconOnClick(creatorDetailData.userInfo.uid) },
                        profileImageURL = creatorDetailData.userInfo.profileImageURL,
                        name = creatorDetailData.userInfo.name ?: "",
                        height = creatorDetailData.userInfo.height,
                        weight = creatorDetailData.userInfo.weight,
                        job = creatorDetailData.userInfo.job,
                        elapsedTime = creatorDetailData.elapsedTime
                    )
                    Box(modifier = Modifier.aspectRatio(3f / 4f)) {
                        var visibleTag by rememberSaveable { mutableStateOf(false) }
                        val pagerState = rememberPagerState(pageCount = { creatorDetailData.imageURLList.size })
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .nonReplyClickable { visibleTag = !visibleTag }
                        ) { pageIndex ->
                            Box {
                                CreatorDetailProductItemTagLayout(
                                    imageContent = {
                                        Box {
                                            AsyncImage(
                                                model = creatorDetailData.imageURLList[pageIndex],
                                                contentDescription = null,
                                                placeholder = previewPlaceHolder(id = R.drawable.cody_item_test),
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                            )
                                        }
                                    },
                                    productTageContent = {
                                        if (pageIndex == 0) {
                                            val configuration = LocalConfiguration.current
                                            val screenWidthPx = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

                                            creatorDetailData.postItems.forEach {
                                                val positionX = (screenWidthPx * it.xPosition).roundToInt()

                                                AnimatedVisibility(
                                                    visible = !visibleTag,
                                                    enter = fadeIn(tween(700)),
                                                    exit = fadeOut(tween(700)),
                                                    modifier = Modifier
                                                        .alpha(.85f)
                                                ) {
                                                    ProductItemTag(
                                                        triangleLeft = positionX > (screenWidthPx / 2),
                                                        productName = it.item.brand.name,
                                                        productPrice = it.item.price,
                                                        productSize = it.size,
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    offsetList = creatorDetailData.postItems.map { Offset(it.xPosition.toFloat(), it.yPosition.toFloat()) }
                                )
                                if (pageIndex == 0) {
                                    AnimatedVisibility(
                                        visible = visibleTag,
                                        enter = fadeIn(tween(700)),
                                        exit = fadeOut(tween(700)),
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_product_tag_white),
                                            contentDescription = "tag icon",
                                            tint = White,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .size(26.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (pagerState.pageCount > 1) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(10.dp)
                            ) {
                                for (index in 0 until pagerState.pageCount) {
                                    val color by animateColorAsState(
                                        targetValue = if (pagerState.currentPage == index) AbleBlue else AbleLight
                                    )
                                    Canvas(modifier = Modifier.size(5.dp)) {
                                        drawCircle(color = color)
                                    }
                                }
                            }
                        }
                    }

                    CreatorActionLayout(
                        likeButtonOnClick = { isLiked = !isLiked },
                        goToCommentPage = { commentButtonOnClick(creatorDetailData.id) },
                        goToLikeUsersPage = { likeCountButtonOnClick(creatorDetailData.id) },
                        bookmarkButtonOnClick = { isBookmarked = !isBookmarked },
                        likeCount = creatorDetailData.likes,
                        commentCount = creatorDetailData.comments,
                        isLiked = isLiked,
                        isBookmarked = isBookmarked
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        CreatorDescriptionLayout(
                            experienceExercise = creatorDetailData.userInfo.experienceExerciseElapsedTime?.let { convertSportElapsedTimeToString(it) } ?: "",
                            favoriteExercise = creatorDetailData.userInfo.favoriteExercise ?: ""
                        )
                        val uriHandler = LocalUriHandler.current
                        if (!creatorDetailData.userInfo.instagramWebLink.isNullOrBlank()) {
                            SNSShortcutButton(
                                onClick = { uriHandler.openUri(creatorDetailData.userInfo.instagramWebLink) },
                                text = "${creatorDetailData.userInfo.name} 님의 인스타그램 바로 가기",
                                textColor = Color(0xFF661FF5),
                                backgroundColor = Color(0xFFF0E9FE),
                                modifier = Modifier
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_instagram_logo),
                                    contentDescription = "instagram icon",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        if (!creatorDetailData.userInfo.youtubeWebLink.isNullOrBlank()) {
                            SNSShortcutButton(
                                onClick = { uriHandler.openUri(creatorDetailData.userInfo.youtubeWebLink) },
                                text = "${creatorDetailData.userInfo.name} 님의 유튜브 채널 바로 가기",
                                textColor = Color(0xFFEA3323),
                                backgroundColor = Color(0xFFF0E9FE),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_youtube_log),
                                    contentDescription = "youtube icon",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Text(
                            text = "크리에이터가 사용하는 제품들",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                fontWeight = FontWeight(500),
                                color = AbleDeep,
                                textAlign = TextAlign.Right,
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            ),
                        )
                    }
                }
                itemsIndexed(items = creatorDetailData.postItems) { index, item ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        if (creatorDetailData.postItems.getOrNull(index - 1)?.category != item.category) {
                            Text(
                                text = convertItemCategoryToString(item.category),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                                    fontWeight = FontWeight(700),
                                    color = AbleDeep,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                        CreatorProductItem(
                            title = item.item.brand.name,
                            content = item.item.name,
                            imageURL = item.item.imageURLList[0],
                            itemSize = item.size,
                            price = item.item.price,
                            salePrice = item.item.salePrice,
                            salePercentage = item.item.salePercentage,
                            isReview = item.hasReview,
                            modifier = Modifier
                                .clip(RoundedCornerShape(size = 15.dp))
                                .clickable { productItemOnClick(item.id) },
                        )
                    }
                }
                item {
                    Box(modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current))
                }
            }
            val lifecycleOwner by rememberUpdatedState(newValue = LocalLifecycleOwner.current)
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { owner, event ->
                    if (event == Lifecycle.Event.ON_STOP) {
                        if (creatorDetailData.isLiked != isLiked) {
                            likeButtonOnClick(creatorDetailData.id)
                        }
                        if (creatorDetailData.bookmarked != isBookmarked) {
                            bookmarkButtonOnClick(creatorDetailData.id, creatorDetailData.bookmarked)
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        }
    }
}

private fun convertSportElapsedTimeToString(elapsedTime: CalculateSportElapsedTime): String =
    when (elapsedTime) {
        is CalculateSportElapsedTime.Year -> "${elapsedTime.year}년 운동 중"
        is CalculateSportElapsedTime.Month -> "${elapsedTime.month}개월 운동 중"
        is CalculateSportElapsedTime.Week -> "${elapsedTime.week}주 운동 중"
        is CalculateSportElapsedTime.Day ->"${elapsedTime.day}일 운동 중"
        is CalculateSportElapsedTime.Hour -> "${elapsedTime.hour}시간 운동 중"
        is CalculateSportElapsedTime.Minutes -> "${elapsedTime.minutes}분 운동 중"
        is CalculateSportElapsedTime.Second -> "${elapsedTime.second}초 운동 중"
    }

private fun convertItemCategoryToString(category: CreatorDetailData.PositionItem.Category): String =
    when (category) {
        CreatorDetailData.PositionItem.Category.SHORT_SLEEVE -> "숏슬리브"
        CreatorDetailData.PositionItem.Category.LONG_SLEEVE -> "롱슬리브"
        CreatorDetailData.PositionItem.Category.SLEEVELESS -> "슬리브리스"
        CreatorDetailData.PositionItem.Category.SWEAT_HOODIE -> "스웻&후디"
        CreatorDetailData.PositionItem.Category.PADTOP -> "패드탑"
        CreatorDetailData.PositionItem.Category.SHORTS -> "쇼츠"
        CreatorDetailData.PositionItem.Category.PANTS -> "팬츠"
        CreatorDetailData.PositionItem.Category.LEGGINGS -> "레깅스"
        CreatorDetailData.PositionItem.Category.ZIP_UP -> "집업"
        CreatorDetailData.PositionItem.Category.WINDBREAK -> "바람막이"
        CreatorDetailData.PositionItem.Category.CARDIGAN -> "가디건"
        CreatorDetailData.PositionItem.Category.HEADWEAR -> "모자"
        CreatorDetailData.PositionItem.Category.GUARD -> "보호대"
        CreatorDetailData.PositionItem.Category.STRAP -> "스트랩"
        CreatorDetailData.PositionItem.Category.BELT -> "벨트"
        CreatorDetailData.PositionItem.Category.SHOES -> "신발"
        CreatorDetailData.PositionItem.Category.ETC -> "기타"
    }

@Composable
fun CreatorInfo(
    profileRequest: () -> Unit,
    profileImageURL: String?,
    name: String,
    height: Int?,
    weight: Int?,
    job: String?,
    elapsedTime: CalculateUserElapsedTime
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .requiredHeight(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 5.dp)
    ) {
        AsyncImage(
            model = profileImageURL,
            contentDescription = null,
            placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(44.dp)
                .nonReplyClickable(profileRequest)
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_creator_badge),
                    contentDescription = "creator badge"
                )
            }
            Row {
                Text(
                    text = makeUserDescription(
                        height?.let { "${it}cm" },
                        weight?.let { "${it}kg" },
                        job,
                        convertElapsedTimeToString(elapsedTime)
                    ),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
        }
    }
}

private fun convertElapsedTimeToString(elapsedTime: CalculateUserElapsedTime): String =
    when (elapsedTime) {
        is CalculateUserElapsedTime.Date -> "${elapsedTime.month}월 ${elapsedTime.day}일"
        is CalculateUserElapsedTime.Hour -> "${elapsedTime.hour}시간 전"
        is CalculateUserElapsedTime.Minutes -> "${elapsedTime.minutes}분 전"
        is CalculateUserElapsedTime.Recent -> "방금 전"
    }

private fun makeUserDescription(vararg values: String?): String =
    values.filterNotNull().joinToString(separator = " · ")

@Composable
fun CreatorActionLayout(
    likeButtonOnClick: () -> Unit,
    goToCommentPage: () -> Unit,
    goToLikeUsersPage: () -> Unit,
    bookmarkButtonOnClick: () -> Unit,
    likeCount: Int,
    commentCount: Int,
    isLiked: Boolean,
    isBookmarked: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val likeIconColor by animateColorAsState(
                targetValue = if (isLiked) AbleRed else AbleLight
            )
            NonReplyIconButton(onClick = likeButtonOnClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = "like icon",
                    tint = likeIconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            NonReplyIconButton(onClick = goToCommentPage) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chat),
                    contentDescription = "comment icon",
                    tint = AbleLight,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NonReplyIconButton(onClick = goToLikeUsersPage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = "like icon",
                        tint = AbleLight,
                        modifier = Modifier.size(19.dp)
                    )
                    Text(
                        text = "$likeCount",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                        )
                    )
                }
            }
            NonReplyIconButton(onClick = goToCommentPage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chat),
                        contentDescription = "comment icon",
                        tint = AbleLight,
                        modifier = Modifier.size(21.dp)
                    )
                    Text(
                        text = "$commentCount",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .nonReplyClickable { bookmarkButtonOnClick() }
            ) {
                val bookmarkIconColor by animateColorAsState(
                    targetValue = if (isBookmarked) AbleDark else AbleLight
                )
                Icon(
                    painter = painterResource(id = if (isBookmarked) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty),
                    contentDescription = "bookmark icon",
                    tint = bookmarkIconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun CreatorDescriptionLayout(
    experienceExercise: String,
    favoriteExercise: String
) {
    ConstraintLayout(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        val (exerciseExperienceTitle, exerciseExperienceDescription, exerciseFavoriteTitle, exerciseFavoriteDescription) = createRefs()
        Text(
            text = "운동 경력",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = AbleLight,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.constrainAs(exerciseExperienceTitle) {
                start.linkTo(parent.start)
                top.linkTo(
                    anchor = exerciseExperienceDescription.top,
                    margin = .5.dp
                )
                bottom.linkTo(exerciseExperienceDescription.bottom)
            }
        )
        Text(
            text = experienceExercise,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = AbleDeep,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.constrainAs(exerciseExperienceDescription) {
                start.linkTo(exerciseFavoriteDescription.start)
                top.linkTo(parent.top)
                bottom.linkTo(exerciseFavoriteDescription.top)
            }
        )
        Text(
            text = "좋아하는 운동",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = AbleLight,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.constrainAs(exerciseFavoriteTitle) {
                top.linkTo(exerciseFavoriteDescription.top)
                bottom.linkTo(exerciseFavoriteDescription.bottom)
            }
        )
        Text(
            text = favoriteExercise,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = AbleDeep,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .constrainAs(exerciseFavoriteDescription) {
                    start.linkTo(
                        anchor = exerciseFavoriteTitle.end,
                        margin = 16.dp
                    )
                    top.linkTo(
                        anchor = exerciseExperienceDescription.bottom,
                        margin = 8.dp
                    )
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun SNSShortcutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(size = 15.dp),
    backgroundColor: Color,
    text: String,
    textColor: Color,
    content: @Composable() (RowScope.() -> Unit),
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = shape)
            .clip(shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        content()
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = textColor,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
private fun CreatorProductItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    imageURL: String,
    itemSize: String,
    price: Int,
    salePrice: Int?,
    salePercentage: Int?,
    isReview: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(color = PlaneGrey, shape = RoundedCornerShape(size = 15.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        AsyncImage(
            model = imageURL,
            contentDescription = null,
            placeholder = previewPlaceHolder(id = R.drawable.product_item_test),
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(5.dp))
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            Text(
                text = content,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    fontWeight = FontWeight(400),
                    color = SmallTextGrey,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$itemSize 착용 · ${NumberFormat.getInstance(Locale.KOREA).format(salePrice ?: price)}원",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                if (salePrice != null) {
                    Text(
                        text = NumberFormat.getInstance(Locale.KOREA).format(price),
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = AbleLight,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.LineThrough,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    Text(
                        text = "$salePercentage%",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                            fontWeight = FontWeight(500),
                            color = AbleBlue,
                            textAlign = TextAlign.Center,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
            if (isReview) {
                Text(
                    text = "이 크리에이터의 리뷰 있음",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(500),
                        color = White,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier
                        .background(color = AbleBlue, shape = RoundedCornerShape(7.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CreatorDetailProductItemTagLayout(
    imageContent: @Composable () -> Unit,
    productTageContent: @Composable () -> Unit,
    offsetList: List<Offset>
) {
    Layout(
        contents = listOf(imageContent, productTageContent),
        measurePolicy = { (imageContentMeasurableList, productTageContentMeasurableList), constraint ->
            val imagePlaceable = imageContentMeasurableList.first().measure(constraint)
            val productItemTagPlaceable = productTageContentMeasurableList.map { it.measure(constraint) }

            layout(imagePlaceable.width, imagePlaceable.height) {
                imagePlaceable.place(0, 0)

                productItemTagPlaceable.mapIndexed { index, placeable ->
                    placeable.place(
                        x = (offsetList[index].x * imagePlaceable.width.toDp() - (placeable.width.toDp() / 2)).toPx().roundToInt(),
                        y = (offsetList[index].y * imagePlaceable.width.toDp() - (placeable.height.toDp() / 2) ).toPx().roundToInt()
                    )
                }
            }
        }
    )
}

@Composable
fun ProductItemTag(
    modifier: Modifier = Modifier,
    triangleLeft: Boolean,
    productName: String,
    productPrice: Int,
    productSize: String,
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .drawBehind {
                drawRoundRect(
                    color = White,
                    cornerRadius = CornerRadius(5.dp.toPx()),
                )

                if (triangleLeft) {
                    drawPath(
                        path = Path().apply {
                            moveTo(0f, size.height / 2f - 10.dp.toPx())
                            lineTo(0f, size.height / 2f + 10.dp.toPx())
                            lineTo(-10.dp.toPx(), size.height / 2f)
                            close()
                        },
                        color = White
                    )
                } else {
                    drawPath(
                        path = Path().apply {
                            moveTo(size.width, size.height / 2f - 10.dp.toPx())
                            lineTo(size.width, size.height / 2f + 10.dp.toPx())
                            lineTo(size.width + 10.dp.toPx(), size.height / 2f)
                            close()
                        },
                        color = White
                    )
                }
            }
            .padding(
                top = 4.dp, bottom = 4.dp, start = 8.dp, end = 20.dp
            )
    ) {
        Text(
            text = productName,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            text = "${NumberFormat.getInstance(Locale.KOREA).format(productPrice)}원",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            text = productSize,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = AbleBlue,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreatorInfoPreview() {
    CreatorInfo(
        profileRequest = {},
        profileImageURL = "",
        name = "ablebody_creator",
        height = 181,
        weight = 76,
        job = null,
        elapsedTime = CalculateUserElapsedTime.Minutes(46)
    )
}

@Preview(showBackground = true)
@Composable
private fun CreatorActionLayoutPreview() {
    CreatorActionLayout(
        likeButtonOnClick = {  },
        goToCommentPage = {  },
        goToLikeUsersPage = {  },
        bookmarkButtonOnClick = {  },
        likeCount = 3,
        commentCount = 10,
        isLiked = true,
        isBookmarked = true
    )
}

@Preview(showBackground = true)
@Composable
private fun CreatorDescriptionLayoutPreview() {
    CreatorDescriptionLayout(
        experienceExercise = "2.5년 운동 중",
        favoriteExercise = "웨이트 트레이닝, 축구"
    )
}

@Preview(showBackground = true)
@Composable
private fun SNSShortcutButtonPreview() {
    SNSShortcutButton(
        onClick = {},
        text = "홍길동 님의 유튜브 채널 바로 가기",
        textColor = Color(0xFFEA3323),
        backgroundColor = Color(0xFFFDEBE9)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_youtube_log),
            contentDescription = "youtube icon",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
fun ProductItemTagPreview() {
    ProductItemTag(
        triangleLeft = false,
        productName = "MAVRK",
        productPrice = 39000,
        productSize = "XL",
    )
}

@Preview(showBackground = true)
@Composable
fun CreatorProductItemPreview() {
    CreatorProductItem(
        title = "나이키 JORDAN",
        content = "AIR JORDAN 어센틱AIR JORDAN 어센틱AIR JORDAN 어센틱",
        imageURL = "",
        itemSize = "XL",
        price = 60000,
        salePrice = 30000,
        salePercentage = 50,
        isReview = true
    )
}

@Preview(
    heightDp = 1500,
    showBackground = true
)
@Composable
fun CreatorDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
        CreatorDetailScreen(
            onBackRequest = {},
            profileIconOnClick = {},
            likeButtonOnClick = {},
            bookmarkButtonOnClick = { _, _ ->},
            commentButtonOnClick = {},
            likeCountButtonOnClick = {},
            productItemOnClick = {},
            creatorDetailUiState = CreatorDetailUiState.Success(fakeCreatorDetailData)
        )
    }
}