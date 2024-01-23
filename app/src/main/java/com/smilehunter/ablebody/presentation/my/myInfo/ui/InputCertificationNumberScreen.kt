package com.smilehunter.ablebody.presentation.my.myInfo.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoEditViewModel
import com.smilehunter.ablebody.presentation.onboarding.data.CertificationNumberInfoMessageUiState
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.utils.CustomLabelText
import com.smilehunter.ablebody.ui.utils.CustomTextField
import com.smilehunter.ablebody.ui.utils.HighlightText
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.ui.utils.TextFieldUnderText
import retrofit2.HttpException

@Composable
fun InputCertificationNumberRoute(
    myInfoEditViewModel: MyInfoEditViewModel = hiltViewModel(),
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onVerificationSuccess: () -> Unit
) {
    val phoneNumber by myInfoEditViewModel.phoneNumber.collectAsStateWithLifecycle()
    val certificationNumberState by myInfoEditViewModel.certificationNumberState.collectAsStateWithLifecycle()
    val certificationNumberInfoMessage by myInfoEditViewModel.certificationNumberInfoMessageUiState.collectAsStateWithLifecycle()
    Log.d("phoneNumber", phoneNumber.toString())
    val errorData by myInfoEditViewModel.sendErrorLiveData.observeAsState()

    val underTextValue = when (certificationNumberInfoMessage) {
        is CertificationNumberInfoMessageUiState.Timeout -> "인증번호가 만료됐어요 다시 전송해주세요."
        is CertificationNumberInfoMessageUiState.InValid -> "인증번호가 올바르지 않아요!"
        is CertificationNumberInfoMessageUiState.Success -> {
            onVerificationSuccess(); ""
        }

        is CertificationNumberInfoMessageUiState.Timer -> {
            (certificationNumberInfoMessage as CertificationNumberInfoMessageUiState.Timer).string
        }

        is CertificationNumberInfoMessageUiState.Already -> {
            ""
        }

        else -> {}
    }

    LaunchedEffect(key1 = true) {
        if (phoneNumber != null) {
            myInfoEditViewModel.startCertificationNumberTimer()
            myInfoEditViewModel.requestSmsVerificationCode(phoneNumber!!)
        }
    }

    InputCertificationNumberScreen(
        underTextValue = underTextValue.toString(),
        underTextIsPositive = certificationNumberInfoMessage is CertificationNumberInfoMessageUiState.Timer,
        value = certificationNumberState,
        onValueChange = { newNumber ->
            Log.d("newNumber", newNumber)
            //TODO newNumber와 인증번호 비교
            myInfoEditViewModel.updateCertificationNumber(newNumber)
        },
        certificationBtnOnClick = {
            if (phoneNumber != null) {
                Log.d("인증번호 다시 받기", "인증번호 다시 받기 버튼 눌림")
                myInfoEditViewModel.cancelCertificationNumberCountDownTimer()
                myInfoEditViewModel.startCertificationNumberTimer()
                myInfoEditViewModel.requestSmsVerificationCode(phoneNumber!!)
            }
        }
    )

    if (errorData != null){
        onErrorOccur(ErrorHandlerCode.INTERNAL_SERVER_ERROR)
    }
}

@Composable
fun InputCertificationNumberScreen(
    underTextValue: String,
    underTextIsPositive: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    certificationBtnOnClick: () -> Unit
) {
    var state by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(vertical = 100.dp, horizontal = 20.dp)
    ) {
        HighlightText(
            string = "문자로 전송된\n인증번호 4자리를 입력해주세요.",
            colorStringList = listOf("인증번호 4자리"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 35.sp,
                fontWeight = FontWeight(700),
                color = AbleDark,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black))
            )
        )
        CustomTextField(
            labelText = { CustomLabelText(text = "4자리 숫자") },
            value = value,
            onValueChange = {
                onValueChange(it)
                Log.d("onValueChange", it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextFieldUnderText(
            text = underTextValue,
            isPositive = underTextIsPositive
        )
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                Log.d("인증번호 받기 버튼 눌림", "인증번호 받기 버튼 눌림")
                certificationBtnOnClick()
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
        ) {
            Text(
                text = "인증번호 다시 받기",
                color = Color.White
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InputCertificationNumberScreenPreview() {
    var state by remember { mutableStateOf("") }
    InputCertificationNumberScreen(
        underTextValue = "2분 30초",
        underTextIsPositive = true,
        value = state,
        onValueChange = { state = it },
        certificationBtnOnClick = {}
    )

}