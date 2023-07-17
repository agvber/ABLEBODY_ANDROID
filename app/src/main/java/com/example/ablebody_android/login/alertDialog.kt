package com.example.ablebody_android.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.SelectGenderButton
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark

@Composable
fun alertDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = {
            HighlightText(
                string = "가입되지 않은 휴대폰 번호예요.\n새로 가입할까요?",
                colorStringList = listOf("휴대폰 번호(아이디)"),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 35.sp,
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black))
                )
            )
        },
        buttons = {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                // TODO: 버튼 스타일 왜 자꾸 됐다가 안됐다가 하는 거..?
                SignupButton("아니오",true)
                SignupButton("예",false)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun alertDialogPreview() {
    alertDialog()
}