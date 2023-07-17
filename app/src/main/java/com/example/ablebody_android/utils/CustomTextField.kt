package com.example.ablebody_android.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.AbleLight
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White

// TODO: disable경우 포함
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = White,
            errorContainerColor = White,
            unfocusedContainerColor = White,
            focusedContainerColor = White,
            focusedIndicatorColor = AbleBlue,
        ),
        label = {
            Text(
                text = labelText,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = SmallTextGrey,
                )
            )
        },
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(700),
            color = AbleDark,
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = when {
                labelText == "닉네임" -> KeyboardType.Text
                else -> KeyboardType.Number
             }
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        value = "", labelText = "휴대폰 번호"
    ) {
    }
}

@Composable
fun CustomHintTextField(
    modifier: Modifier = Modifier,
    hintText: String,
    value: String,
    onValueChange: (String) -> Unit,
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                hintText,
                color = AbleLight,
                fontSize = 21.sp,
                fontWeight = FontWeight(400),
                //TODO : hint 글자가 앞에 딱 붙게 하기
//                textAlign = TextAlign.Left,
            )
        },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = White,
            errorContainerColor = White,
            unfocusedContainerColor = White,
            focusedContainerColor = White,
            focusedIndicatorColor = AbleBlue,
        ),
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(700),
            color = AbleDark,
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = when {
                hintText == "닉네임(20자 이내 영문,숫자,_,.가능)" -> KeyboardType.Text
                else -> KeyboardType.Number
            }
        )
    )
}


@Preview(showBackground = true)
@Composable
fun CustomHintTextFieldPreview1() {
    CustomHintTextField(
        value = "", hintText = "닉네임(20자 이내 영문,숫자,_,.가능)"
    ){}
}

@Preview(showBackground = true)
@Composable
fun CustomHintTextFieldPreview2() {
    CustomHintTextField(
        value = "", hintText = "휴대폰 번호"
    ){}
}