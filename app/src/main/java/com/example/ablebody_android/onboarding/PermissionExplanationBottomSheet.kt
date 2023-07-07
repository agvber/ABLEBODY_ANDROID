package com.example.ablebody_android.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import com.example.ablebody_android.onboarding.utils.compose.CustomButton
import com.example.ablebody_android.ui.theme.AbleDark


@Composable
private fun PermissionExplanationItem(
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
                    lineHeight = 26.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF191E29),
                )
            )

            Text(
                text = description,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF191E29),
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionExplanationItemPreview() {
    PermissionExplanationItem(
        title = "알림",
        description = "운동약속을 등록하면 잊지 않게 알림을 받을 수 있어요.",
        painter = painterResource(id = R.drawable.alertbell)
    )
}

@Composable
private fun PermissionExplanationContent() {
    Column {
        Surface(modifier = Modifier.padding( horizontal = 16.dp)) {
            Text(
                text = "앱 사용을 위해 권한을 허용해주세요.\n꼭 필요한 권한만 받아요.",
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black))
                ),
            )
        }

        PermissionExplanationItem(
            title = "알림",
            description = "운동약속을 등록하면 잊지 않게 알림을 받을 수\n있어요.",
            painter = painterResource(id = R.drawable.alertbell)
        )

        PermissionExplanationItem(
            title = "사진",
            description = "사진 권한을 허용하면 운동생활 게시글을 작성할 때 더 쉽게 사진을 올릴 수 있어요.",
            painter = painterResource(id = R.drawable.photo)
        )

        CustomButton(
            text = "확인",
            onClick = { /* TODO: (온보딩/시작하기) bottom sheet 확인 클릭 이벤트 */ },
            modifier = Modifier.padding(vertical = 34.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionExplanationContentPreview() {
    PermissionExplanationContent()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionExplanationBottomSheet(
    scaffoldState: BottomSheetScaffoldState,
    content: @Composable (PaddingValues) -> Unit
) {

    BottomSheetScaffold(
        sheetContent = { PermissionExplanationContent() },
        sheetDragHandle = {  },
        scaffoldState = scaffoldState,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PermissionExplanationBottomSheetPreview() {
    val state = rememberBottomSheetScaffoldState(
        SheetState(
            skipPartiallyExpanded = true,
            initialValue = SheetValue.Expanded
        )
    )
    PermissionExplanationBottomSheet(state) {
        Surface(modifier = Modifier.padding(it)) {
            
        }
    }
}