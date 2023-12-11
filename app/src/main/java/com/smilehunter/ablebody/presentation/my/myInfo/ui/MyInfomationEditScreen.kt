package com.smilehunter.ablebody.presentation.my.myInfo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoEditViewModel
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout

@Composable
fun MyInfoEditScreenRoute(
    myInfoEditViewModel: MyInfoEditViewModel = hiltViewModel(),
    onBackRequest: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        myInfoEditViewModel.getMyInfoData()
    }
    val userInfoData by myInfoEditViewModel.userLiveData.observeAsState()

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
        uid = ("#" + userInfoData?.uid) ?: ""
    )
}

@Composable
fun MyInfomationEditScreen(
    onBackRequest: () -> Unit,
    phoneNumber: String,
    nickname: String,
    gender: String,
    uid: String
) {
    var editSavePopup by remember { mutableStateOf(false) }
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
                MyInfoEditTextField("휴대폰 번호(아이디)", contentText = phoneNumber)
                MyInfoEditTextField("닉네임", contentText = nickname)
                MyInfoEditTextField("성별", contentText = gender)
                MyInfoEditTextField("회원코드", contentText = uid)
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {
//                            suggestText(inputText)
                            editSavePopup = true
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
//                        enabled = isButtonEnabled // 버튼 활성화 여부를 설정
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
            EditSavePopup( onBackRequest = onBackRequest, {editSavePopup = false})
        }
    }
}

@Composable
fun PhoneNumberSavePopup(
    onBackRequest: () -> Unit,
    onDismiss: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "예",
        positiveButtonOnClick = { onBackRequest() },
        negativeText = "아니오",
        negativeButtonOnClick = { onDismiss() },
    ) {
        androidx.compose.material.Text(
            text = "휴대폰 번호를 바꾸려면 인증번호가\n" +
                    "필요해요.",
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

@Preview(showSystemUi = true)
@Composable
fun MyInfomationEditScreenPreview() {
    MyInfomationEditScreen({}, "01012345678", "닉네임", "남자", "000000")
}

@Preview(showSystemUi = true)
@Composable
fun PhoneNumberSavePopupPreview(){
    PhoneNumberSavePopup({},{})
}