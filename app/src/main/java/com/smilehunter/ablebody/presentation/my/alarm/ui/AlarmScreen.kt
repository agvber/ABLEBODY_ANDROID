package com.smilehunter.ablebody.presentation.my.alarm.ui

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.my.setting.ui.BenefitDescription
import com.smilehunter.ablebody.presentation.my.setting.ui.MarketingAlarmToggleButton
import com.smilehunter.ablebody.presentation.my.alarm.AlarmViewModel
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler

@Composable
fun AlarmRoute(
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    onErrorOccur: (ErrorHandlerCode) -> Unit
) {
    val getAlarmAgreeData by alarmViewModel.getUserAdConsentLiveData.observeAsState(false)
    val errorData by alarmViewModel.sendErrorLiveData.observeAsState()

    LaunchedEffect(key1 = true) {
        alarmViewModel.getAlarmData()
    }

    AlarmPage(
        onBackRequest = onBackRequest,
        getAlarmAgree = getAlarmAgreeData,
        passAlarmAgree =  {alarmViewModel.changeUserAdConsent(it)}
    )
    SimpleErrorHandler(
        refreshRequest = { alarmViewModel.getAlarmData() },
        onErrorOccur = onErrorOccur,
        isError = errorData != null,
        throwable = errorData
    )
}

@Composable
fun AlarmPage(
    onBackRequest: () -> Unit,
    getAlarmAgree: Boolean,
    passAlarmAgree: (Boolean) -> Unit,
) {
    var alarmAgreeStatus = getAlarmAgree
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
    Log.d("보여주는 알림 데이터", alarmAgreeStatus.toString()) //true <-> false

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "알림",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "혜택 · 마케팅 알림",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(500),
                        color = if (alarmAgreeStatus) Color.Black else SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.weight(1f)
                )
                MarketingAlarmToggleButton(buttonState = alarmAgreeStatus) { toggledState ->
                    Log.d("toggledState", toggledState.toString())
                    alarmAgreeStatus = toggledState
                    passAlarmAgree(toggledState)
                    if(toggledState){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                        }
                    }
                }
            }
            BenefitDescription(alarmAgree = alarmAgreeStatus)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AlarmPagePreview() {
    Column(){
//        AlarmPage({}, true)
        AlarmPage({}, true, {})
    }
}
