package com.example.ablebody_android.brand.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ablebody_android.R
import com.example.ablebody_android.brand.data.GenderFilterType
import com.example.ablebody_android.brand.data.OrderFilterType
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey

@Composable
fun BrandFilterTab(
    genderFilter: GenderFilterType,
    genderFilterTabClicked: (GenderFilterType) -> Unit,
    orderFilter: OrderFilterType,
    orderFilterTabClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            modifier = Modifier.padding(start = 15.dp)
        ) {
            items(items = GenderFilterType.values()) { filterType ->
                val animateTextColor by animateColorAsState(
                    targetValue = if (genderFilter == filterType) AbleDark else SmallTextGrey,
                )
                val animateTextSize by animateIntAsState(
                    targetValue = if (genderFilter == filterType) 700 else 500
                )
                Text(
                    text = stringResource(id = filterType.stringResourceID),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(animateTextSize),
                        color = animateTextColor,
                    ),
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { genderFilterTabClicked(filterType) }
                        )
                )
            }
        }

        Row(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = orderFilterTabClicked,
            )
        ) {
            Text(
                text = stringResource(id = orderFilter.stringResourceID),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                ),
            )
            Image(
                painter = painterResource(id = R.drawable.chevrondown),
                contentDescription = "chevronDownButton",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandFilterTabPreview() {
    BrandFilterTab(
        genderFilter = GenderFilterType.ALL,
        genderFilterTabClicked = {  },
        orderFilter = OrderFilterType.Popularity,
        orderFilterTabClicked = {  }
    )
}

@Composable
fun BrandListItemLayout() {
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        val (brandImage, brandKoreanName, brandEnglishName, discountText, chevronButton) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.brand_test),
            contentDescription = "brandImage",
            modifier = Modifier
                .padding(end = 15.dp)
                .shadow(3.dp, shape = CircleShape)
                .constrainAs(ref = brandImage) {
                    parent.start
                },
        )

        Text(
            text = "제이엘브",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight(500),
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold))
            ),
            modifier = Modifier.constrainAs(brandKoreanName) {
                top.linkTo(parent.top)
                bottom.linkTo(brandEnglishName.top)
                absoluteLeft.linkTo(brandImage.absoluteRight)
            }
        )

        Text(
            text = "JELEVE",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
                color = SmallTextGrey,
            ),
            modifier = Modifier.constrainAs(brandEnglishName) {
                top.linkTo(brandKoreanName.bottom)
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(brandImage.absoluteRight)
            }
        )

        Text(
            text = "최대 51% 할인 중",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
                color = AbleBlue,
            ),
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .constrainAs(discountText) {
                    top.linkTo(brandKoreanName.bottom)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(brandEnglishName.absoluteRight)
                }
        )

        IconButton(
            onClick = {  },
            modifier = Modifier.constrainAs(ref = chevronButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.chevronforward),
                contentDescription = "chevronForwardButton",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandListItemLayoutPreview() {
    BrandListItemLayout()
}

@Composable
fun BrandScreen(modifier: Modifier = Modifier) {
    var isBrandFilterBottomSheetShow by remember { mutableStateOf(false) }
    var currentGenderFilterType by remember { mutableStateOf(GenderFilterType.ALL) }
    var currentOrderFilterType by remember { mutableStateOf(OrderFilterType.Popularity) }

    Column(modifier) {
        if (isBrandFilterBottomSheetShow) {
            BrandFilterBottomSheet(
                onDismissRequest = { orderFilterType ->
                    orderFilterType?.let { currentOrderFilterType = it }
                    isBrandFilterBottomSheetShow = false
                }
            )
        }
        BrandFilterTab(
            genderFilter = currentGenderFilterType,
            genderFilterTabClicked = { currentGenderFilterType = it },
            orderFilter = currentOrderFilterType,
            orderFilterTabClicked = { isBrandFilterBottomSheetShow = true }
        )
        LazyColumn {
            items(items = (0..10).toList()) {
                BrandListItemLayout()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandScreenPreview() {
    BrandScreen()
}