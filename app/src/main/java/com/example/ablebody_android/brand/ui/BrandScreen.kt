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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import com.example.ablebody_android.R
import com.example.ablebody_android.brand.data.GenderFilterType
import com.example.ablebody_android.brand.data.OrderFilterType
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.DropDownFilterLayout

@Composable
fun BrandScreen(
    modifier: Modifier = Modifier
) {
    var isFilterBottomSheetShow by remember { mutableStateOf(false) }
    var genderFilterState by remember { mutableStateOf(GenderFilterType.ALL) }
    var orderFilterState by remember { mutableStateOf(OrderFilterType.Popularity) }
    val context = LocalContext.current

    if (isFilterBottomSheetShow) {
        val filterBottomSheetValueList by remember {
            derivedStateOf {
                OrderFilterType.values().map { context.getString(it.stringResourceID) }
            }
        }
        ProductItemFilterBottomSheet(
            valueList = filterBottomSheetValueList,
            onDismissRequest = { orderFilterType ->
                orderFilterType?.let { value ->
                    try {
                        orderFilterState = OrderFilterType.values()
                            .first { context.getString(it.stringResourceID) == value }
                    } catch (e: NoSuchElementException) {
                        // TODO: 브랜드 페이지 null
                    }
                }
                isFilterBottomSheetShow = false
            }
        )
    }

    Column(
        modifier = modifier
    ) {
        BrandFilterTab(
            genderFilter = genderFilterState,
            genderFilterTabClicked = { genderFilterState = it },
            orderFilter = orderFilterState,
            orderFilterTabClicked = { isFilterBottomSheetShow = true }
        )
        LazyColumn {
            items(items = (0..10).toList()) {
                BrandListItemLayout(
                    brandName = "제이엘브",
                    subName = "JELEVE",
                    thumbnailURL = "https://",
                    maxDisCountString = "최대 51% 할인 중",
                    onClick = {  }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandScreenPreview() {
    BrandScreen()
}

@Composable
fun BrandListItemLayout(
    brandName: String,
    subName: String,
    thumbnailURL: String,
    maxDisCountString: String?,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 15.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        val (brandImage, brandKoreanName, brandEnglishName, discountText, chevronButton) = createRefs()

        AsyncImage(
            model = thumbnailURL,
            contentDescription = "brand image",
            modifier = Modifier
                .padding(end = 15.dp)
                .shadow(3.dp, shape = CircleShape)
                .constrainAs(ref = brandImage) { parent.start },
            placeholder = painterResource(id = R.drawable.brand_test)
        )

        Text(
            text = brandName,
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
            text = subName,
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

        if (maxDisCountString != null) {
            Text(
                text = maxDisCountString,
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
        }

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
    BrandListItemLayout(
        brandName = "제이엘브",
        subName = "JELEVE",
        thumbnailURL = "https://",
        maxDisCountString = "최대 51% 할인 중",
        onClick = {  }
    )
}

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
        DropDownFilterLayout(
            value = stringResource(id = orderFilter.stringResourceID),
            onClick = orderFilterTabClicked
        )
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
