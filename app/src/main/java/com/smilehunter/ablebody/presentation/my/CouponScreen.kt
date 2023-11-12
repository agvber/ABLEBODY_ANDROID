package com.smilehunter.ablebody.presentation.my

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.LightShaded
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun CouponScreen(
    onBackRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "쿠폰",
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 15.dp),
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
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black,  // 글자 색을 검정색으로 설정합니다.
                ),
                shape = RoundedCornerShape(10.dp),  // 둥근 모서리를 25.dp로 설정합니다.
                border = BorderStroke(1.dp, Color.Black),  // 테두리를 검정색으로 설정합니다.
                modifier = Modifier
                    .padding(end = 10.dp)  // 가로 크기를 최대로 확장합니다.
                    .align(Alignment.End)  // 오른쪽으로 정렬합니다.
            ) {
                Text(text = "쿠폰 등록")
            }

            CouponContent(false, "선착순 100명 3,000원 할인 쿠폰", 55, 19, "3,000원 할인")
            CouponContent(true, "제이엘브 15% 할인", 0, 19, "15% 할인")
        }
    }
}

@Composable
fun CouponContent(
    isUsable: Boolean,
    couponTitle: String,
    couponCount: Int,
    expirationDateStr: Int,
    content: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 17.dp, start = 15.dp, end = 15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ){
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(15.dp)
                .fillMaxSize()
        ){
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = couponTitle,
                        style = TextStyle(
                            fontSize = 13.sp,
                            lineHeight = 26.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                    UsageStatusButton(isUsable)
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${couponCount}명 남음",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(500),
                            color = AbleBlue,
                            textAlign = TextAlign.Center,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "${expirationDateStr}일 남음",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(500),
                            color = AbleDark,
                            textAlign = TextAlign.Center,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                    )
                }

                Spacer(modifier = Modifier.size(23.dp))
                Text(
                    text = content,
                    style = TextStyle(
                        fontSize = 19.sp,
                        lineHeight = 26.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(700),
                        color = AbleBlue,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
            }
        }
    }
}


@Composable
fun UsageStatusButton(isUsable: Boolean) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isUsable) LightShaded else PlaneGrey
        ),
        modifier = Modifier
            .width(100.dp)
            .height(30.dp)
    ) {
        Text(
            text = if (isUsable) "사용가능" else "사용완료",
            fontSize = 11.sp,  // 글자 크기를 조절합니다.
            color = if (isUsable) AbleBlue else SmallTextGrey  ,
            modifier = Modifier
                .padding(horizontal = 8.dp)  // 좌우 여백을 조절합니다.
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CouponPreview() {
    CouponScreen({})
}

//@Preview()
//@Composable
//fun CardTestPreview() {
//    CouponContent(true, "제이엘브 15% 할인", 55, 19, "15%할인")
//}

@Composable
fun CouponRegisterScreen(
    onBackRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "쿠폰 등록",
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 15.dp),
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
        ) {
            Column() {
                Row(){
                    Text(text = "쿠폰 등록")
                    Icon(
                        painter = painterResource(id = R.drawable.information_icon), // 변환된 벡터 드로어블 리소스 ID
                        contentDescription = "Information Icon",
                        tint = SmallTextGrey,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .nonReplyClickable { }
                    )
                }
                Row(
                    modifier = Modifier
                        .height(50.dp)
                ){
                    var inputText by remember {
                        mutableStateOf("")
                    }

                    androidx.compose.material3.TextField(
                        value = inputText,
                        onValueChange = {
                            inputText = it
                        },
                        placeholder = { Text(text = "쿠폰 번호를 입력하세요") },
    //                    colors = TextFieldDefaults.textFieldColors(
    //                        containerColor = PlaneGrey,
    //                        focusedIndicatorColor = Color.Transparent,
    //                        unfocusedIndicatorColor = Color.Transparent
    //                    )
                    )

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White,
                        ),
                        modifier = Modifier
                            .padding(end = 10.dp)  // 가로 크기를 최대로 확장합니다.
                            .fillMaxHeight()
                    ) {
                        Text(text = "쿠폰 등록")
                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponNumberTextField() {
    var inputText by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .padding(20.dp)
            .border(1.dp, color = LightShaded)
    ){
        Row(
            modifier = Modifier.height(60.dp)
        ){
            androidx.compose.material3.TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                },
                modifier = Modifier.weight(10.5f),
                placeholder = { Text(text = "쿠폰 번호를 입력하세요") },
              colors = TextFieldDefaults.textFieldColors(
//                    textColor = Color.Black,  // 텍스트 색상 설정
//                    backgroundColor = Color.White,  // 배경 색상 설정
//                    cursorColor = Color.Black,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent
                )

            )

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
//                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .height(58.dp)
                    .weight(4.5f),
            ) {
                Text(
                    text = "쿠폰 등록",
                    style = TextStyle(color = Color.White)
                )

            }

        }


    }
}


@Preview(showSystemUi = true)
@Composable
fun CouponNumberTextFieldPreview() {
    CouponNumberTextField()
}
@Preview
@Composable
fun CouponRegisterScreenPreview() {
    CouponRegisterScreen({})
}