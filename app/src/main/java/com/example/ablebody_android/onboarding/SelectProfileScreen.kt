package com.example.ablebody_android.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ablebody_android.Gender
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.data.ProfileImages
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
fun SelectProfileImageLayout(
    value: ProfileImages?,
    onChangeValue: (ProfileImages) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        items(ProfileImages.values()) { profileImages ->
            val animateFloat by animateFloatAsState(
                targetValue = if (value == null || value == profileImages) 1f else .5f,
                animationSpec = tween(300)
            )

            Image(
                painter = painterResource(id = profileImages.resourcesID),
                contentDescription = profileImages.contentDescription,
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable(onClick = { onChangeValue(profileImages) }),
                alpha = animateFloat
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectProfileImageLayoutPreview() {
    SelectProfileImageLayout(ProfileImages.MAN_ONE) {}
}

@Composable
fun SelectProfileScreen(
    value: ProfileImages?,
    onChangeValue: (ProfileImages) -> Unit,
    bottomButtonOnClick: () -> Unit
) {
    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = bottomButtonOnClick,
        enable = value != null
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            SelectProfileTitleLayout()
            SelectProfileImageLayout(value = value, onChangeValue = onChangeValue)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SelectProfileScreenPreview() {
    var profileImageState by remember { mutableStateOf<ProfileImages?>(null) }
    SelectProfileScreen(
        value = profileImageState,
        onChangeValue = { profileImageState = it },
        bottomButtonOnClick = { }
    )
}