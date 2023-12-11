package com.smilehunter.ablebody.presentation.receipt.ui

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.smilehunter.ablebody.model.fake.fakeReceiptData
import com.smilehunter.ablebody.presentation.main.ui.LocalNetworkConnectState
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.presentation.order_management.ui.OrderItemLayout
import com.smilehunter.ablebody.presentation.receipt.ReceiptViewModel
import com.smilehunter.ablebody.presentation.receipt.data.ReceiptUiState
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.InactiveGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.TopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable
import retrofit2.HttpException
import java.text.NumberFormat

@Composable
fun ReceiptRoute(
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    orderComplete: () -> Unit,
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
) {
    val receipt by receiptViewModel.receiptData.collectAsStateWithLifecycle()
    ReceiptScreen(
        orderComplete = orderComplete,
        receiptData = receipt
    )

    val isNetworkDisconnected = receipt is ReceiptUiState.LoadFail || !LocalNetworkConnectState.current
    if (isNetworkDisconnected) {
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

    if (receipt is ReceiptUiState.LoadFail) {
        val throwable = (receipt as ReceiptUiState.LoadFail).t
        val httpException = throwable as? HttpException
        if (httpException?.code() == 404) {
            onErrorOccur(ErrorHandlerCode.NOT_FOUND_ERROR)
            return
        }
        if (httpException != null) {
            onErrorOccur(ErrorHandlerCode.INTERNAL_SERVER_ERROR)
            return
        }
        isNetworkDisConnectedDialogShow = true
    }

    if (receipt is ReceiptUiState.Receipt) {
        if (isNetworkDisConnectedDialogShow) {
            isNetworkDisConnectedDialogShow = false
        }
    }
}


@Composable
fun ReceiptScreen(
    orderComplete: () -> Unit,
    receiptData: ReceiptUiState
) {
    Scaffold(
        topBar = {
            TopBarLayout {
                Text(
                    text = "완료",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.nonReplyClickable { orderComplete() }
                )
            }
        }
    ) { paddingValue ->
        if (receiptData !is ReceiptUiState.Receipt) { return@Scaffold }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValue)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.check_switch_on),
                    contentDescription = "complete icon",
                    modifier = Modifier
                        .size(45.dp)
                        .padding(7.5.dp)
                )
                Text(
                    text = "주문이 완료되었습니다",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = Color.Black,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                Text(
                    text = "주문번호 : ${receiptData.data.orderID}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(500),
                        color = Color.Black,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
            Divider(thickness = 4.dp, color = InactiveGrey)
            TitleWithColumn(
                titleText = "주문 상품",
                titleTextModifier = Modifier.fillMaxWidth(),
            ) {
                OrderItemLayout(
                    brandName = receiptData.data.brandName,
                    productName = receiptData.data.itemName,
                    productPrice = receiptData.data.price,
                    productImageURL = receiptData.data.itemImageURL,
                    options = receiptData.data.itemOptionDetailList.map { it.itemOptionDetail }
                )
            }
            Divider(thickness = 4.dp, color = InactiveGrey)
            TitleWithColumn(titleText = "무통장 입금 계좌 정보",) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    mapOf(
                        "입금 은행" to "농협",
                        "예금주" to "조민재",
                        "계좌번호" to "352-2134-4360-73",
                        "입금기한" to receiptData.data.depositDeadline.run { "${monthValue}월 ${dayOfMonth}일까지" }
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
                            Toast.makeText(context, "계좌번호가 복사되었습니다.", Toast.LENGTH_SHORT).show()
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
                        "받는 분" to receiptData.data.receiverName,
                        "주소" to "${receiptData.data.roadAddress}\n${receiptData.data.roadAddressDetail}",
                        "연락처" to receiptData.data.phoneNumber,
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
                    "총 상품 금액" to receiptData.data.price,
                    "상품 할인" to receiptData.data.itemDiscount.unaryMinus(),
                    "쿠폰 할인" to receiptData.data.couponDiscount.unaryMinus(),
                    "포인트 할인" to receiptData.data.pointDiscount.unaryMinus(),
                    "배송비" to receiptData.data.deliveryPrice,
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
                        text = "${numberFormat.format(receiptData.data.amountOfPayment)}원",
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
    titleTextModifier: Modifier = Modifier.padding(bottom = 16.dp),
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
            modifier = titleTextModifier
        )
        content()
    }
}

@Preview(heightDp = 1100)
@Composable
fun ReceiptScreenPreview() {
    ABLEBODY_AndroidTheme {
        ReceiptScreen(
            orderComplete = {},
            receiptData = ReceiptUiState.Receipt(fakeReceiptData)
        )
    }
}