package com.smilehunter.ablebody.presentation.home.brand.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.model.fake.fakeBrandListData
import com.smilehunter.ablebody.presentation.home.brand.BrandViewModel
import com.smilehunter.ablebody.presentation.home.brand.data.BrandListResultUiState
import com.smilehunter.ablebody.presentation.main.ui.LocalMainScaffoldPaddingValue
import com.smilehunter.ablebody.presentation.main.ui.LocalNetworkConnectState
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.DefaultFilterTabItem
import com.smilehunter.ablebody.ui.utils.DefaultFilterTabRow
import com.smilehunter.ablebody.ui.utils.DropDownFilterLayout
import com.smilehunter.ablebody.ui.utils.ItemSearchBar
import com.smilehunter.ablebody.ui.utils.ProductItemFilterBottomSheet
import com.smilehunter.ablebody.ui.utils.ProductItemFilterBottomSheetItem
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import kotlinx.coroutines.launch

@Composable
fun BrandRoute(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onItemClick: (Long, String) -> Unit,
    brandViewModel: BrandViewModel = hiltViewModel()
) {
    val sortingMethod by brandViewModel.brandListSortingMethod.collectAsStateWithLifecycle()
    val genderFilter by brandViewModel.brandListGenderFilterType.collectAsStateWithLifecycle()
    val brandItemList by brandViewModel.brandItemList.collectAsStateWithLifecycle()

    BrandScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        sortingMethod = sortingMethod,
        onSortingMethodChange = { brandViewModel.updateBrandListOrderFilterType(it) },
        genderFilter = genderFilter,
        onGenderFilterChange = { brandViewModel.updateBrandListGenderFilterType(it) },
        brandItemList = brandItemList,
        onItemClick = onItemClick
    )

    val isNetworkDisconnected = brandItemList is BrandListResultUiState.Error || !LocalNetworkConnectState.current
    if (isNetworkDisconnected) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { brandViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandScreen(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    sortingMethod: SortingMethod,
    onSortingMethodChange: (SortingMethod) -> Unit,
    genderFilter: ItemGender,
    onGenderFilterChange: (ItemGender) -> Unit,
    brandItemList: BrandListResultUiState,
    onItemClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFilterBottomSheetShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isFilterBottomSheetShow) {
            ProductItemFilterBottomSheet(onDismissRequest = { isFilterBottomSheetShow = false }) {
                items(items = SortingMethod.values()) { item ->
                    ProductItemFilterBottomSheetItem(
                        sheetState = sheetState,
                        value = item.string,
                        onValueChange = {
                            onSortingMethodChange(item)
                            scope.launch { lazyListState.animateScrollToItem(0) }
                            isFilterBottomSheetShow = false
                        }
                    )
                }
            }
        }
        Scaffold(
            modifier = modifier,
            topBar = {
                ItemSearchBar(
                    textFiledOnClick = onSearchBarClick,
                    alertOnClick = onAlertButtonClick
                )
            }
        ) { paddingValue ->
            Column(
                modifier = Modifier.padding(paddingValue)
            ) {
                BrandFilterTab(
                    genderFilter = genderFilter,
                    onGenderFilterChange = {
                        onGenderFilterChange(it)
                        scope.launch { lazyListState.animateScrollToItem(0) }
                    },
                    sortingMethod = sortingMethod,
                    onSortingMethodChange = { isFilterBottomSheetShow = true }
                )
                if (brandItemList is BrandListResultUiState.Success) {
                    LazyColumn(
                        state = lazyListState
                    ) {
                        items(
                            items = brandItemList.data,
                            key = { it.id }
                        ) {
                            BrandListItemLayout(
                                modifier = Modifier.animateItemPlacement(),
                                brandName = it.name,
                                subName = it.subName,
                                thumbnailURL = it.thumbnail,
                                maxDisCountString = it.maxDiscount,
                                onClick = { onItemClick(it.id, it.name) }
                            )
                            Divider(
                                thickness = 1.dp,
                                startIndent = 1.dp,
                                modifier = Modifier.height(1.dp)
                            )
                        }
                        item {
                            Box(modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BrandListItemLayout(
    modifier: Modifier = Modifier,
    brandName: String,
    subName: String,
    thumbnailURL: Any?,
    maxDisCountString: Int,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 15.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        AsyncImage(
            model = thumbnailURL,
            contentDescription = "brand image",
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp)
                .shadow(3.dp, shape = CircleShape)
                .size(48.dp)
            ,
            placeholder = previewPlaceHolder(id = R.drawable.brand_test)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = brandName,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = subName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        color = SmallTextGrey,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )
                if (maxDisCountString != 0) {
                    Text(
                        text = "최대 $maxDisCountString% 할인 중",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            color = AbleBlue,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.chevronforward),
            contentDescription = "chevronForwardButton",
            modifier = Modifier
        )
    }
}

@Composable
fun BrandFilterTab(
    sortingMethod: SortingMethod,
    onSortingMethodChange: () -> Unit,
    genderFilter: ItemGender,
    onGenderFilterChange: (ItemGender) -> Unit,
) {
    DefaultFilterTabRow(
        actionContent = {
            DropDownFilterLayout(
                value = sortingMethod.string,
                onClick = onSortingMethodChange
            )
        }
    ) {
        ItemGender.values().forEach {
            DefaultFilterTabItem(
                selected = genderFilter == it,
                text = it.string,
                onClick = { onGenderFilterChange(it) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandFilterTabPreview(
    sortingMethod: SortingMethod = SortingMethod.POPULAR,
    onSortingMethodChange: () -> Unit = {},
    genderFilter: ItemGender = ItemGender.UNISEX,
    onGenderFilterChange: (ItemGender) -> Unit = {},
) {
    BrandFilterTab(
        sortingMethod = sortingMethod,
        onSortingMethodChange = onSortingMethodChange,
        genderFilter = genderFilter,
        onGenderFilterChange = onGenderFilterChange,
    )
}

@Preview(showBackground = true)
@Composable
fun BrandListItemLayoutPreview() {
    BrandListItemLayout(
        brandName = "제이엘브",
        subName = "JELEVE",
        thumbnailURL = "https://",
        maxDisCountString = 51,
        onClick = {  }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BrandScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandScreen(
            onSearchBarClick = {},
            onAlertButtonClick = {},
            sortingMethod = SortingMethod.POPULAR,
            onSortingMethodChange = {},
            genderFilter = ItemGender.UNISEX,
            onGenderFilterChange = {},
            brandItemList = BrandListResultUiState.Success(fakeBrandListData),
            onItemClick = { id, name -> }
        )
    }
}