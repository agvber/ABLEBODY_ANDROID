package com.smilehunter.ablebody.presentation.order_management.ui

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.ReceiptData
import com.smilehunter.ablebody.model.fake.fakeReceiptData
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.presentation.order_management.OrderManagementViewModel
import com.smilehunter.ablebody.presentation.order_management.data.OrderManagementUiState
import com.smilehunter.ablebody.presentation.receipt.ReceiptViewModel
import com.smilehunter.ablebody.presentation.receipt.data.ReceiptUiState
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.InactiveGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable
import retrofit2.HttpException
import java.text.NumberFormat

@Composable
fun OrderItemDetailRoute(
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    orderManagementViewModel: OrderManagementViewModel = hiltViewModel(),
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
) {
    val deliveryTrackingData by orderManagementViewModel.deliveryTrackingData.collectAsStateWithLifecycle()
    val receiptData by receiptViewModel.receiptData.collectAsStateWithLifecycle()

    OrderItemDetailScreen(
        onBackRequest = onBackRequest,
        cancelOrderItem = { id ->
            orderManagementViewModel.cancelOrderItem(id)
            receiptViewModel.refreshReceiptData()
        },
        deliveryInfoRequest = orderManagementViewModel::updateDeliveryTrackingID,
        deliveryCompanyName = (deliveryTrackingData as? OrderManagementUiState.DeliveryTracking)?.data?.deliveryCompanyName ?: "",
        deliveryTrackingNumber = (deliveryTrackingData as? OrderManagementUiState.DeliveryTracking)?.data?.trackingNumber ?: "",
        receiptData = (receiptData as? ReceiptUiState.Receipt)?.data
    )

    var isNetworkDisConnectedDialogShow by remember { mutableStateOf(false) }
    if (isNetworkDisConnectedDialogShow) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { receiptViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }

    if (receiptData is ReceiptUiState.LoadFail) {
        val throwable = (receiptData as ReceiptUiState.LoadFail).t
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

    if (receiptData is ReceiptUiState.Receipt) {
        if (isNetworkDisConnectedDialogShow) {
            isNetworkDisConnectedDialogShow = false
        }
    }
}


@Composable
fun OrderItemDetailScreen(
    onBackRequest: () -> Unit,
    cancelOrderItem: (String) -> Unit,
    deliveryInfoRequest: (String) -> Unit,
    deliveryCompanyName: String,
    deliveryTrackingNumber: String,
    receiptData: ReceiptData?
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "주문 상세"
            )
        }
    ) { paddingValue ->
        if (receiptData == null) {
            return@Scaffold
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

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValue)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = receiptData.orderedDate.run { "$year.$monthValue.$dayOfMonth" },
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                Text(
                    text = "주문번호 : ${receiptData.orderID}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                OrderItemLayout(
                    stateText = convertOderStatusToString(receiptData.orderStatus),
                    brandName = receiptData.brandName,
                    productName = receiptData.itemName,
                    productPrice = receiptData.price,
                    productImageURL = receiptData.itemImageURL,
                    options = receiptData.itemOptionDetailList.map { it.itemOptionDetail }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isShowDeliveryButton(receiptData.orderStatus)) {
                        OrderActionButton(
                            text = when (receiptData.orderStatus) {
                                ReceiptData.OrderStatus.DEPOSIT_WAITING -> "주문 취소"
                                ReceiptData.OrderStatus.DEPOSIT_COMPLETED -> "주문 취소"
                                ReceiptData.OrderStatus.ON_DELIVERY -> "배송조회"
                                ReceiptData.OrderStatus.DELIVERY_COMPLETED -> "배송조회"
                                else -> ""
                            },
                            modifier = Modifier
                                .weight(1f)
                                .nonReplyClickable {
                                    if (receiptData.orderStatus == ReceiptData.OrderStatus.ON_DELIVERY) {
                                        deliveryInfoRequest(receiptData.orderID)
                                        showDeliveryTrackingNumberDialog = true
                                    }

                                    if (receiptData.orderStatus == ReceiptData.OrderStatus.DELIVERY_COMPLETED) {
                                        showDeliveryCompleteDialog = true
                                    }

                                    if (receiptData.orderStatus == ReceiptData.OrderStatus.DEPOSIT_WAITING || receiptData.orderStatus == ReceiptData.OrderStatus.DEPOSIT_COMPLETED) {
                                        showOrderItemCancelDialog["id"] = receiptData.orderID
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
            Divider(thickness = 4.dp, color = InactiveGrey)
            TitleWithColumn(titleText = "무통장 입금 계좌 정보",) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    mapOf(
                        "입금 은행" to "농협",
                        "예금주" to "조민재",
                        "계좌번호" to "352-2134-4360-73",
                        "입금기한" to receiptData.depositDeadline.run { "${monthValue}월 ${dayOfMonth}일까지" }
                    )
                        .forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = it.key,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                        fontWeight = FontWeight(500),
                                        color = SmallTextGrey,
                                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                                    )
                                )
                                Text(
                                    text = it.value,
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
                }
                val context = LocalContext.current
                val clipboardManager = LocalClipboardManager.current
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, AbleLight))
                        .nonReplyClickable {
                            clipboardManager.setText(AnnotatedString("농협 3522134436073"))
                            Toast
                                .makeText(context, "계좌번호가 복사되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                ) {
                    Text(
                        text = "계좌 번호 복사하기",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                            fontWeight = FontWeight(500),
                            color = AbleDark,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                        modifier = Modifier.padding(vertical = 13.5.dp)
                    )
                }
            }
            Divider(thickness = 4.dp, color = InactiveGrey)
            TitleWithColumn(titleText = "배송지 정보") {
                Column {
                    mapOf(
                        "받는 분" to receiptData.receiverName,
                        "주소" to "${receiptData.roadAddress}\n${receiptData.roadAddressDetail}",
                        "연락처" to receiptData.phoneNumber,
                    )
                        .forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = it.key,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                        fontWeight = FontWeight(500),
                                        color = SmallTextGrey,
                                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                                    )
                                )
                                Text(
                                    text = it.value,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                        fontWeight = FontWeight(500),
                                        color = AbleDark,
                                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                                    ),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                }
            }
            Divider(thickness = 4.dp, color = InactiveGrey)
            TitleWithColumn(titleText = "결제 정보") {
                val numberFormat = NumberFormat.getInstance()
                mapOf(
                    "총 상품 금액" to receiptData.price,
                    "상품 할인" to receiptData.itemDiscount,
                    "쿠폰 할인" to receiptData.couponDiscount,
                    "포인트 할인" to receiptData.pointDiscount,
                    "배송비" to receiptData.deliveryPrice,
                )
                    .forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.5.dp)
                        ) {
                            Text(
                                text = it.key,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                    fontWeight = FontWeight(500),
                                    color = SmallTextGrey,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                            Text(
                                text = "${numberFormat.format(it.value)}원",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                    fontWeight = FontWeight(500),
                                    color = AbleDark,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                ),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.5.dp)
                ) {
                    Text(
                        text = "총 결제 금액",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    Text(
                        text = "${numberFormat.format(receiptData.amountOfPayment)}원",
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
            Box(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun TitleWithColumn(
    titleText: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
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
                color = Color.Black,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        content()
    }
}

private fun isShowDeliveryButton(status: ReceiptData.OrderStatus) = when (status) {
    ReceiptData.OrderStatus.DEPOSIT_WAITING -> true
    ReceiptData.OrderStatus.DEPOSIT_COMPLETED -> true
    ReceiptData.OrderStatus.ON_DELIVERY -> true
    ReceiptData.OrderStatus.DELIVERY_COMPLETED -> true
    ReceiptData.OrderStatus.ORDER_CANCELED -> false
    ReceiptData.OrderStatus.REFUND_REQUEST -> false
    ReceiptData.OrderStatus.REFUND_COMPLETED -> false
    ReceiptData.OrderStatus.EXCHANGE_REQUEST -> true
    ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY -> true
    ReceiptData.OrderStatus.EXCHANGE_COMPLETED -> true
}

private fun convertOderStatusToString(status: ReceiptData.OrderStatus) =
    when (status) {
        ReceiptData.OrderStatus.DEPOSIT_WAITING -> "입금대기"
        ReceiptData.OrderStatus.DEPOSIT_COMPLETED -> "입금완료"
        ReceiptData.OrderStatus.ON_DELIVERY -> "배송중"
        ReceiptData.OrderStatus.DELIVERY_COMPLETED -> "배송완료"
        ReceiptData.OrderStatus.ORDER_CANCELED -> "주문취소"
        ReceiptData.OrderStatus.REFUND_REQUEST -> "환불요청"
        ReceiptData.OrderStatus.REFUND_COMPLETED -> "환불완료"
        ReceiptData.OrderStatus.EXCHANGE_REQUEST -> "교환요청"
        ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY -> "교환배송중"
        ReceiptData.OrderStatus.EXCHANGE_COMPLETED -> "교환완료"
    }

@Preview(heightDp = 1200)
@Composable
fun OrderItemDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
        OrderItemDetailScreen(
            onBackRequest = {},
            cancelOrderItem = {},
            deliveryInfoRequest = {},
            deliveryCompanyName = "",
            deliveryTrackingNumber = "",
            receiptData = fakeReceiptData
        )
    }
}