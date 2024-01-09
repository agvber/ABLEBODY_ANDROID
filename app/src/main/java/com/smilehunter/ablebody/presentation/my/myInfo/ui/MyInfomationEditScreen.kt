package com.smilehunter.ablebody.presentation.my.myInfo.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.presentation.home.my.data.NicknameCheckUiState
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoEditViewModel
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoViewModel
import com.smilehunter.ablebody.presentation.onboarding.data.NicknameRule
import com.smilehunter.ablebody.presentation.onboarding.ui.InputPhoneNumberWithRuleLayout
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.TextFieldUnderNormalText
import com.smilehunter.ablebody.ui.utils.TextFieldUnderText
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun MyInfoEditScreenRoute(
    myInfoEditViewModel: MyInfoEditViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    onPositiveBtnClick: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        myInfoEditViewModel.getMyInfoData()
    }
    val userInfoData by myInfoEditViewModel.userLiveData.observeAsState()
    val nickname by myInfoEditViewModel.nicknameState.collectAsStateWithLifecycle()
    val nicknameRuleState by myInfoEditViewModel.nicknameRuleState.collectAsStateWithLifecycle()

    MyInfomationEditScreen(
        onBackRequest = onBackRequest,
        phoneNumber = userInfoData?.phoneNumber ?: "",
        nickname = userInfoData?.nickname ?: "",
        gender = when (userInfoData?.gender) {
            Gender.MALE -> "남자"
            Gender.FEMALE -> "여자"
            else -> ""
        },
        uid = "#" + userInfoData?.uid,
        onNicknameChange = {
            Log.d("닉네임 콜백", it)
            myInfoEditViewModel.updateNickname(it)
        },
        onPositiveBtnClick = onPositiveBtnClick,
        nicknameRuleState = nicknameRuleState.toString(),
        onChangeBtnClick = {
            Log.d("확인 버튼 누른 후 닉네임 확인", nickname)
            myInfoEditViewModel.requestChangeNickname()
        }
    )
}

@Composable
fun MyInfomationEditScreen(
    onBackRequest: () -> Unit,
    phoneNumber: String,
    nickname: String,
    gender: String,
    uid: String,
    onNicknameChange: (String) -> Unit, // 닉네임 변경 콜백
    onPositiveBtnClick: () -> Unit,
    nicknameRuleState: String = "",
    onChangeBtnClick: () -> Unit
) {
    var PhoneNumberChangePopup by remember { mutableStateOf(false) }
    var editSavePopup by remember { mutableStateOf(false) }
    var isNicknameValid  by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "내 정보 수정",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column {
                MyInfoEditTextField(
                    editCategory = "휴대폰 번호(아이디)",
                    contentText = phoneNumber,
                    onTextChange = {},
                    onClick = {
                        PhoneNumberChangePopup = true },
                    "",
                    isEnableBtn = {}
                )
                MyInfoEditTextField(
                    editCategory = "닉네임",
                    contentText = nickname,
                    onTextChange = onNicknameChange, // 닉네임 변경 처리,
                    nicknameRuleState = nicknameRuleState,
                    isEnableBtn = { isValid -> isNicknameValid = isValid }
                )
                MyInfoEditTextField("성별", contentText = gender, onTextChange = {},{},"", isEnableBtn = {})
                MyInfoEditTextField("회원코드", contentText = uid, onTextChange = {},{},"", isEnableBtn = {})
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            editSavePopup = true
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
                        enabled = isNicknameValid
                    ) {
                        Text(
                            text = "확인",
                            color = Color.White
                        )
                    }
                }
            }
        }
        if (editSavePopup) {
            EditSavePopup( onBackRequest = onBackRequest, {editSavePopup = false}, onChangeBtnClick= onChangeBtnClick)
        }
        if (PhoneNumberChangePopup) {
            PhoneNumberChangePopup(
                onDismiss = { PhoneNumberChangePopup = false },
                onPositiveBtnClick = { onPositiveBtnClick() }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInfoEditTextField(
    editCategory: String,
    contentText: String,
    onTextChange: (String) -> Unit,
    onClick: () -> Unit = {},
    nicknameRuleState: String,
    isEnableBtn: (Boolean) -> Unit
) {
    var inputText by remember {
        mutableStateOf(contentText)
    }

    LaunchedEffect(contentText){
        inputText = contentText
    }

    val isPhoneField = editCategory == "휴대폰 번호(아이디)"
    val isNicknameField = editCategory == "닉네임"
    val textFieldModifier = if (isPhoneField) {
        Modifier.nonReplyClickable {
            onClick()
        }
    } else {
        Modifier
    }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp)
    ) {
        Text(
            text = editCategory,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            style = TextStyle(
                color = AbleDeep
            )
        )
        TextField(
            value = inputText,
            onValueChange = { newText ->
                Log.d("닉네임 수정2", newText)
                if (isNicknameField) {
                    inputText = newText
                    onTextChange(newText)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .then(textFieldModifier),
            textStyle = if(editCategory == "닉네임" || editCategory == "휴대폰 번호(아이디)") TextStyle(color = Color.Black, fontSize = 17.sp) else TextStyle(color = SmallTextGrey, fontSize = 17.sp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = PlaneGrey,
                disabledIndicatorColor = Color.Transparent
            ),
            enabled = editCategory == "닉네임"
        )
        if(editCategory == "닉네임"){
            when(nicknameRuleState){
                NicknameRule.Available.name -> {
                    TextFieldUnderNormalText(NicknameRule.Available.description, NicknameRule.Available.positive)
                    isEnableBtn(true)
                }
                NicknameRule.InUsed.name -> {
                    TextFieldUnderNormalText(NicknameRule.InUsed.description, NicknameRule.InUsed.positive)
                    isEnableBtn(false)
                }
                NicknameRule.StartsWithDot.name -> {
                    TextFieldUnderNormalText(NicknameRule.StartsWithDot.description, NicknameRule.StartsWithDot.positive)
                    isEnableBtn(false)
                }
                NicknameRule.OnlyNumber.name -> {
                    TextFieldUnderNormalText(NicknameRule.OnlyNumber.description, NicknameRule.OnlyNumber.positive)
                    isEnableBtn(false)
                }
                NicknameRule.UnAvailable.name -> {
                    TextFieldUnderNormalText(NicknameRule.UnAvailable.description, NicknameRule.UnAvailable.positive)
                    isEnableBtn(false)
                }
                NicknameRule.Nothing.name -> isEnableBtn(false)
            }
        }
    }
}


@Composable
fun PhoneNumberChangePopup(
    onDismiss: () -> Unit,
    onPositiveBtnClick: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "예",
        positiveButtonOnClick = {
            onPositiveBtnClick()
                                    /*onBackRequest()*/
                                },
        negativeText = "아니오",
        negativeButtonOnClick = { onDismiss() },
    ) {
        androidx.compose.material.Text(
            text = "휴대폰 번호를 바꾸려면 인증번호가\n" +
                    "필요해요.\n",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun EditSavePopup(
    onBackRequest: () -> Unit,
    onDismiss: () -> Unit,
    onChangeBtnClick: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "예",
        positiveButtonOnClick = {
            onChangeBtnClick()
            Log.d("팝업","확인 버튼 후 예 버튼 눌림")
            onBackRequest()
                                },
        negativeText = "아니오",
        negativeButtonOnClick = {
            Log.d("팝업","확인 버튼 후 아니오 버튼 눌림")
            onDismiss()
        },
    ) {
        androidx.compose.material.Text(
            text = "저장할까요?",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        androidx.compose.material.Text(
            text = "바꾼 정보를 저장합니다.",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )
    }
}
@Preview(showSystemUi = true)
@Composable
fun MyInfomationEditScreenPreview() {
    MyInfomationEditScreen({}, "01012345678", "닉네임", "남자", "000000", {}, {}, "사용 가능한 닉네임이에요.", {})
}

@Preview(showSystemUi = true)
@Composable
fun PhoneNumberSavePopupPreview(){
    PhoneNumberChangePopup({},{})
}