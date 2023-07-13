package com.example.ablebody_android.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.utils.compose.CustomButton
import com.example.ablebody_android.ui.theme.AbleDark


@Composable
private fun TermsAgreementSheetContentItem(
    checked: Boolean,
    description: String
) {

    val checkedImageResourceID = if (checked) R.drawable.checked_big else R.drawable.unchecked_big

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
    ) {

        val (checkedImage, Text, chevronForward) = createRefs()

        Image(
            painter = painterResource(id = checkedImageResourceID),
            contentDescription = "Checked_Image",
            modifier = Modifier.constrainAs(ref = checkedImage) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(parent.absoluteLeft)
            }
        )

        Text(
            text = description,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .constrainAs(ref = Text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(checkedImage.absoluteRight)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.chevronforward),
            contentDescription = "Chevron_Forward",
            modifier = Modifier.constrainAs(ref = chevronForward) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsAgreementSheetContentItemPreview() {
    TermsAgreementSheetContentItem(checked = true, description = "서비스 이용약관 동의")
}


@Composable
private fun TermsAgreementSheetContent() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.check_switch_on),
                contentDescription = "image description",
                contentScale = ContentScale.None
            )

            Text(
                text = "약관에 모두 동의",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        TermsAgreementSheetContentItem(checked = true, description = "서비스 이용약관 동의")
        TermsAgreementSheetContentItem(checked = true, description = "개인정보 수집 및 이용 동의")
        TermsAgreementSheetContentItem(checked = true, description = "개인정보 제3자 제공 동의")
        TermsAgreementSheetContentItem(checked = true, description = "마케팅 정보 수신 동의(선택)")
        CustomButton(text = "확인", modifier = Modifier.padding(vertical = 50.dp)) {
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TermsAgreementSheetContentPreview() {
    TermsAgreementSheetContent()
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TermsAgreementScreen(
    sheetState: ModalBottomSheetState,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = { TermsAgreementSheetContent() },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp),
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true)
@Composable
fun TermsAgreementScreenPreview() {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    TermsAgreementScreen(sheetState = state) {}
}