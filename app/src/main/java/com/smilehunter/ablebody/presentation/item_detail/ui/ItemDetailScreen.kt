@file:OptIn(ExperimentalFoundationApi::class)

package com.smilehunter.ablebody.presentation.item_detail.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.PurchaseItemData
import com.smilehunter.ablebody.presentation.main.ui.LocalMainScaffoldPaddingValue
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemDetailScreen(
    viewModel: ItemDetailViewModel = hiltViewModel(),
    id: Long,
    itemClick: (Long, Long) -> Unit,
    onBackRequest: () -> Unit,
    purchaseOnClick: (PurchaseItemData) -> Unit,
    brandOnClick: (Long, String) -> Unit
) {
    val itemDetailData by viewModel.itemDetailLiveData.observeAsState()
    val mainImageList = itemDetailData?.item?.images
    Log.d("mainImageList", mainImageList.toString())
    val colorList = itemDetailData?.colorList
    val sizeList = itemDetailData?.sizeList
    Log.d("DetailScreen", "colorList:${colorList} sizeList:${sizeList}")
    val configuration = LocalConfiguration.current
    val bookMark = itemDetailData?.bookmarked
    val optionBottomSheetState  = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context  = LocalContext.current
    var percent: Int = 0
    var purchaseItemData: PurchaseItemData
    // 브랜드 이름, 제품 이름, 제품 색상, 제품 사이즈, 가격, 세일 퍼센트

    Log.d("DetailScreen", itemDetailData.toString())

    Log.d("LOGdataid_DetailScreen", id.toString())
    LaunchedEffect(key1 = true) {
        viewModel.getData(id)
    }
    Scaffold(
        topBar = { BackButtonTopBarLayout(onBackRequest = onBackRequest) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            ViewPagerPage(mainImageList)

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
//                    .padding(15.dp),
                    .padding(horizontal = 15.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .nonReplyClickable {
                            itemDetailData?.item?.brand?.id?.let { brandOnClick(it,itemDetailData?.item?.brand?.name.toString()) }
                        },
                    verticalAlignment = Alignment.CenterVertically,

                    ){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = itemDetailData?.item?.brand?.thumbnail)
                            .placeholder(R.drawable.nike_store_test) // 로딩 중에 표시될 이미지
                            .build(),
                        contentDescription = "Detailed image description",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(44.dp)  // use the calculated height
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    Text(
                        text = itemDetailData?.item?.brand?.name ?: "",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF191E29),
                        ),
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.chevronforward),
                        contentDescription = "image description",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 5.dp)
                    )
                }

                if (bookMark != null) {
                    Log.d("bookMark", bookMark.toString())
                    Image(
                        painter = painterResource(
                            id = if (bookMark) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty
                        ),
                        contentDescription = "image description",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .nonReplyClickable {
                                viewModel.toggleBookMark(id)
//                                if (bookMark) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty
                            },
                        alignment = Alignment.CenterEnd
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Text(
                    text = itemDetailData?.item?.name ?: "",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 5.dp)
                ) {
                    val salePrice = itemDetailData?.item?.salePrice
                    val price = itemDetailData?.item?.price

                    val displayText = salePrice?.let {
                        "${
                            NumberFormat.getNumberInstance(Locale.KOREA).format(it)
                        }원"
                    } ?: price?.let {
                        "${
                            NumberFormat.getNumberInstance(Locale.KOREA).format(it)
                        }원"
                    } ?: "Unknown"

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
                        val roundedPercentage = round(salePercentage).toInt()
                        percent = roundedPercentage
                        Log.d(
                            "LOGPRICE",
                            "price: ${price}, ${salePrice}, ${salePercentage}, ${roundedPercentage} "
                        )

                        Text(
                            text = "${NumberFormat.getInstance(Locale.KOREA).format(price)}원",
                            style = TextStyle(
                                fontSize = 13.sp,
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
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = AbleBlue,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
            }
            val avgStarRating = itemDetailData?.item?.avgStarRating.toString()
            val regex = """\((\d+)\)""".toRegex()
            val matchResult = regex.find(avgStarRating)
            val numberInsideParentheses = matchResult?.groups?.get(1)?.value
            val defaultSize = 0
            val size = numberInsideParentheses?.toInt() ?: defaultSize

            Log.d("size", size.toString())
            if (size != 0) {
                //            val creatorReviewImage = itemDetailData?.data?.itemReviews?.get(0)?.images?.get(0)
                val createStarRating = itemDetailData?.itemReviews?.get(0)?.starRating
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
                                fontWeight = FontWeight(550),
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
                            text = itemDetailData?.item?.avgStarRating.toString(),
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
                        itemsIndexed(items = List(size) { Unit }) { index, _ ->
                            Log.d("Logindex", index.toString())
                            val reviewId = itemDetailData?.itemReviews?.getOrNull(index)?.id
                            Log.d("reviewId", reviewId.toString())
                            Box(
                                modifier = Modifier
                                    .width(350.dp)
                                    .height(155.dp)
                                    .padding(start = 16.dp, end = 10.dp)
                                    .background(
                                        color = Color(0xFFF3F4F6),
                                        shape = RoundedCornerShape(size = 15.dp)
                                    )
                                    //                                .clickable { Log.d("크리에이터 리뷰 항목 클릭", id.toString()) },

                                    .clickable { itemClick(id, reviewId!!) } // 여기에 클릭 시 화면 이동 로직 추가
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(13.dp)
                                ) {
                                    val creatorReviewImage =
                                        itemDetailData?.itemReviews?.getOrNull(index)?.images?.getOrNull(
                                            0
                                        )
                                    //                                Log.d("creatorReviewImage", creatorReviewImage.toString())
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(data = creatorReviewImage)
                                            .build(),
                                        contentDescription = "Detailed image description",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(68.dp)
                                            .height(120.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Column(
                                        modifier = Modifier.padding(top = 2.dp, start = 10.dp)
                                    ) {
                                        Row() {
                                            val review = itemDetailData?.itemReviews?.getOrNull(index)

                                            // height
                                            review?.creator?.height?.let { height ->
                                                Text(
                                                    text = "$height cm / ",
                                                    fontSize = 13.sp,
                                                    color = SmallTextGrey
                                                )
                                            }

                                            // weight
                                            review?.creator?.weight?.let { weight ->
                                                Text(
                                                    text = "$weight kg / ",
                                                    fontSize = 13.sp,
                                                    color = SmallTextGrey
                                                )
                                            }

                                            // size
                                            review?.size?.let { size ->
                                                if (size != "사이즈 정보 없음") {
                                                    Text(
                                                        text = "$size 착용",
                                                        fontSize = 13.sp,
                                                        color = SmallTextGrey
                                                    )
                                                } else {
                                                    Text(
                                                        text = size,
                                                        fontSize = 13.sp,
                                                        color = SmallTextGrey
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = itemDetailData?.itemReviews?.getOrNull(
                                                index
                                            )?.review.toString(),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.height(63.dp)

                                        )
                                        Row() {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_product_item_star),
                                                contentDescription = "image description",
                                                contentScale = ContentScale.None
                                            )
                                            Text(
                                                text = createStarRating.toString(),
                                                color = SmallTextGrey
                                            )

                                            Text(
                                                text = itemDetailData?.itemReviews?.getOrNull(
                                                    index
                                                )?.creator?.nickname.toString(),
                                                color = SmallTextGrey,
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                textAlign = TextAlign.End
                                            )
                                        }

                                    }

                                }

                            }
                        }
                    }
                }
            }
            val numOfHomePost = itemDetailData?.homePosts?.size
            Log.d("LOGhomPosts", numOfHomePost.toString())

            val codeImageUrls = mutableListOf<String>()
            if (numOfHomePost != null && numOfHomePost != 0) {
                for (i in 0 until numOfHomePost) {
                    itemDetailData?.homePosts?.get(i)?.imageURL?.let { imageUrl ->
                        codeImageUrls.add(imageUrl) // imageUrl이 null이 아닌 경우에만 리스트에 추가합니다.
                        Log.d("codeImageUrls.size", codeImageUrls.toString())
                    }
                }
                Text(
                    text = "이 제품을 활용한 코디",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(550),
                        color = Color(0xFF505863),
                        textAlign = TextAlign.Right,
                    ),
                    modifier = Modifier.padding(start = 20.dp, bottom = 5.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 5.dp, end = 20.dp)
                        .height(165.dp)
                ) {
                    items(codeImageUrls) { imageUrl ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(165.dp)
                                .clickable {
                                    //TODO : 크리에이터 상세페이지_코디 화면으로 이동
                                }
                                .padding(1.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = imageUrl,
                                    builder = {
                                        crossfade(true)
                                        placeholder(R.drawable.ic_launcher_background) // 로딩 중 또는 오류 발생 시 표시할 placeholder 이미지.
                                    }
                                ),
                                contentDescription = "My content description", // 접근성을 위한 설명을 제공합니다.
                                modifier = Modifier
                                    .fillMaxSize(), // 이미지가 상자 전체를 채우도록 지정합니다.
                                //                                .padding(start = 2.dp),
                                contentScale = ContentScale.Crop, // 이미지의 비율을 유지하면서 전체 영역에 맞게 크기를 조정합니다.
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.size(90.dp)
            ){

            }
        }

        ModalBottomSheetLayout(
            sheetState = optionBottomSheetState,
            sheetContent = {
                val columnModifier = if (itemDetailData?.itemOptionList?.size == 2) {
                    Modifier.height(250.dp)
                } else if (itemDetailData?.itemOptionList?.size == 1) {
                    Modifier.height(180.dp)
                } else {
                    Modifier
                }
                Column(modifier = columnModifier)  {
                    var size by remember { mutableStateOf("") }
                    var color by remember { mutableStateOf("") }
                    Log.d("sizecolor", "$size $color")

                    // 여기에 바텀 시트 내용을 작성합니다.
                    purchaseItemData = PurchaseItemData(
                        brandName = itemDetailData?.item?.brand?.name.toString(),
                        itemName = itemDetailData?.item?.name.toString(),
                        itemColor = color,
                        itemSize = size,
                        price = itemDetailData?.item?.salePrice  ?: itemDetailData?.item?.price ?: 0,
                        salePercent = percent,
                        itemImage = mainImageList?.get(0) ?: "",
                        deliveryFee = itemDetailData?.item?.deliveryFee ?: 3000
                    )
                    ExposedDropdownMenuSample(colorList, sizeList, colorOnChange = {color = it}, sizeOnChange = {size = it},
                        purchaseButtonOnClick = {purchaseOnClick(it)}, purchaseItemData = purchaseItemData
                    )
                    Log.d("1purchaseItemData", purchaseItemData.toString())
//                    purchaseOnClick(purchaseItemData)
                }
            },
            sheetBackgroundColor = Color.White,
            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (itemDetailData?.itemOptionList?.isEmpty() == true) {
                                val url: String = itemDetailData?.item?.redirectUrl.toString()
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
//                                Log.d("ItemDetailScreen", "itemOptionList 비어있음")
                            } else {
                                optionBottomSheetState.show()
                            }
                        }

                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(55.dp)
                        .align(Alignment.BottomCenter),
                    colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
                ) {
                    Text(
                        text = if(colorList == null && sizeList == null) "${itemDetailData?.item?.redirectText} 구매링크 이동" else "구매하기",
                        color = Color.White
                    )
                }
            }
        }

        Box(
            modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current)
        )

    }

}
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
////            .padding(bottom = 20.dp),
//        contentAlignment = Alignment.BottomCenter
//    ){


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenuSample(
    colorList: List<String>?,
    sizeList: List<String>?,
    colorOnChange: (String) -> Unit,
    sizeOnChange: (String) -> Unit,
    purchaseButtonOnClick: (PurchaseItemData) -> Unit,
    purchaseItemData: PurchaseItemData
) {
    val optionDetailBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val optionCoroutineScope = rememberCoroutineScope()

    // 현재 활성화된 리스트를 나타내는 상태 추가
    val activeList = remember { mutableStateOf<List<String>?>(null) }
    val selectedColor = remember { mutableStateOf<String?>(null) }
    val selectedSize = remember { mutableStateOf<String?>(null) }
// 두 항목의 선택 여부를 추적하는 상태
    val isColorSelected = remember { mutableStateOf(false) }
    val isSizeSelected = remember { mutableStateOf(false) }
// "구매하기" 버튼의 활성화 상태 결정
    val isButtonEnabled = when {
        colorList != null && sizeList != null -> isColorSelected.value && isSizeSelected.value
        colorList != null -> isColorSelected.value
        sizeList != null -> isSizeSelected.value
        else -> false
    }
    var colorSelected by remember { mutableStateOf(false) }
    var sizeSelected by remember { mutableStateOf(false) }

    val colorListSize = colorList?.size
    val sizeListSize = sizeList?.size
    val activeListSize = activeList.value?.size
    val optionListSize = activeListSize?.let { it.dp * 70 }
    ModalBottomSheetLayout(
        sheetState = optionDetailBottomSheetState,
        sheetContent = {
            if (optionListSize != null) {
                Log.d("activeList 클릭", optionListSize.toString())
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp)
                        //                    .height(350.dp)
//                        .height(optionListSize)
//                        .heightIn(min = optionListSize, max = 282.dp)
//                        .heightIn(min = 582.dp)
                        .fillMaxWidth()  // 가로로 꽉 차게 설정
                    //                    .wrapContentHeight() // 내부 항목들의 합에 따라 높이 조정
                    //                    .heightIn(min = 100.dp, max = 600.dp) // 최소 및 최대 높이 제한 설정
                ) {
                    // 현재 활성화된 리스트의 항목들 출력
                    activeList.value?.forEach { item ->
                        Text(
                            text = item,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(bottom = 25.dp)
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable(onClick = {
                                    Log.d("item","항목 선택됨")
                                    if (activeList.value == colorList) {
                                        selectedColor.value = item
                                        isColorSelected.value = true // 색상 선택됨
                                        Log.d("colorItem", item)
                                        colorOnChange(item)
                                        colorSelected = true
                                    }
                                    if (activeList.value == sizeList) {
                                        selectedSize.value = item
                                        isSizeSelected.value = true // 사이즈 선택됨
                                        Log.d("sizeItem", item)
                                        sizeOnChange(item)
                                        sizeSelected = true
                                    }
                                    optionCoroutineScope.launch {
                                        optionDetailBottomSheetState.hide() // 이부분이 변경됨
                                    }
                                })
                        )
                    }
                }
            }
        },
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {
        Box() {
            Column() {
                if (colorList != null) {
                    Spacer(modifier = Modifier.height(5.dp))
                    ColorSizeTextField(
                        option = selectedColor.value ?: "색상",
                        colorList = colorList,
                        sizeList = sizeList, // 파라미터 추가
                        colorOnClick = {
                            optionCoroutineScope.launch {
                                activeList.value = colorList
                                optionDetailBottomSheetState.show()
                            }
                        },
                        sizeOnClick = {},
                        selected = colorSelected
                    )
                }
                if (sizeList != null) {
                    Spacer(modifier = Modifier.padding(5.dp))
                    ColorSizeTextField(
                        option = selectedSize.value ?: "사이즈",
                        colorList = colorList, // 파라미터 추가
                        sizeList = sizeList,
                        colorOnClick = {},
                        sizeOnClick = {
                            optionCoroutineScope.launch {
                                activeList.value = sizeList
                                optionDetailBottomSheetState.show()
                            }
                        },
                        selected = sizeSelected
                    )
                }

                Button(
                    onClick = {
                        purchaseButtonOnClick(purchaseItemData)
                        Log.d("purchaseItemData", purchaseItemData.toString())
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
                    enabled = isButtonEnabled
                ) {
                    Text(
                        text = "구매하기",
                        color = Color.White
                    )
                }
            }
        }
    }
}



@Composable
fun ColorSizeTextField(
    option: String,
    colorList: List<String>?,
    sizeList: List<String>?,  // 새로 추가된 파라미터
    colorOnClick: (String) -> Unit,
    sizeOnClick: (String) -> Unit,
    selected: Boolean
){
    Log.d("LOGoption", option)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .border(BorderStroke(1.dp, SmallTextGrey)),
        verticalAlignment = Alignment.CenterVertically
    ){
        androidx.compose.material3.Text(
            text = option,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if(selected) Color.Black else SmallTextGrey,
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 24.dp)
                .clickable {
                    when {
                        option == "색상" || colorList?.contains(option) == true -> colorOnClick(option)
                        option == "사이즈" || sizeList?.contains(option) == true -> sizeOnClick(option)
                    }
                }
                .weight(1f)
        )
        Icon(
            Icons.Filled.KeyboardArrowDown,
            contentDescription = "Expanded",
            Modifier
                .padding(end = 25.dp)
                .size(23.dp),
            tint = AbleDeep
        )
    }
}


@Composable
fun ViewPagerPage(mainImageList: List<String>?) {
    if (mainImageList == null || mainImageList.isEmpty()) return

    val pagerState = rememberPagerState {
        mainImageList.size
    }

    Box() {
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
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        if(mainImageList.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(mainImageList.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) AbleBlue else AbleLight
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                    )
                }
            }
        }
    }
}
