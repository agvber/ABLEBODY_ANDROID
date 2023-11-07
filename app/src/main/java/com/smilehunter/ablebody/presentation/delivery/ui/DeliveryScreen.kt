package com.smilehunter.ablebody.presentation.delivery.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.delivery.DeliveryViewModel
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.CustomButton
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.launch

@Composable
fun DeliveryRoute(
    onBackRequest: () -> Unit,
    postCodeRequest: () -> Unit,
    customName: String,
    phoneNumber: String,
    roadAddress: String,
    roadDetailAddress: String,
    zipCode: String,
    requestMessage: String,
    deliveryViewModel: DeliveryViewModel = hiltViewModel(),
) {

    DeliveryScreen(
        onBackRequest = onBackRequest,
        postCodeRequest = postCodeRequest,
        onSaveRequest = { address ->
            deliveryViewModel.addAddress(
                attentionName = address.attentionName,
                phoneNumber = address.phoneNumber,
                roadAddress = address.roadAddress,
                roadDetailAddress = address.roadDetailAddress,
                zipCode = address.zipCode,
                deliveryRequestMessage = address.deliveryRequestMessage
            )
                        },
        customName = customName,
        phoneNumber = phoneNumber,
        roadAddress = roadAddress,
        roadDetailAddress = roadDetailAddress,
        zipCode = zipCode,
        requestMessage = requestMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeliveryScreen(
    onBackRequest: () -> Unit,
    postCodeRequest: () -> Unit,
    onSaveRequest: (Address) -> Unit,
    customName: String,
    phoneNumber: String,
    roadAddress: String,
    roadDetailAddress: String,
    zipCode: String,
    requestMessage: String,
) {
    var customNameValueState by rememberSaveable { mutableStateOf(customName) }
    var phoneNumberValueState by rememberSaveable { mutableStateOf(phoneNumber) }
    var deliveryDetailAddressValueState by rememberSaveable { mutableStateOf(roadDetailAddress) }
    var deliveryRequestMessageValueState by rememberSaveable { mutableStateOf(requestMessage) }

    val saveButtonEnable = customNameValueState.isNotBlank() && phoneNumberValueState.isNotBlank() && roadAddress.isNotBlank() && deliveryDetailAddressValueState.isNotBlank() && zipCode.isNotBlank()

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "배송지 등록",
            )
        },
        bottomBar = {
            CustomButton(
                text = "저장하기",
                enable = saveButtonEnable
            ) {
                val address = Address(
                    customNameValueState,
                    phoneNumberValueState,
                    roadAddress,
                    deliveryDetailAddressValueState,
                    zipCode,
                    deliveryRequestMessageValueState
                )
                onSaveRequest(address)
                onBackRequest()
            }
        }
    ) { paddingValue ->
        var showCouponBottomSheet by rememberSaveable { mutableStateOf(false) }

        if (showCouponBottomSheet) {
            DeliveryRequestMessageBottomSheet(
                onDismissRequest = { showCouponBottomSheet = false },
                itemOnClick = {
                    deliveryRequestMessageValueState = it
                    showCouponBottomSheet = false
                }
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValue)
        ) {
            TitleColumLayout(text = "받는분") {
                DeliveryTextField(
                    value = customNameValueState,
                    onValueChange = { customNameValueState = it }
                )
            }
            TitleColumLayout(text = "휴대폰 번호") {
                DeliveryTextField(
                    value = phoneNumberValueState,
                    onValueChange = { phoneNumberValueState = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            TitleColumLayout(text = "주소") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    DeliveryTextField(
                        value = roadAddress,
                        onValueChange = {  },
                        modifier = Modifier.weight(2f),
                        enabled = false
                    )
                    Button(
                        onClick = postCodeRequest,
                        shape = RectangleShape,
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AbleDeep
                        )
                    ) {
                        Text(
                            text = "주소 찾기",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFFFFFFFF),
                            )
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    DeliveryTextField(
                        value = deliveryDetailAddressValueState,
                        onValueChange = { deliveryDetailAddressValueState = it },
                        modifier = Modifier.weight(2f)
                    )
                    DeliveryTextField(
                        value = zipCode,
                        onValueChange = {  },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "우편번호",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                                    fontWeight = FontWeight(500),
                                    color = SmallTextGrey,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        enabled = false
                    )
                }
            }
            TitleColumLayout(text = "(선택) 배송 요청 사항") {
                DeliveryTextField(
                    value = deliveryRequestMessageValueState,
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
                    modifier = Modifier.nonReplyClickable { showCouponBottomSheet = true }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryRequestMessageBottomSheet(
    onDismissRequest: () -> Unit,
    itemOnClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
        listOf(
            "문 앞에 놔주세요.",
            "택배함에 놔주세요.",
            "경비실에 맡겨주세요.",
            "배송 전에 미리 연락 주세요."
        )
            .forEach {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 19.dp, start = 16.dp, end = 16.dp)
                        .nonReplyClickable {
                            itemOnClick(it)
                            animateToDismiss()
                        }
                )
            }
        Box(modifier = Modifier.height(50.dp))
    }
}

@Composable
private fun TitleColumLayout(
    text: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 16.dp),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                color = AbleLight,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        content()
    }
}

@Composable
fun DeliveryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    actionContent: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.5.dp, color = AbleLight, shape = RectangleShape)
        ,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
            fontWeight = FontWeight(500),
            color = Color.Black,
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        ),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation, 
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                it()
                if (placeholder != null && value.isEmpty()) {
                    placeholder()
                }
            }
            if (actionContent != null) {
                actionContent()
            }
        }
    }
}

private data class Address(
    val attentionName: String,
    val phoneNumber: String,
    val roadAddress: String,
    val roadDetailAddress: String,
    val zipCode: String,
    val deliveryRequestMessage: String
)

@Preview(showBackground = true)
@Composable
fun DeliveryTextFieldPreview() {
    ABLEBODY_AndroidTheme {
        DeliveryTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(
                    text = "상세주소",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF8C959E),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
        )
    }
}

@Preview
@Composable
fun DeliveryScreenPreview() {
    ABLEBODY_AndroidTheme {
        DeliveryScreen(
            onBackRequest = {  },
            postCodeRequest = {  },
            onSaveRequest = {},
            customName = "",
            phoneNumber = "",
            roadAddress = "",
            roadDetailAddress = "",
            zipCode = "",
            requestMessage = "",
        )
    }
}