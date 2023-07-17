package com.example.ablebody_android.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey


@Composable
fun SelectProfileTitleLayout() {
    Column {
        HighlightText(
            string = "마지막이에요! 프로필 사진을 골라주세요",
            colorStringList = listOf("프로필 사진"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 35.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(700),
                color = AbleDark,
            )
        )
        Text(
            text = "* 프로필 사진은 언제든지 바꿀 수 있어요",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 35.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectProfileTitleLayoutPreview() {
    SelectProfileTitleLayout()
}


@Composable
fun SelectProfileImageLayout() {
//    LazyHorizontalGrid(rows = GridCells.Adaptive(100.dp)) {
//        items()
//    }
    Column(
        modifier = Modifier.padding(top = 40.dp)
    ) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.profile_woman1),
                contentDescription = "1",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
            )
            Image(
                painter = painterResource(id = R.drawable.profile_man3),
                contentDescription = "2",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
            )
            Image(
                painter = painterResource(id = R.drawable.profile_woman2),
                contentDescription = "3",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
            )
        }
        Row(
//            modifier = Modifier.padding(23.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_man2),
                contentDescription = "4",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
            )
            Image(
                painter = painterResource(id = R.drawable.profile_man1),
                contentDescription = "5",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
            )
            Image(
                painter = painterResource(id = R.drawable.profile_woman3),
                contentDescription = "6",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectProfileImageLayoutPreview() {
    SelectProfileImageLayout()
}

@Composable
fun SelectProfileScreen() {
    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = {  },
        enable = false
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            SelectProfileTitleLayout()
            SelectProfileImageLayout()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SelectProfileScreenPreview() {
    SelectProfileScreen()
}