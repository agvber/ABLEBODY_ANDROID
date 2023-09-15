package com.example.ablebody_android.presentation.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.ablebody_android.presentation.onboarding.data.TermsAgreements
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.utils.CustomButton
import com.example.ablebody_android.utils.redirectToURL

@Composable
fun TermsAgreementSheetContentControlAllSwitches(
    checkedSwitchResourceID: Int,
    onCheckedChange: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Image(
            painter = painterResource(id = checkedSwitchResourceID),
            contentDescription = "Checked_Switch",
            contentScale = ContentScale.None,
            modifier = Modifier.clickable(
                interactionSource = MutableInteractionSource(), indication = null
            ) { onCheckedChange() }
        )

        Text(
            text = "약관에 모두 동의",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
                fontWeight = FontWeight(700),
                color = AbleDark,
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAgreementSheetContentControlAllSwitchesPreview() {
    val termsAgreements = remember { mutableStateListOf<TermsAgreements>() }

    val checkedSwitchResourceID = if (TermsAgreements.values().size == termsAgreements.size) {
        R.drawable.check_switch_on
    } else {
        R.drawable.check_switch_off
    }

    TermsAgreementSheetContentControlAllSwitches(
        checkedSwitchResourceID = checkedSwitchResourceID,
        onCheckedChange = {  }
    )
}
@Composable
private fun TermsAgreementSheetContentItem(
    description: String,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    val checkedImageResourceID = if (checked) R.drawable.checked_big else R.drawable.unchecked_big
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
    ) {
        val (checkedImage, Text, chevronForward) = createRefs()

        Image(
            painter = painterResource(id = checkedImageResourceID),
            contentDescription = "Checked_Image",
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { onCheckedChange() }
                .constrainAs(ref = checkedImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                }
        )

        Text(
            text = description,
            modifier = Modifier
                .width(300.dp)
                .padding(horizontal = 10.dp)
                .constrainAs(ref = Text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(checkedImage.absoluteRight)
                }
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { onCheckedChange() }
        )

        Image(
            painter = painterResource(id = R.drawable.chevronforward),
            contentDescription = "Chevron_Forward",
            modifier = Modifier
                .constrainAs(ref = chevronForward) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
                .width(30.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                ) {
                    if(description == "서비스 이용약관 동의")
                        redirectToURL(context, "service agreement")
                    else if(description == "개인정보 수집 및 이용 동의")
                        redirectToURL(context, "privacy policy")
                    else if(description == "개인정보 제3자 제공 동의")
                        redirectToURL(context, "thirdparty sharing consent")
                    else if(description == "마케팅 정보 수신 동의(선택)")
                        redirectToURL(context, "marketing information consent")
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsAgreementSheetContentItemPreview() {
    var state by remember { mutableStateOf(false) }

    TermsAgreementSheetContentItem(checked = state, description = "서비스 이용약관 동의") {
        state = !state
    }
}

@Composable
private fun TermsAgreementSheetContent(
    termsAgreements: MutableList<TermsAgreements>,
    allCheckSwitchOnCheckedChange: (Boolean) -> Unit,
    onCheckedChange: (TermsAgreements) -> Unit,
    bottomButtonOnClick: () -> Unit,
) {
    val termsArray = TermsAgreements.values()
    val check = termsArray.size == termsAgreements.size

    val checkedSwitchResourceID = if (check) {
        R.drawable.check_switch_on
    } else {
        R.drawable.check_switch_off
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TermsAgreementSheetContentControlAllSwitches(
            checkedSwitchResourceID = checkedSwitchResourceID,
            onCheckedChange = { allCheckSwitchOnCheckedChange(check) }
        )

        for (terms in termsArray) {
            TermsAgreementSheetContentItem(
                checked = termsAgreements.contains(terms),
                description = terms.description,
                onCheckedChange = { onCheckedChange(terms) }
            )
        }

        CustomButton(
            text = "확인",
            modifier = Modifier.padding(vertical = 50.dp),
            onClick = { bottomButtonOnClick() },
            enable = termsAgreements.containsAll(TermsAgreements.values().filter { it.isRequired })
            // compose에서는 filter를 사용하면 안좋다는 이야기가 있었음 추후에 수정 할 것
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAgreementSheetContentPreview() {
    val termsAgreementsState = remember { mutableStateListOf(TermsAgreements.ServiceAgreement) }

    TermsAgreementSheetContent(
        termsAgreements = termsAgreementsState,
        allCheckSwitchOnCheckedChange = {
            if (it) {
                termsAgreementsState.clear()
            } else {
                termsAgreementsState.clear()
                termsAgreementsState.addAll(TermsAgreements.values())
            }
        },
        onCheckedChange = {
            if (termsAgreementsState.contains(it)) {
                termsAgreementsState.remove(it)
            } else {
                termsAgreementsState.add(it)
            }
        }
    ) {  }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TermsAgreementScreen(
    termsAgreements: MutableList<TermsAgreements>,
    bottomButtonOnClick: () -> Unit,
    sheetState: ModalBottomSheetState,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = { TermsAgreementSheetContent(
            termsAgreements = termsAgreements,
            allCheckSwitchOnCheckedChange = {
                if (it) {
                    termsAgreements.clear()
                } else {
                    termsAgreements.clear()
                    termsAgreements.addAll(TermsAgreements.values())
                }
            },
            onCheckedChange = {
                if (termsAgreements.contains(it)) {
                    termsAgreements.remove(it)
                } else {
                    termsAgreements.add(it)
                }
                              },
            bottomButtonOnClick = bottomButtonOnClick
        ) },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp),
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true)
@Composable
fun TermsAgreementScreenPreview() {
    val termsAgreementsState = remember { mutableStateListOf<TermsAgreements>() }
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

    TermsAgreementScreen(
        termsAgreements = termsAgreementsState,
        bottomButtonOnClick = {  },
        sheetState = modalBottomSheetState,
    )  {}
}