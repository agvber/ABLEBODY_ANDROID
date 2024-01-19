package com.smilehunter.ablebody.presentation.item_detail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.ItemDetailData
import com.smilehunter.ablebody.model.fake.fakeItemDetailData
import com.smilehunter.ablebody.presentation.item_detail.ItemDetailViewModel
import com.smilehunter.ablebody.presentation.item_detail.data.ItemDetailUiState
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.CustomButton
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.ui.utils.height
import com.smilehunter.ablebody.ui.utils.ignoreParentPadding
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.nonReplyClickable
import com.smilehunter.ablebody.utils.preloadImageList
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private enum class ItemOption {
    SIZE, COLOR
}

@Composable
fun ItemDetailRoute(
    onBackRequest: () -> Unit,
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    purchaseOnClick: (PaymentPassthroughData) -> Unit,
    itemClick: (ItemDetailData.ItemReview) -> Unit,
    brandOnClick: (Long, String) -> Unit,
    codyOnClick: (Long) -> Unit,
    itemDetailViewModel: ItemDetailViewModel = hiltViewModel()
) {
    val itemDetailData by itemDetailViewModel.itemDetail.collectAsStateWithLifecycle()

    ItemDetailScreen(
        onBackRequest = onBackRequest,
        onBookmarkRequest = itemDetailViewModel::toggleBookmarkItem,
        purchaseOnClick = purchaseOnClick,
        itemClick = itemClick,
        brandOnClick = brandOnClick,
        codyOnClick = codyOnClick,
        itemDetailUiState = itemDetailData
    )

    SimpleErrorHandler(
        refreshRequest = { itemDetailViewModel.refreshNetwork() },
        onErrorOccur = onErrorOccur,
        isError = itemDetailData is ItemDetailUiState.Error,
        throwable = (itemDetailData as? ItemDetailUiState.Error)?.t
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    onBackRequest: () -> Unit,
    onBookmarkRequest: (Boolean) -> Unit,
    purchaseOnClick: (PaymentPassthroughData) -> Unit,
    itemClick: (ItemDetailData.ItemReview) -> Unit,
    brandOnClick: (Long, String) -> Unit,
    codyOnClick: (Long) -> Unit,
    itemDetailUiState: ItemDetailUiState
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = { BackButtonTopBarLayout(onBackRequest = onBackRequest) }
    ) { paddingValues ->

        if (itemDetailUiState !is ItemDetailUiState.Success) {
            return@Scaffold
        }

        val itemDetailData = itemDetailUiState.data
        var isBookmarked by rememberSaveable { mutableStateOf(itemDetailData.bookmarked) }

        var isItemPaymentBottomSheetShow by rememberSaveable { mutableStateOf(false) }
        var isItemOptionSelectBottomSheetShow by rememberSaveable { mutableStateOf(false) }
        var itemOption: ItemOption? by rememberSaveable { mutableStateOf(null) }
        var options by rememberSaveable { mutableStateOf(listOf<String>()) }
        val selectOptionMap = remember { mutableStateMapOf<ItemOption, String>() }

        if (isItemPaymentBottomSheetShow) {
            ItemPaymentBottomSheet(
                onDismissRequest = { isItemPaymentBottomSheetShow = false },
                onItemPurchaseRequest = {
                    purchaseOnClick(
                        calculatorPaymentPassthroughData(itemDetailData, selectOptionMap)
                    )
                },
                onItemOptionRequest = {
                    when (it) {
                        ItemOption.SIZE -> {
                            options = itemDetailData.sizeList!!
                        }
                        ItemOption.COLOR -> {
                            options = itemDetailData.colorList!!
                        }
                    }
                    itemOption = it
                    isItemOptionSelectBottomSheetShow = true
                },
                requireItemOption = setOfNotNull(
                    itemDetailData.colorList?.let { ItemOption.COLOR },
                    itemDetailData.sizeList?.let { ItemOption.SIZE }
                ),
                selectOptionMap = selectOptionMap,
            )
        }

        if (isItemOptionSelectBottomSheetShow) {
            ItemOptionListBottomSheet(
                onDismissRequest = { option ->
                    if (option != null) {
                        selectOptionMap[itemOption!!] = option
                    }
                    isItemOptionSelectBottomSheetShow = false
                                   },
                options = options
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val lazyGridState = rememberLazyGridState()
            val lazyVerticalGridContentHorizontalPadding = with(density) { 16.dp.roundToPx() }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                state = lazyGridState,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {
                    Column(
                        modifier = Modifier.ignoreParentPadding(lazyVerticalGridContentHorizontalPadding)
                    ) {
                        val itemPagerState = rememberPagerState { itemDetailData.item.images.size }

                        ItemDetailImageView(
                            pagerState = itemPagerState,
                            imageUrlList = itemDetailData.item.images
                        )
                        val brand = itemDetailData.item.brand
                        ItemDetailTitle(
                            brandPageRequest = { brandOnClick(brand.id, brand.name) },
                            bookmarkClick = { isBookmarked = !isBookmarked },
                            brandName = brand.name,
                            brandImageURL = brand.thumbnail,
                            isBookmarked = isBookmarked
                        )

                        ItemDetailProfile(
                            itemName = itemDetailData.item.name,
                            price = itemDetailData.item.price,
                            salePrice = itemDetailData.item.salePrice,
                            salePercentage = itemDetailData.item.salePercentage
                        )
                    }
                }

                if (itemDetailData.detailImageUrls.isNotEmpty()) {
                    item(
                        span = { GridItemSpan(this.maxLineSpan) }
                    ) {
                        ItemDetailImageList(
                            density = density,
                            state = lazyGridState,
                            imageUrlList = itemDetailData.detailImageUrls
                        )
                    }
                }

                if (itemDetailData.itemReviews.isNotEmpty()) {
                    item(span = { GridItemSpan(this.maxLineSpan) }) {
                        val creatorReviewPagerState = rememberPagerState { itemDetailData.itemReviews.size }

                        Column(
                            modifier = Modifier
                                .ignoreParentPadding(lazyVerticalGridContentHorizontalPadding)
                                .padding(vertical = 12.dp)
                        ) {
                            CreatorReviewTitle(averageStar = itemDetailData.item.avgStarRating ?: "")
                            HorizontalPager(
                                state = creatorReviewPagerState,
                                contentPadding = PaddingValues(end = 54.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp)
                                    .animateContentSize()
                            ) { pageIndex ->
                                val itemReview = itemDetailData.itemReviews[pageIndex]
                                CreatorReviewContent(
                                    writerName = itemReview.creator.nickname,
                                    writerHeight = itemReview.creator.height,
                                    writerWeight = itemReview.creator.weight,
                                    itemSize = itemReview.size,
                                    description = itemReview.review,
                                    imageUrl = itemReview.images[0],
                                    starRating = itemReview.starRating,
                                    modifier = Modifier.nonReplyClickable {
                                        itemClick(itemReview)
                                    }
                                )
                            }
                        }
                    }
                }

                if (itemDetailData.homePosts.isNotEmpty()) {
                    item(
                        span = { GridItemSpan(this.maxLineSpan) }
                    ) {
                        Text(
                            text = "이 제품을 착용한 크리에이터",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                color = AbleDeep,
                            ),
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    }

                    items(
                        items = itemDetailData.homePosts,
                        key = { item: ItemDetailData.HomePost -> item.id }
                    ) { homePosts ->
                        AsyncImage(
                            model = homePosts.imageURL,
                            contentDescription = null,
                            placeholder = previewPlaceHolder(id = R.drawable.cody_item_test),
                            modifier = Modifier
                                .fillMaxSize()
                                .animateContentSize()
                                .nonReplyClickable {
                                    codyOnClick(homePosts.id)
                                }
                        )
                    }
                }

                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    var isPaymentNoticeExpand by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var isSellerInformationTabExpand by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var isBusinessInformationTabExpand by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var isCustomerSupportTabExpand by rememberSaveable {
                        mutableStateOf(false)
                    }
                    BusinessInformation(
                        paymentNoticeTabClick = { isPaymentNoticeExpand = !isPaymentNoticeExpand },
                        sellerInformationTabClick = { isSellerInformationTabExpand = !isSellerInformationTabExpand },
                        businessInformationTabClick = { isBusinessInformationTabExpand = !isBusinessInformationTabExpand },
                        customerSupportTabClick = { isCustomerSupportTabExpand = !isCustomerSupportTabExpand },
                        isPaymentNoticeExpand = isPaymentNoticeExpand,
                        isSellerInformationTabExpand = isSellerInformationTabExpand,
                        isBusinessInformationTabExpand = isBusinessInformationTabExpand,
                        isCustomerSupportTabExpand = isCustomerSupportTabExpand,
                        businessName = itemDetailData.seller.businessName ?: "-",
                        brandName = itemDetailData.seller.brand ?: "-",
                        businessNumber = itemDetailData.seller.businessNumber ?: "-",
                        reportNumber = itemDetailData.seller.reportNumber ?: "-",
                        contacts = itemDetailData.seller.contactNumber ?: "-",
                        email = itemDetailData.seller.emailAddress ?: "-",
                        roadAddress = itemDetailData.seller.roadAddress ?: "-"
                    )
                }

                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    Box(modifier = Modifier.height(100.dp))
                }
            }

            CustomButton(
                text = if (itemDetailData.item.brand.isLaunched) {
                    "구매하기"
                } else {
                    "${itemDetailData.item.redirectText} 구매 링크 이동"
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                if (itemDetailData.item.brand.isLaunched) {
                    isItemPaymentBottomSheetShow = true
                } else {
                    uriHandler.openUri(itemDetailData.item.redirectUrl)
                }
            }
        }

        LaunchedEffect(key1 = itemDetailData.item.images) {
            preloadImageList(context, itemDetailData.item.images)
            preloadImageList(context, itemDetailData.detailImageUrls)
        }

        val lifecycleOwner by rememberUpdatedState(newValue = LocalLifecycleOwner.current)
        DisposableEffect(key1 = lifecycleOwner.lifecycle) {
            val observer = LifecycleEventObserver { owner, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    if (itemDetailData.bookmarked != isBookmarked) {
                        onBookmarkRequest(isBookmarked)
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

private fun calculatorPaymentPassthroughData(
    itemDetailData: ItemDetailData,
    selectOptionMap: Map<ItemOption, String>
) = PaymentPassthroughData(
    deliveryPrice = itemDetailData.item.deliveryFee?.toInt() ?: 3000,
    items = listOf(
        PaymentPassthroughData.Item(
            itemID = itemDetailData.id,
            brandName = itemDetailData.item.brand.name,
            itemName = itemDetailData.item.name,
            price = itemDetailData.item.price,
            salePrice = itemDetailData.item.salePrice,
            salePercentage = itemDetailData.item.salePercentage,
            itemImageURL = itemDetailData.item.images[0],
            count = 1,
            options = selectOptionMap.map { (key, value) ->
                PaymentPassthroughData.ItemOptions(
                    id = 0,
                    content = value,
                    options = when(key) {
                        ItemOption.SIZE -> PaymentPassthroughData.ItemOptions.Option.SIZE
                        ItemOption.COLOR -> PaymentPassthroughData.ItemOptions.Option.COLOR
                    }
                )
            }
        )
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemDetailImageView(
    pagerState: PagerState,
    imageUrlList: List<String>
) {
    Box(
        modifier = Modifier.animateContentSize()
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            AsyncImage(
                model = imageUrlList[page],
                contentDescription = "Detailed image description",
                contentScale = ContentScale.FillWidth,
                placeholder = previewPlaceHolder(id = R.drawable.product_item_test),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (pagerState.pageCount > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
            ) {
                repeat(pagerState.pageCount) { index ->
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
}


@Composable
fun ItemDetailTitle(
    brandPageRequest: () -> Unit,
    bookmarkClick: () -> Unit,
    brandName: String,
    brandImageURL: String,
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .nonReplyClickable(brandPageRequest)
        ) {
            AsyncImage(
                model = brandImageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = previewPlaceHolder(id = R.drawable.brand_log_test),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(44.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(
                    text = brandName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700),
                        fontStyle = FontStyle(R.font.noto_sans_cjk_kr_bold),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier
                )

                Icon(
                    painter = painterResource(id = R.drawable.chevronforward),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 4.5.dp)
                )
            }
        }

        Image(
            painter = painterResource(
                id = if (isBookmarked) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty
            ),
            contentDescription = null,
            modifier = Modifier
                .nonReplyClickable(bookmarkClick),
        )
    }
}

@Composable
fun ItemDetailProfile(
    itemName: String,
    price: Int,
    salePrice: Int?,
    salePercentage: Int?
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    val displayPrice = salePrice?.let { "${numberFormat.format(it)}원" }
        ?: "${numberFormat.format(price)}원"

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Text(
            text = itemName,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                color = AbleDark,
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayPrice,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(700),
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = AbleDark,
                ),
                modifier = Modifier
                    .padding(end = 5.dp)
            )

            if (salePrice != null) {
                Text(
                    text = "${numberFormat.format(price)}원",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        color = SmallTextGrey,
                        textDecoration = TextDecoration.LineThrough,
                    ),
                    modifier = Modifier
                        .padding(end = 5.dp)
                )

                Text(
                    text = "$salePercentage%",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight(500),
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        color = AbleBlue
                    )
                )
            }
        }
    }
}

@Composable
fun ItemDetailImageList(
    density: Density,
    state: LazyGridState,
    imageUrlList: List<String>
) {
    val scope = rememberCoroutineScope()
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var collapseOffset by rememberSaveable { mutableIntStateOf(0) }
    var height by remember { mutableStateOf(0.dp) }
    Column {
        Box(
            modifier = Modifier.onGloballyPositioned {
                with(density) {
                    height = it.size.height.toDp()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height {
                        if (!isExpanded) {
                            return@height 1350.dp
                        }
                        null
                    }
            ) {
                imageUrlList.forEach { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    )
                }
            }
            if (height > 1350.dp && !isExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White)
                            )
                        )
                )
            }
        }
        if (height > 1350.dp) {
            ImageControlBar(
                onClick = {
                    isExpanded = !isExpanded
                    if (!isExpanded) {
                        scope.launch { state.animateScrollToItem(1, collapseOffset) }
                    } else {
                        collapseOffset = state.firstVisibleItemScrollOffset
                    }
                },
                isExpanded = isExpanded,
            )
        }
    }
}

@Composable
fun ImageControlBar(
    onClick: () -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(width = 1.dp, color = AbleBlue)
            .fillMaxWidth()
            .padding(start = 10.dp, top = 16.dp, end = 10.dp, bottom = 16.dp)
            .nonReplyClickable { onClick() }
    ) {
        AnimatedVisibility(visible = !isExpanded) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "상품정보 더보기",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontWeight = FontWeight(500),
                        color = AbleBlue,
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.chevrondown),
                    contentDescription = null,
                    tint = AbleBlue
                )
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "상품정보 접기",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontWeight = FontWeight(500),
                        color = AbleBlue,
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_up),
                    contentDescription = null,
                    tint = AbleBlue
                )
            }
        }
    }
}

@Composable
private fun CreatorReviewTitle(
    averageStar: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 5.dp)
    ) {
        Text(
            text = "크리에이터 리뷰",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                color = AbleDeep,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
            )
        )
        Image(
            painter = painterResource(id = R.drawable.ic_product_item_star),
            contentDescription = null,
            modifier = Modifier
        )
        Text(
            text = averageStar,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight(400),
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                color = SmallTextGrey,
            )
        )
    }
}

@Composable
private fun CreatorReviewContent(
    writerName: String,
    writerHeight: Int?,
    writerWeight: Int?,
    itemSize: String,
    description: String,
    imageUrl: String,
    starRating: Float,
    modifier: Modifier = Modifier
) {
    val titleText by remember {
        derivedStateOf {
            listOfNotNull(
                writerHeight?.let { "${it}cm" },
                writerWeight?.let { "${it}kg" },
                "$itemSize 착용"
            )
                .joinToString(separator = " / ")
        }
    }
    Row(
        modifier = modifier
            .width(325.dp)
            .height(133.dp)
            .background(PlaneGrey, RoundedCornerShape(15.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = previewPlaceHolder(id = R.drawable.card_image_thumbnail_test),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(69.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 12.dp)
        ) {
            Column {
                Text(
                    text = titleText,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                    )
                )
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        color = AbleDark,
                    ),
                    maxLines = 3
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_product_item_star),
                        contentDescription = null
                    )
                    Text(
                        text = starRating.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            color = SmallTextGrey,
                        )
                    )
                }
                Text(
                    text = writerName,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                    )
                )
            }
        }
    }
}

@Composable
fun BusinessInformation(
    paymentNoticeTabClick: ()-> Unit,
    sellerInformationTabClick: () -> Unit,
    businessInformationTabClick: () -> Unit,
    customerSupportTabClick: () -> Unit,
    isPaymentNoticeExpand: Boolean,
    isSellerInformationTabExpand: Boolean,
    isBusinessInformationTabExpand: Boolean,
    isCustomerSupportTabExpand: Boolean,
    businessName: String,
    brandName: String,
    businessNumber: String,
    reportNumber: String,
    contacts: String,
    email: String,
    roadAddress: String
) {
    Column {
        BusinessInformationTab(
            isExpanded = isPaymentNoticeExpand,
            titleText = "배송/교환/환불 안내",
            modifier = Modifier.nonReplyClickable(onClick = paymentNoticeTabClick)
        ) {
            Column {
                PaymentNoticeTitleAndDescription(
                    title = "배송",
                    descriptions = listOf(
                        PaymentNotice(
                            "1. ",
                            "평균 상품 준비 기간은 영업일 기준 1~5일이며, 판매자 사정에 따라 최대 14일까지 지연될 수 있습니다."
                        )
                    )
                )
                PaymentNoticeTitleAndDescription(
                    title = "교환/반품/환불 접수",
                    descriptions = listOf(
                        PaymentNotice(
                            "1. ",
                            "교환/반품/환불은 상품을 수령하신 후, 7일 이내에 ‘MY’ > ‘ 주문/관리' > ‘문의하기' 메뉴에서 신청할 수 있습니다."
                        ),
                        PaymentNotice(
                            "2. ",
                            "단순 변심에 의한 신청의 경우 추가 배송비가 발생하며, 추가 배송비는 브랜드 및 배송 주에서 따라 상이하므로 접수 시 별도 안내드립니다."
                        )
                    )
                )
                PaymentNoticeTitleAndDescription(
                    title = "교환/반품/환불 불가",
                    descriptions = listOf(
                        PaymentNotice("1. ", "상품을 수령하신 후 7일이 초과한 경우"),
                        PaymentNotice("2. ", "상품의 재판매가 불가한 경우"),
                        PaymentNotice("· ", "의류 제품은 단 1회라도 착용하면 상품의 가치가 훼손되기 때문에 교환 및 반품이 불가합니다."),
                        PaymentNotice("· ", "고객님의 부주의로 인해 상품이 변형/훼손/파손된 경우"),
                        PaymentNotice("· ", "화장품, 음식물, 향수, 담배, 방향제, 등으로 인해 상품에 오염이 발생한 경우"),
                        PaymentNotice("· ", "포장 자재(예: 비닐, 박스 등)또는 상품 정보 표시(예: 라벨, 상품 태그 등)등이 훼손 또는 분실된 경우"),
                        PaymentNotice("· ", "상품 수령 후 세탁/수선한 경우"),
                        PaymentNotice("3. ", "나염 상품은 상품 특성 상 패턴의 위치가 조금씩 다를 수 있으며, 이는 상품의 하자 또는 불량에 해당되지 않습니다."),
                        PaymentNotice("4. ", "상품의 마감 처리가 미흡한 경우(예: 실밤, 본드 자국, 초크 자국, 균일하지 않은 박음질, 지퍼가 부드럽지 않은 경우, 단추 구멍이 완벽히 뚫리지 않은 경우 등)는 상품의 하자 또는 불량에 해당하지 않습니다."),
                        PaymentNotice("5. ", "컴퓨터 모니터 및 스마트폰 화면 등의 차이로 인해 실물과 색상 차이가 있는 경우나 측정하는 방식에 따라 다를 수 있는 상품 사이즈 오차는 상품의 하자 또는 불량에 해당되지 않습니다."),
                        PaymentNotice("6. ", "상품과 관계가 없는 케이스, 포장지 등의 파손은 상품의 하자 또는 불량에 해당되지 않습니다."),
                        PaymentNotice("7. ", "충전재가 포함된 패딩류나 앙고라, 무스탕 소재가 포함된 상품 등에서 일부 털이 빠지는 현상이 발생할 수 있으며, 이는 자연스러운 현상이기 때문에 상품의 하자 똔느 불량에 해당되지 않습니다.")
                    )
                )
            }
        }
        BusinessInformationTab(
            isExpanded = isSellerInformationTabExpand,
            titleText = "판매자 정보",
            modifier = Modifier.nonReplyClickable(onClick = sellerInformationTabClick)
        ) {
            Column {
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "상호 / 대표자",
                    description = businessName
                )
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "브랜드",
                    description = brandName
                )
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "사업자번호",
                    description = businessNumber
                )
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "통신판매업신고",
                    description = reportNumber
                )
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "연락처",
                    description = contacts
                )
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "이메일",
                    description = email
                )
                SellerInformationTitleAndDescription(
                    modifier = Modifier.padding(top = 6.dp),
                    title = "영업소재지",
                    description = roadAddress
                )
            }
        }
        BusinessInformationTab(
            isExpanded = isBusinessInformationTabExpand,
            titleText = "사업자 정보",
            modifier = Modifier.nonReplyClickable(onClick = businessInformationTabClick)
        ) {
            Text(
                text = "스마일헌터 | 대표자 : 이재휘, 조민재 | 주소 : 경기도 가평군 청평면 경춘로 869 | 사업자등록번호 : 158-33-01102 | 전화 : 070-8648-1844 | 이메일 : ablebody@smilehuntercorp.com",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontWeight = FontWeight(400),
                    color = AbleDeep,
                )
            )
        }
        BusinessInformationTab(
            isExpanded = isCustomerSupportTabExpand,
            titleText = "고객지원",
            modifier = Modifier.nonReplyClickable(onClick = customerSupportTabClick)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(top = 6.dp)
            ) {
                Text(
                    text = "1:1 문의하기",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontWeight = FontWeight(400),
                        color = AbleDeep,
                    )
                )
                Text(
                    text = "이메일 : ablebody@smilehuntercorp.com",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontWeight = FontWeight(400),
                        color = AbleDeep,
                    )
                )
            }
        }
    }
}

@Composable
fun BusinessInformationTab(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    titleText: String,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            Text(
                text = titleText,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                    fontWeight = FontWeight(500),
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = AbleDark,
                )
            )
            Image(
                painter = painterResource(id = R.drawable.chevrondown),
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            content()
        }
    }
}

private data class PaymentNotice(
    val number: String,
    val description: String
)

@Composable
private fun PaymentNoticeTitleAndDescription(
    title: String,
    descriptions: List<PaymentNotice>
) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            fontWeight = FontWeight(500),
            color = AbleDark,
        ),
        modifier = Modifier.padding(bottom = 4.dp, top = 6.dp)
    )
    descriptions.forEach { (number, text) ->
        Row {
            Text(
                text = number,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                )
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                )
            )
        }
    }
}

@Composable
private fun SellerInformationTitleAndDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontWeight = FontWeight(400),
                color = AbleDark,
            ),
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = description,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontWeight = FontWeight(400),
                color = AbleDeep,
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemPaymentBottomSheet(
    onDismissRequest: () -> Unit,
    onItemPurchaseRequest: () -> Unit,
    onItemOptionRequest: (ItemOption) -> Unit,
    requireItemOption: Set<ItemOption>,
    selectOptionMap: Map<ItemOption, String>,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = Color.White,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
) {
    val scope = rememberCoroutineScope()
    val animateToDismiss: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        windowInsets = windowInsets
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            if (requireItemOption.contains(ItemOption.COLOR)) {
                val text = selectOptionMap.getOrDefault(ItemOption.COLOR, "색상 선택")
                ItemPaymentTextField(
                    text = text,
                    enable = text != "색상 선택",
                    modifier = Modifier.nonReplyClickable {
                        onItemOptionRequest(ItemOption.COLOR)
                    }
                )
            }
            if (requireItemOption.contains(ItemOption.SIZE)) {
                val text = selectOptionMap.getOrDefault(ItemOption.SIZE, "사이즈 선택")
                ItemPaymentTextField(
                    text = text,
                    enable = text != "사이즈 선택",
                    modifier = Modifier.nonReplyClickable {
                        onItemOptionRequest(ItemOption.SIZE)
                    }
                )
            }
        }
        val buttonEnable by remember(selectOptionMap, requireItemOption) {
            derivedStateOf { selectOptionMap.keys.containsAll(requireItemOption) }
        }
        CustomButton(
            text = "구매하기",
            enable = buttonEnable,
            onClick = { animateToDismiss(); onItemPurchaseRequest() }
        )
        Box(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ItemPaymentTextField(
    text: String,
    enable: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue = if (enable) AbleDark else SmallTextGrey
    )
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = AbleLight)
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontWeight = FontWeight(700),
                color = textColor,
            )
        )
        Image(
            painter = painterResource(id = R.drawable.chevrondown),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemOptionListBottomSheet(
    onDismissRequest: (String?) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = Color.White,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
) {
    val scope = rememberCoroutineScope()
    val animateToDismiss: (String?) -> Unit = { option ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest(option)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest(null) },
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        windowInsets = windowInsets
    ) {
        options.forEach {
            Text(
                text = it,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 16.dp)
                    .nonReplyClickable { animateToDismiss(it) }
            )
        }
        Box(modifier = Modifier.height(50.dp))
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(heightDp = 400)
@Composable
fun ItemDetailItemImageViewPreview() {
    ABLEBODY_AndroidTheme {
        val pagerState = rememberPagerState { 10 }
        ItemDetailImageView(pagerState = pagerState, imageUrlList = listOf(""))
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailTitlePreview() {
    ABLEBODY_AndroidTheme {
        ItemDetailTitle(
            brandPageRequest = {  },
            bookmarkClick = {  },
            brandName = "오옴",
            brandImageURL = "",
            isBookmarked = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailProfilePreview() {
    ABLEBODY_AndroidTheme {
        ItemDetailProfile(
            itemName = "[SIGNATURE] - mustard",
            price = 38000,
            salePrice = 41000,
            salePercentage = 10
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreatorReviewTitlePreview() {
    ABLEBODY_AndroidTheme {
        CreatorReviewTitle("5.0(1)")
    }
}

@Preview(showBackground = true)
@Composable
fun CreatorReviewContentPreview() {
    ABLEBODY_AndroidTheme {
        CreatorReviewContent(
            writerName = "sssooo",
            writerHeight = 160,
            writerWeight = 45,
            itemSize = "S",
            description = "ㅑ",
            imageUrl = "",
            starRating = 4.0f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPaymentTextFieldPreview() {
    ABLEBODY_AndroidTheme {
        ItemPaymentTextField("색상 선택", false)
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessInformationTabPreview() {
    ABLEBODY_AndroidTheme {
        BusinessInformationTab(
            isExpanded = false,
            titleText = "배송/교환/환불 안내"
        ) {

        }
    }
}

@Preview(heightDp = 2000)
@Composable
fun ItemDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
        ItemDetailScreen(
            onBackRequest = {  },
            onBookmarkRequest = { },
            purchaseOnClick = {  },
            itemClick = {  },
            brandOnClick = { _, _ -> },
            codyOnClick = {  },
            itemDetailUiState = ItemDetailUiState.Success(
                fakeItemDetailData.copy(detailImageUrls = emptyList()
                )
            )
        )
    }
}