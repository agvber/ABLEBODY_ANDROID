package com.smilehunter.ablebody.presentation.order_management.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.OrderItemData
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.DELIVERY_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.DEPOSIT_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.DEPOSIT_WAITING
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.EXCHANGE_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.EXCHANGE_REQUEST
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.ON_DELIVERY
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.ON_EXCHANGE_DELIVERY
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.ORDER_CANCELED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.REFUND_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.REFUND_REQUEST
import com.smilehunter.ablebody.model.fake.fakeOrderItemData
import com.smilehunter.ablebody.presentation.order_management.OrderManagementViewModel
import com.smilehunter.ablebody.presentation.order_management.data.OrderManagementUiState
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.nonReplyClickable
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderItemListRoute(
    onBackRequest: () -> Unit,
    itemOnClick: (String) -> Unit,
    orderManagementViewModel: OrderManagementViewModel = hiltViewModel(),
) {
    val orderItems by orderManagementViewModel.orderItems.collectAsStateWithLifecycle()
    val deliveryTrackingData by orderManagementViewModel.deliveryTrackingData.collectAsStateWithLifecycle()

    OrderItemListScreen(
        onBackRequest = onBackRequest,
        itemOnClick = itemOnClick,
        cancelOrderItem = { id ->
            orderManagementViewModel.cancelOrderItem(id)
            orderManagementViewModel.refreshOrderItems()
        },
        deliveryInfoRequest = orderManagementViewModel::updateDeliveryTrackingID,
        deliveryCompanyName = (deliveryTrackingData as? OrderManagementUiState.DeliveryTracking)?.data?.deliveryCompanyName ?: "",
        deliveryTrackingNumber = (deliveryTrackingData as? OrderManagementUiState.DeliveryTracking)?.data?.trackingNumber ?: "",
        orderItems = orderItems as? OrderManagementUiState.OrderItems
    )
}

@Composable
fun OrderItemListScreen(
    onBackRequest: () -> Unit,
    itemOnClick: (String) -> Unit,
    cancelOrderItem: (String) -> Unit,
    deliveryInfoRequest: (String) -> Unit,
    deliveryCompanyName: String,
    deliveryTrackingNumber: String,
    orderItems: OrderManagementUiState.OrderItems?
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "주문 관리"
            )
        }
    ) { paddingValue ->
        if (orderItems == null) return@Scaffold

        if (orderItems.data.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {
                Text(
                    text = "주문하신 내역이 없습니다.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                OrderActionButton(text = "쇼핑하러 가기")
            }
        }

        var showDeliveryTrackingNumberDialog by rememberSaveable { mutableStateOf(false) }
        val showOrderItemCancelDialog = remember { mutableStateMapOf<String, String>() }
        var showDeliveryCompleteDialog by rememberSaveable { mutableStateOf(false) }

        if (showDeliveryTrackingNumberDialog) {
            DeliveryTrackingNumberDialog(
                onDismissRequest = { showDeliveryTrackingNumberDialog = false },
                positiveButtonOnClick = { showDeliveryTrackingNumberDialog = false },
                deliveryCompany = deliveryCompanyName,
                trackingNumber = deliveryTrackingNumber
            )
        }
        if (showOrderItemCancelDialog["is_show"] == "true") {
            OrderItemCancelDialog(
                onDismissRequest = { showOrderItemCancelDialog["is_show"] = "" },
                positiveButtonOnClick = {
                    showOrderItemCancelDialog["id"]?.let(cancelOrderItem)
                    showOrderItemCancelDialog["is_show"] = ""
                },
                negativeButtonOnClick = { showOrderItemCancelDialog["is_show"] = "" }
            )
        }
        if (showDeliveryCompleteDialog) {
            DeliveryCompleteDialog(
                onDismissRequest = { showDeliveryCompleteDialog = false },
                positiveButtonOnClick = { showDeliveryCompleteDialog = false }
            )
        }

        LazyColumn(
            modifier = Modifier.padding(paddingValue)
        ) {
            items(items = orderItems.data) { item ->
                DateWithColumn(
                    titleText = item.orderedDate,
                    modifier = Modifier.padding(16.dp)
                ) {
                    OrderItemLayout(
                        stateText = convertOderStatusToString(item.orderStatus),
                        brandName = item.brandName,
                        productName = item.itemName,
                        productPrice = item.amountOfPayment,
                        productImageURL = item.itemImageURL,
                        options = item.itemOptionDetailList.map { it.itemOptionDetail },
                        modifier = Modifier.nonReplyClickable { itemOnClick(item.id) }
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isShowDeliveryButton(item.orderStatus)) {
                            OrderActionButton(
                                text = when (item.orderStatus) {
                                    DEPOSIT_WAITING -> "주문 취소"
                                    DEPOSIT_COMPLETED -> "주문 취소"
                                    ON_DELIVERY -> "배송조회"
                                    DELIVERY_COMPLETED -> "배송조회"
                                    else -> ""
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .nonReplyClickable {
                                        if (item.orderStatus == ON_DELIVERY) {
                                            deliveryInfoRequest(item.id)
                                            showDeliveryTrackingNumberDialog = true
                                        }

                                        if (item.orderStatus == DELIVERY_COMPLETED) {
                                            showDeliveryCompleteDialog = true
                                        }

                                        if (item.orderStatus == DEPOSIT_WAITING || item.orderStatus == DEPOSIT_COMPLETED) {
                                            showOrderItemCancelDialog["id"] = item.id
                                            showOrderItemCancelDialog["is_show"] = "true"
                                        }
                                    }
                            )
                        }
                        val uriHandler = LocalUriHandler.current
                        OrderActionButton(
                            text = "문의하기",
                            modifier = Modifier
                                .weight(1f)
                                .nonReplyClickable { uriHandler.openUri("https://pf.kakao.com/_FQyQb") }
                        )
                    }
                }
            }
        }
    }
}

private fun convertOderStatusToString(status: OrderItemData.OrderStatus) =
    when (status) {
        DEPOSIT_WAITING -> "입금대기"
        DEPOSIT_COMPLETED -> "입금완료"
        ON_DELIVERY -> "배송중"
        DELIVERY_COMPLETED -> "배송완료"
        ORDER_CANCELED -> "주문취소"
        REFUND_REQUEST -> "환불요청"
        REFUND_COMPLETED -> "환불완료"
        EXCHANGE_REQUEST -> "교환요청"
        ON_EXCHANGE_DELIVERY -> "교환배송중"
        EXCHANGE_COMPLETED -> "교환완료"
    }

private fun isShowDeliveryButton(status: OrderItemData.OrderStatus) = when (status) {
    DEPOSIT_WAITING -> true
    DEPOSIT_COMPLETED -> true
    ON_DELIVERY -> true
    DELIVERY_COMPLETED -> true
    ORDER_CANCELED -> false
    REFUND_REQUEST -> false
    REFUND_COMPLETED -> false
    EXCHANGE_REQUEST -> true
    ON_EXCHANGE_DELIVERY -> true
    EXCHANGE_COMPLETED -> true
}

@Composable
fun OrderActionButton(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(BorderStroke(1.dp, AbleLight))
            .padding(vertical = 13.5.dp, horizontal = 24.dp)
    ) {
        Text(
            text = text,
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

@Composable
fun DateWithColumn(
    titleText: String,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = titleText,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = SmallTextGrey,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        content()
    }
}

@Composable
fun OrderItemLayout(
    modifier: Modifier = Modifier,
    stateText: String = "입금 대기",
    brandName: String,
    productName: String,
    productPrice: Int,
    productImageURL: String,
    options: List<String>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.padding(vertical = 18.dp)
    ) {
        AsyncImage(
            model = productImageURL,
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
                text = stateText,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(700),
                    color = AbleBlue,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                Text(
                    text = "${NumberFormat.getInstance(Locale.KOREA).format(productPrice)}원",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
        }
    }
}

@Composable
fun OrderItemCancelDialog(
    onDismissRequest: () -> Unit,
    positiveButtonOnClick: () -> Unit,
    negativeButtonOnClick: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = onDismissRequest,
        positiveButtonOnClick = positiveButtonOnClick,
        negativeButtonOnClick = negativeButtonOnClick,
        positiveText = "예",
        negativeText = "아니오",
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "주문을 취소하시겠어요?",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            Text(
                text = "취소한 주문 건은 되돌릴 수 없어요.",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    fontWeight = FontWeight(400),
                    color = SmallTextGrey,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}

@Composable
fun DeliveryTrackingNumberDialog(
    onDismissRequest: () -> Unit,
    positiveButtonOnClick: () -> Unit,
    deliveryCompany: String,
    trackingNumber: String
) {
    AbleBodyAlertDialog(
        onDismissRequest = onDismissRequest,
        positiveButtonOnClick = positiveButtonOnClick,
        negativeButtonOnClick = {  },
        positiveText = "확인",
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = deliveryCompany,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            val clipboardManager = LocalClipboardManager.current
            val context = LocalContext.current
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.5.dp)
            ) {
                Text(
                    text = trackingNumber,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 26.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(1.dp, SmallTextGrey),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                        .nonReplyClickable {
                            clipboardManager.setText(AnnotatedString(text = trackingNumber))
                            Toast
                                .makeText(context, "복사되었습니다", Toast.LENGTH_SHORT)
                                .show()
                        }
                ) {
                    Text(
                        text = "복사",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_light)),
                            fontWeight = FontWeight(300),
                            color = AbleBlue,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DeliveryCompleteDialog(
    onDismissRequest: () -> Unit,
    positiveButtonOnClick: () -> Unit,
) {
    AbleBodyAlertDialog(
        onDismissRequest = onDismissRequest,
        positiveButtonOnClick = positiveButtonOnClick,
        negativeButtonOnClick = {  },
        positiveText = "확인",
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "배송이 완료됐습니다.",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.padding(bottom = 20.dp)
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
        productPrice = 4444,
        productImageURL = "",
        options = listOf("블루", "1")
    )
}

@Preview(showBackground = true)
@Composable
fun DeliveryTrackingNumberDialogPreview() {
    ABLEBODY_AndroidTheme {
        DeliveryTrackingNumberDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { },
            deliveryCompany = "CJ대한통운",
            trackingNumber = "0000000000000"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderItemCancelDialogPreview() {
    ABLEBODY_AndroidTheme {
        OrderItemCancelDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { },
            negativeButtonOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryCompleteDialogPreview() {
    ABLEBODY_AndroidTheme {
        DeliveryCompleteDialog(onDismissRequest = {  }) {}
    }
}

@Preview
@Composable
fun OrderItemListScreenPreview() {
    ABLEBODY_AndroidTheme {
        OrderItemListScreen(
            onBackRequest = { },
            itemOnClick = { },
            cancelOrderItem = {  },
            deliveryInfoRequest = {  },
            deliveryCompanyName = "",
            deliveryTrackingNumber = "",
            orderItems = OrderManagementUiState.OrderItems(fakeOrderItemData)
        )
    }
}