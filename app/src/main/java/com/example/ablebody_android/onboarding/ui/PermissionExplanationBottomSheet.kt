package com.example.ablebody_android.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.utils.CustomButton


@Composable
private fun PermissionExplanationItemLayout(
    title: String,
    description: String,
    painter: Painter,
) {
    Row(modifier = Modifier.padding(horizontal = 16.dp)) {

        Image(
            painter = painter,
            contentDescription = title,
            modifier = Modifier.padding(vertical = 14.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF191E29),
                )
            )

            Text(
                text = description,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF191E29),
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionExplanationItemLayoutPreview() {
    PermissionExplanationItemLayout(
        title = "알림",
        description = "운동약속을 등록하면 잊지 않게 알림을 받을 수 있어요.",
        painter = painterResource(id = R.drawable.alertbell)
    )
}

@Composable
private fun PermissionExplanationContentLayout(
    onClick: () -> Unit
) {
    Column {
        Surface(modifier = Modifier.padding( horizontal = 16.dp)) {
            Text(
                text = "앱 사용을 위해 권한을 허용해주세요.\n꼭 필요한 권한만 받아요.",
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black))
                ),
            )
        }

        PermissionExplanationItemLayout(
            title = "알림",
            description = "운동약속을 등록하면 잊지 않게 알림을 받을 수\n있어요.",
            painter = painterResource(id = R.drawable.alertbell)
        )

        PermissionExplanationItemLayout(
            title = "사진",
            description = "사진 권한을 허용하면 운동생활 게시글을 작성할 때 더 쉽게 사진을 올릴 수 있어요.",
            painter = painterResource(id = R.drawable.photo)
        )

        CustomButton(
            text = "확인",
            onClick = onClick,
            modifier = Modifier.padding(vertical = 34.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionExplanationContentLayoutPreview() {
    PermissionExplanationContentLayout() { }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PermissionExplanationBottomSheet(
    sheetState: ModalBottomSheetState,
    bottomButtonClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { PermissionExplanationContentLayout(bottomButtonClick) },
        content = content,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun PermissionExplanationBottomSheetPreview() {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    PermissionExplanationBottomSheet(state, {}) {}
}