package com.smilehunter.ablebody.presentation.payment.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.presentation.delivery.data.DeliveryPassthroughData
import com.smilehunter.ablebody.presentation.delivery.ui.DeliveryRequestMessageBottomSheet
import com.smilehunter.ablebody.presentation.delivery.ui.DeliveryTextField
import com.smilehunter.ablebody.presentation.payment.PaymentViewModel
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import com.smilehunter.ablebody.presentation.payment.data.PaymentUiState
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.InactiveGrey
import com.smilehunter.ablebody.ui.theme.LightShaded
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.CustomButton
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.KoreaMoneyFormatVisualTransformation
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentRoute(
    onBackRequest: () -> Unit,
    addressRequest: (DeliveryPassthroughData) -> Unit,
    receiptRequest: (String) -> Unit,
    paymentViewModel: PaymentViewModel = hiltViewModel(),
) {

    val bankTextValue by paymentViewModel.bankTextValue.collectAsStateWithLifecycle()
    val accountNumberTextValue by paymentViewModel.accountNumberTextValue.collectAsStateWithLifecycle()
    val accountHolderTextValue by paymentViewModel.accountHolderTextValue.collectAsStateWithLifecycle()
    val couponID by paymentViewModel.couponID.collectAsStateWithLifecycle()
    val userPointTextValue by paymentViewModel.userPointTextValue.collectAsStateWithLifecycle()
    val userData by paymentViewModel.userData.collectAsStateWithLifecycle()
    val coupons by paymentViewModel.coupons.collectAsStateWithLifecycle()
    val couponDiscountPrice by paymentViewModel.couponDiscountPrice.collectAsStateWithLifecycle()
    val deliveryAddress by paymentViewModel.deliveryAddress.collectAsStateWithLifecycle()

    val paymentPassthroughData by paymentViewModel.paymentPassthroughData.collectAsStateWithLifecycle()

    val orderItemID by paymentViewModel.orderItemID.collectAsStateWithLifecycle()

    if (orderItemID.isNotBlank()) {
        receiptRequest(orderItemID)
    }

    PaymentScreen(
        onBackRequest = onBackRequest,
        payButtonOnClick = paymentViewModel::orderItem,
        addressRequest = addressRequest,
        bankTextValueChange = paymentViewModel::updateBankTextValue,
        accountNumberTextValueChange = paymentViewModel::updateAccountNumberTextValue,
        accountHolderTextValueChange = paymentViewModel::updateAccountHolderTextValue,
        couponIDChange = paymentViewModel::updateCouponID,
        pointTextValueChange = paymentViewModel::updateUserPointTextValue,
        pointSelected = paymentViewModel::calculatorUserPoint,
        paymentPassthroughData = paymentPassthroughData,
        bankTextValue = bankTextValue,
        accountNumberTextValue = accountNumberTextValue,
        accountHolderTextValue = accountHolderTextValue,
        couponID = couponID,
        pointTextValue = userPointTextValue,
        couponDisCountPrice = couponDiscountPrice,
        userData = userData as? PaymentUiState.User,
        deliveryAddress = deliveryAddress as? PaymentUiState.DeliveryAddress,
        coupons = coupons
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBackRequest: () -> Unit,
    payButtonOnClick: (String, Int) -> Unit,
    addressRequest: (DeliveryPassthroughData) -> Unit,
    bankTextValueChange: (String) -> Unit,
    accountNumberTextValueChange: (String) -> Unit,
    accountHolderTextValueChange: (String) -> Unit,
    couponIDChange: (Int) -> Unit,
    pointTextValueChange: (String) -> Unit,
    pointSelected: (Int) -> Unit,
    paymentPassthroughData: PaymentPassthroughData?,
    bankTextValue: String,
    accountNumberTextValue: String,
    accountHolderTextValue: String,
    couponID: Int,
    pointTextValue: String,
    couponDisCountPrice: Int,
    userData: PaymentUiState.User?,
    deliveryAddress: PaymentUiState.DeliveryAddress?,
    coupons: PaymentUiState,
) {
    val scope = rememberCoroutineScope()
    var showCouponBottomSheet by rememberSaveable { mutableStateOf(false) }
    var agreementPrivateData by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isPayButtonEnable = bankTextValue.isNotBlank() && accountHolderTextValue.isNotBlank() && accountNumberTextValue.isNotEmpty() && agreementPrivateData
    val receipt = remember { mutableStateMapOf<String, Int>() }

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "주문 / 결제"
            )
        },
        bottomBar = {
            CustomButton(
                text = "결제하기",
                onClick = {
                          payButtonOnClick("CASH", receipt.values.sum())
                },
                enable = isPayButtonEnable
            )
        }
    ) { paddingValue ->

        if (coupons !is PaymentUiState.Coupons) {
            return@Scaffold
        }

        if (paymentPassthroughData == null) {
            return@Scaffold
        }

        var showDeliveryRequestMessageBottomSheet by rememberSaveable { mutableStateOf(false) }

        var deliveryRequestMessageValueState by remember {
            mutableStateOf(deliveryAddress?.data?.deliveryRequestMessage ?: "")
        }

        if (showDeliveryRequestMessageBottomSheet) {
            DeliveryRequestMessageBottomSheet(
                onDismissRequest = { showDeliveryRequestMessageBottomSheet = false },
                itemOnClick = {
                    deliveryRequestMessageValueState = it
                }
            )
        }

        if (showCouponBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCouponBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = null,
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                ) {
                    items(items = coupons.data) { item ->
                        CouponLayout(
                            onClick = {
                                couponIDChange(item.id)
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showCouponBottomSheet = false
                                    }
                                }
                                      },
                            enabled = !item.invalid,
                            title = item.couponTitle,
                            expirationCount = item.couponCount,
                            expirationDate = item.expirationDate,
                            discount = when (item.discountType) {
                                CouponData.DiscountType.PRICE -> "${item.discountAmount}원"
                                CouponData.DiscountType.RATE -> "${item.discountAmount}%"
                            }
                        )
                    }
                    item { Box(modifier = Modifier.height(30.dp)) }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValue)
                .verticalScroll(state = rememberScrollState())
        ) {
            OrderItemLayout(
                brandName = paymentPassthroughData.brandName,
                productName = paymentPassthroughData.itemName,
                profileImageURL = paymentPassthroughData.itemImageURL,
                salePercentage = paymentPassthroughData.salePercentage,
                productPrice = paymentPassthroughData.price,
                options = paymentPassthroughData.itemContentOptions
            )
            Divider(thickness = 4.dp, color = InactiveGrey)
            DeliveryAddressLayout(
                addressRequest = {
                    val deliveryPassthroughData = DeliveryPassthroughData(
                        attentionName = deliveryAddress?.data?.userName ?: "",
                        phoneNumber = deliveryAddress?.data?.phoneNumber ?: "",
                        roadAddress = deliveryAddress?.data?.roadAddress ?: "",
                        roadDetailAddress = deliveryAddress?.data?.roadDetailAddress ?: "",
                        zipCode = deliveryAddress?.data?.zipCode ?: "",
                        requestMessage = deliveryRequestMessageValueState,
                    )
                    addressRequest(deliveryPassthroughData)
                },
                requestMessageChange = { showDeliveryRequestMessageBottomSheet = true },
                userName = deliveryAddress?.data?.userName ?: "",
                phoneNumber = deliveryAddress?.data?.phoneNumber ?: "",
                roadAddress = deliveryAddress?.data?.roadAddress ?: "",
                roadDetailAddress = deliveryAddress?.data?.roadDetailAddress ?: "",
                requestMessage = deliveryRequestMessageValueState
            )
            Divider(thickness = 4.dp, color = InactiveGrey)

            val couponTextValue by remember(couponID) { derivedStateOf { coupons.data.firstOrNull { it.id == couponID }?.couponTitle ?: "" } }
            var isPointUsed by rememberSaveable { mutableStateOf(false) }

            DiscountLayout(
                couponButtonOnClick = {
                    if (couponTextValue.isNotEmpty()) {
                        couponIDChange(-1)
                    } else {
                        showCouponBottomSheet = true
                    }
                                      },
                pointButtonOnClick = {
                    if (pointTextValue.isNotBlank()) {
                        if (isPointUsed) {
                            pointTextValueChange("")
                        } else {
                            pointSelected(paymentPassthroughData.salePrice)
                        }
                        isPointUsed = !isPointUsed
                    }
                },
                pointTextOnValueChange = { pointTextValueChange(it) },
                couponTextValue = couponTextValue,
                couponSelected = couponTextValue.isNotEmpty(),
                pointTextValue = pointTextValue,
                point = userData?.data?.creatorPoint ?: 0,
                isPointUsed = isPointUsed && (userData?.data?.creatorPoint ?:0) >= 2000
            )

            Divider(thickness = 4.dp, color = InactiveGrey)
            PaymentMethodLayout(
                bankTextValueChange = bankTextValueChange,
                accountNumberTextValueChange = accountNumberTextValueChange,
                accountHolderTextValueChange = accountHolderTextValueChange,
                bankTextValue = bankTextValue,
                accountNumberTextValue = accountNumberTextValue,
                accountHolderTextValue = accountHolderTextValue
            )
            Divider(thickness = 4.dp, color = InactiveGrey)
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "총 결제금액",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = Color.Black,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                receipt.putAll(
                    mapOf(
                        "총 상품금액" to paymentPassthroughData.price,
                        "상품 할인" to paymentPassthroughData.differencePrice,
                        "쿠폰 할인" to couponDisCountPrice,
                        "포인트 할인" to if (isPointUsed) - (pointTextValue.toIntOrNull() ?:0) else 0,
                        "배송비" to 3000,
                    )
                )
                receipt.forEach { (key, value) ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.5.dp)
                    ) {
                        Text(
                            text = key,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                fontWeight = FontWeight(500),
                                color = AbleDark,
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            )
                        )
                        Text(
                            text = "${NumberFormat.getInstance().format(value)}원",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                fontWeight = FontWeight(500),
                                color = AbleDark,
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            )
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.5.dp)
                ) {
                    Text(
                        text = "결제금액",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    Text(
                        text = "${NumberFormat.getInstance().format(receipt.values.sum())}원",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
            Divider(thickness = 4.dp, color = InactiveGrey)

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "개인정보 이용 동의",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = Color.Black,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    Surface(
                        modifier = Modifier
                            .padding(3.75.dp)
                            .size(22.5.dp)
                            .nonReplyClickable { agreementPrivateData = !agreementPrivateData }
                    ) {
                        AnimatedVisibility(
                            visible = !agreementPrivateData,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.check_switch_off),
                                contentDescription = "uncheck"
                            )
                        }
                        AnimatedVisibility(
                            visible = agreementPrivateData,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.check_switch_on),
                                contentDescription = "check"
                            )
                        }
                    }
                }
                val uriHandler = LocalUriHandler.current
                Text(
                    text = "애블바디 이용 약관 보기",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = SmallTextGrey,
                        textDecoration = TextDecoration.Underline,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://spiffy-vegetarian-7f4.notion.site/38b4ac1baa874fbf8195784f7b4d1169")
                    }
                )
            }
        }
    }
}

@Composable
private fun OrderItemLayout(
    brandName: String,
    productName: String,
    profileImageURL: String,
    salePercentage: Int,
    productPrice: Int,
    options: List<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "주문 상품",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 18.dp)
        ) {
            AsyncImage(
                model = profileImageURL,
                contentDescription = "item",
                placeholder = previewPlaceHolder(id = R.drawable.product_item_test),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = brandName,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF8C959E),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                Text(
                    text = productName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.padding(bottom = 1.dp)
                )
                Text(
                    text = "옵션 : ${options.joinToString(separator = " · ")}",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF8C959E),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "${salePercentage}%",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF0069FF),
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    Text(
                        text = "${NumberFormat.getInstance(Locale.KOREA).format(productPrice)}원",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF000000),
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DeliveryAddressLayout(
    addressRequest: () -> Unit,
    requestMessageChange: () -> Unit,
    userName: String,
    phoneNumber: String,
    roadAddress: String,
    roadDetailAddress: String,
    requestMessage: String
) {
    val isEmptyAddress = userName.isBlank() && phoneNumber.isBlank() && roadAddress.isBlank() && roadDetailAddress.isBlank()
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "배송지 정보",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = AbleLight)
                .padding(horizontal = 16.dp)
        ) {

            if (isEmptyAddress) {
                Text(
                    text = "배송지 등록하기",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 13.dp)
                )
            }

            AnimatedVisibility(visible = !isEmptyAddress) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = userName,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF191E29),
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                            Text(
                                text = phoneNumber,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF191E29),
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(LightShaded, RoundedCornerShape(4.dp))
                                .clickable { addressRequest() }
                                .padding(vertical = 6.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = "배송지 변경",
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
                    Text(
                        text = roadAddress,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    Text(
                        text = roadDetailAddress,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    DeliveryTextField(
                        value = requestMessage,
                        onValueChange = {  },
                        placeholder = {
                            Text(
                                text = "배송 요청 사항을 선택하세요.",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                                    fontWeight = FontWeight(500),
                                    color = SmallTextGrey,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                        },
                        actionContent = {
                            Image(
                                painter = painterResource(id = R.drawable.chevrondown),
                                contentDescription = null
                            )
                        },
                        enabled = false,
                        modifier = Modifier
                            .nonReplyClickable { requestMessageChange() }
                            .padding(vertical = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscountLayout(
    couponButtonOnClick: () -> Unit,
    pointButtonOnClick: () -> Unit,
    pointTextOnValueChange: (String) -> Unit,
    couponTextValue: String,
    couponSelected: Boolean,
    pointTextValue: String,
    point: Int,
    isPointUsed: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "쿠폰 / 포인트",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        DiscountTextFieldLayout(
            onClick = couponButtonOnClick,
            enabled = false,
            buttonText = if (couponSelected) "취소" else "쿠폰 선택" ,
            title = "쿠폰",
            value = couponTextValue,
            onValueChange = {}
        )
        DiscountTextFieldLayout(
            onClick = pointButtonOnClick,
            enabled = !isPointUsed,
            buttonText = if (isPointUsed) "취소" else "사용",
            title = "포인트",
            titleDescription = "2,000점 이상 포인트 보유시 사용 가능",
            value = pointTextValue,
            onValueChange = pointTextOnValueChange,
            placeHolder = "${NumberFormat.getInstance().format(point)}포인트 보유",
            description = "포인트는 상품 금액의 최대 5%까지 사용 가능합니다.",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = KoreaMoneyFormatVisualTransformation()
        )
    }
}

@Composable
private fun PaymentMethodLayout(
    bankTextValueChange: (String) -> Unit,
    accountNumberTextValueChange: (String) -> Unit,
    accountHolderTextValueChange: (String) -> Unit,
    bankTextValue: String,
    accountNumberTextValue: String,
    accountHolderTextValue: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "결제방법",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .widthIn(min = 150.dp)
                .background(color = AbleBlue, shape = RoundedCornerShape(5.dp))
                .padding(10.dp),
        ) {
            Text(
                text = "무통장 입금",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    fontWeight = FontWeight(400),
                    color = Color.White,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
        }
        Text(
            text = "환불 계좌 정보",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(500),
                color = SmallTextGrey,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val paymentPlaceHolderTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = SmallTextGrey,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
            val paymentTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = Color.Black,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
            PaymentTextFieldLayout(
                value = bankTextValue,
                onValueChange = bankTextValueChange,
                textStyle = paymentTextStyle,
                placeHolder = {
                    Text(
                        text = "은행명 입력",
                        style = paymentPlaceHolderTextStyle
                    )
                },
            )
            PaymentTextFieldLayout(
                value = accountNumberTextValue,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        accountNumberTextValueChange(it)
                    }
                },
                textStyle = paymentTextStyle,
                placeHolder = {
                    Text(
                        text = "계좌번호 입력",
                        style = paymentPlaceHolderTextStyle
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            PaymentTextFieldLayout(
                value = accountHolderTextValue,
                onValueChange = accountHolderTextValueChange,
                textStyle = paymentTextStyle,
                placeHolder = {
                    Text(
                        text = "예금주 입력",
                        style = paymentPlaceHolderTextStyle
                    )
                }
            )
        }
    }
}

@Composable
private fun DiscountTextFieldLayout(
    onClick: () -> Unit,
    enabled: Boolean,
    buttonText: String,
    title: String,
    titleDescription: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String? = null,
    description: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
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
                text = title,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                    fontWeight = FontWeight(500),
                    color = SmallTextGrey,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            if (titleDescription != null) {
                Text(
                    text = titleDescription,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
        }
        val textColor by animateColorAsState(
            targetValue = if (enabled) Color.Black else AbleBlue
        )
        PaymentTextFieldLayout(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = textColor,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            placeHolder = {
                if (placeHolder != null) {
                    Text(
                        text = placeHolder,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            },
            actionContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .widthIn(min = 66.dp)
                        .nonReplyClickable(onClick = onClick)
                        .background(AbleDark, RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = buttonText,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = Color.White,
                        )
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
        if (description != null) {
            Text(
                text = description,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                    fontWeight = FontWeight(500),
                    color = AbleBlue,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
        }
    }
}

@Composable
private fun PaymentTextFieldLayout(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    placeHolder: @Composable (() -> Unit)? = null,
    actionContent: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.heightIn(min = 54.dp),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = InactiveGrey)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (value == "" && placeHolder != null) {
                    placeHolder()
                }
                it()
            }
            if (actionContent != null) {
                actionContent()
            }
        }
    }
}

@Composable
private fun CouponLayout(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    title: String,
    expirationCount: Int,
    expirationDate: Int,
    discount: String
) {
    Surface(
        shape = RoundedCornerShape(size = 15.dp),
        border = BorderStroke(1.dp, PlaneGrey),
        elevation = 1.5.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                Box(
                    modifier = Modifier
                        .nonReplyClickable { onClick() }
                        .background(
                            color = if (enabled) LightShaded else PlaneGrey,
                            shape = RoundedCornerShape(size = 4.dp)
                        )
                        .padding(horizontal = 20.5.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (enabled) "사용하기" else "사용완료",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = if (enabled) AbleBlue else SmallTextGrey,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "${expirationCount}명 남음",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                Text(
                    text = "${expirationDate}일 남음",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
            Text(
                text = discount,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(700),
                    color = AbleBlue,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderItemLayoutPreview() {
    OrderItemLayout(
        brandName = "제이엘브",
        productName = "스탠다드 무지 나시 4 Colors",
        salePercentage = 40,
        productPrice = 29000,
        options = listOf("블루", "1"),
        profileImageURL = ""
    )
}

@Preview(showBackground = true)
@Composable
fun UnregisteredDeliveryAddressLayoutPreview() {
    DeliveryAddressLayout(
        addressRequest = {},
        requestMessageChange = {  },
        userName = "",
        phoneNumber = "",
        roadAddress = "",
        roadDetailAddress = "",
        requestMessage = ""
    )
}

@Preview(showBackground = true)
@Composable
fun RegisteredDeliveryInfoLayoutPreview() {
    DeliveryAddressLayout(
        addressRequest = {},
        requestMessageChange = {  },
        userName = "조민재",
        phoneNumber = "010-9307-1141",
        roadAddress = "경기 가평군 청평면 경춘로 869",
        roadDetailAddress = "C동 201호",
        requestMessage = ""
    )
}

@Preview(showBackground = true)
@Composable
fun DiscountLayoutPreview() {
    DiscountLayout(
        couponButtonOnClick = {},
        pointButtonOnClick = {},
        pointTextOnValueChange = {},
        couponTextValue = "",
        couponSelected = false,
        pointTextValue = "",
        point = 3500,
        isPointUsed = false
    )
}

@Preview(showBackground = true)
@Composable
fun CouponLayoutPreview() {
    CouponLayout(
        onClick = {},
        enabled = true,
        title = "제이엘브 10% 쿠폰",
        expirationCount = 9,
        expirationDate = 0,
        discount = "15%"
    )
}

@Preview(showBackground = true)
@Composable
fun PaymentPreview() {
    PaymentMethodLayout(
        bankTextValueChange = {},
        accountNumberTextValueChange = {},
        accountHolderTextValueChange = {},
        bankTextValue = "",
        accountNumberTextValue = "",
        accountHolderTextValue = ""
    )
}

@Preview(heightDp = 1450)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        onBackRequest = { },
        payButtonOnClick = { payment, amountOfPayment -> },
        addressRequest = {  },
        bankTextValueChange = {},
        accountNumberTextValueChange = {},
        accountHolderTextValueChange = {},
        couponIDChange = {},
        pointTextValueChange = {},
        pointSelected = {},
        paymentPassthroughData = PaymentPassthroughData(
            itemID = 0,
            brandName = "brand",
            salePercentage = 30,
            price = 1000,
            salePrice = 5000,
            deliveryPrice = 3000,
            itemName = "아이템",
            itemIDOptions = listOf(),
            itemContentOptions = listOf(),
            itemImageURL = ""
        ),
        bankTextValue = "",
        accountNumberTextValue = "",
        accountHolderTextValue = "",
        couponID = -1,
        pointTextValue = "",
        couponDisCountPrice = 0,
        userData = null,
        deliveryAddress = null,
        coupons = PaymentUiState.Coupons(listOf())
    )
}