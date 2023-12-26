package com.smilehunter.ablebody.presentation.item_detail.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
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
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private enum class ItemOption {
    SIZE, COLOR
}

@Composable
fun ItemDetailRoute(
    onBackRequest: () -> Unit,
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
                    val paymentPassthroughData = PaymentPassthroughData(
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
                    purchaseOnClick(paymentPassthroughData)
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {
                    Column(
                        modifier = Modifier
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

                        if (itemDetailData.item.avgStarRating != null) {
                            val creatorReviewPagerState = rememberPagerState { itemDetailData.itemReviews.size }

                            Column(
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                CreatorReviewTitle(averageStar = itemDetailData.item.avgStarRating)
                                HorizontalPager(
                                    state = creatorReviewPagerState,
                                    contentPadding = PaddingValues(end = 54.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp)
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
                }

                item(span = { GridItemSpan(this.maxLineSpan) }) {
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
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(items = itemDetailData.homePosts) { homePosts ->
                    AsyncImage(
                        model = homePosts.imageURL,
                        contentDescription = null,
                        placeholder = previewPlaceHolder(id = R.drawable.cody_item_test),
                        modifier = Modifier.nonReplyClickable {
                            codyOnClick(homePosts.id)
                        }
                    )
                }

                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    Box(modifier = Modifier.height(100.dp))
                }
            }

            CustomButton(
                text = "구매하기",
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                isItemPaymentBottomSheetShow = true
            }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemDetailImageView(
    pagerState: PagerState,
    imageUrlList: List<String>
) {
    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            AsyncImage(
                model = imageUrlList[page],
                contentDescription = "Detailed image description",
                contentScale = ContentScale.Crop,
                placeholder = previewPlaceHolder(id = R.drawable.product_item_test),
                modifier = Modifier
                    .fillMaxWidth()
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
private fun CreatorReviewTitle(
    averageStar: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 20.dp)
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
            onClick = { onItemPurchaseRequest() }
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

@Preview(heightDp = 1500)
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
            itemDetailUiState = ItemDetailUiState.Success(fakeItemDetailData)
        )
    }
}