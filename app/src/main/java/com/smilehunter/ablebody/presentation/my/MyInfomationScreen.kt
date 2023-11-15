package com.smilehunter.ablebody.presentation.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout

@Composable
fun MyInfomationScreen(
    onBackRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "전체",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = "수정")
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(PlaneGrey)
        ){
            SettingList("휴대폰 번호(아이디)","", editText = "01092393487")
            SettingList("닉네임","", editText = "nahyi")
            SettingList("성별","", editText = "여자")
            SettingList("회원코드","", editText = "#8907255")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("탈퇴하기","", Color.Red)
        }
    }
}

@Composable
fun MyInfomationEditScreen(
    onBackRequest: () -> Unit
) {

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "내 정보 수정",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )},
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column {
                MyInfoEditTextField("휴대폰 번호(아이디)")
                MyInfoEditTextField("닉네임")
                MyInfoEditTextField("성별")
                MyInfoEditTextField("회원코드")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInfoEditTextField(
    editCategory: String
) {
    var inputText by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp)
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
            onValueChange = {
                inputText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = PlaneGrey
            )
        )
    }

}

@Preview
@Composable
fun MyInfoEditTextFieldPreview() {
    Column {
        MyInfoEditTextField("휴대폰 번호(아이디)")
    }
}
@Preview(showSystemUi = true)
@Composable
fun MyInfomationScreenPreview() {
    MyInfomationScreen({})
}

@Preview(showSystemUi = true)
@Composable
fun MyInfomationEditScreenPreview() {
    MyInfomationEditScreen({})
}