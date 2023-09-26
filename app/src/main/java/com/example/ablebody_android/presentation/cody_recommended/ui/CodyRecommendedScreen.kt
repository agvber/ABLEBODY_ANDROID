package com.example.ablebody_android.presentation.cody_recommended.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.PersonHeightFilterType
import com.example.ablebody_android.model.CodyItemData
import com.example.ablebody_android.model.fake.fakeCodyItemData
import com.example.ablebody_android.presentation.cody_recommended.CodyRecommendViewModel
import com.example.ablebody_android.ui.cody_item.CodyItemListLayout
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.ItemSearchBar
import kotlinx.coroutines.flow.flowOf

@Composable
fun CodyRecommendedRoute(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    itemClick: (Long) -> Unit,
    codyRecommendViewModel: CodyRecommendViewModel = hiltViewModel()
) {
    CodyRecommendedScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        itemClick = itemClick,
        resetRequest = { codyRecommendViewModel.resetCodyItemFilter() },
        onCodyItemListGenderFilterChange = { codyRecommendViewModel.updateCodyItemListGendersFilter(it) },
        onCodyItemListSportFilterChange = { codyRecommendViewModel.updateCodyItemListSportFilter(it) },
        onCodyItemListPersonHeightFilterChange = { codyRecommendViewModel.updateCodyItemListPersonHeightFilter(it) },
        codyItemListGenderFilterList = codyRecommendViewModel.codyItemListGenderFilter.collectAsStateWithLifecycle().value,
        codyItemListSportFilter = codyRecommendViewModel.codyItemListSportFilter.collectAsStateWithLifecycle().value,
        codyItemListPersonHeightFilter = codyRecommendViewModel.codyItemListPersonHeightFilter.collectAsStateWithLifecycle().value,
        codyItemData = codyRecommendViewModel.codyPagingItem.collectAsLazyPagingItems(),
    )
}


@Composable
fun CodyRecommendedScreen(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    itemClick: (Long) -> Unit,
    resetRequest: () -> Unit,
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit,
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit,
    modifier: Modifier = Modifier,
    codyItemListGenderFilterList: List<Gender>,
    codyItemListSportFilter: List<HomeCategory>,
    codyItemListPersonHeightFilter: PersonHeightFilterType,
    codyItemData: LazyPagingItems<CodyItemData.Item>
) {
    Scaffold(
        topBar = {
            ItemSearchBar(
                textFiledOnClick = onSearchBarClick,
                alertOnClick = onAlertButtonClick
            )
        }
    ) { paddingValue ->
        CodyItemListLayout(
            itemClick = itemClick,
            resetRequest = resetRequest,
            onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
            onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
            onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
            modifier = modifier.padding(paddingValue),
            codyItemListGenderFilterList = codyItemListGenderFilterList,
            codyItemListSportFilter = codyItemListSportFilter,
            codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
            codyItemData = codyItemData
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CodyRecommendedScreenPreview() {
    ABLEBODY_AndroidTheme {
        CodyRecommendedScreen(
            onSearchBarClick = {  },
            onAlertButtonClick = {  },
            itemClick = {},
            resetRequest = {  },
            onCodyItemListGenderFilterChange = {},
            onCodyItemListSportFilterChange = {},
            onCodyItemListPersonHeightFilterChange = {},
            codyItemListGenderFilterList = emptyList(),
            codyItemListSportFilter = emptyList(),
            codyItemListPersonHeightFilter = PersonHeightFilterType.ALL,
            codyItemData = flowOf(PagingData.from(fakeCodyItemData.content)).collectAsLazyPagingItems()
        )
    }
}