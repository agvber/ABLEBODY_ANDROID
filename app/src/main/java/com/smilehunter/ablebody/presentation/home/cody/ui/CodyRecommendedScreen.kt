package com.smilehunter.ablebody.presentation.home.cody.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.PersonHeightFilterType
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.fake.fakeCodyItemData
import com.smilehunter.ablebody.presentation.home.cody.CodyRecommendViewModel
import com.smilehunter.ablebody.presentation.main.ui.error_handling.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.ui.cody_item.CodyItemListLayout
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.utils.ItemSearchBar
import kotlinx.coroutines.flow.flowOf

@Composable
fun CodyRecommendedRoute(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    itemClick: (Long) -> Unit,
    codyRecommendViewModel: CodyRecommendViewModel = hiltViewModel()
) {
    val codyItemListGenderFilterList by codyRecommendViewModel.codyItemListGenderFilter.collectAsStateWithLifecycle()
    val codyItemListSportFilter by codyRecommendViewModel.codyItemListSportFilter.collectAsStateWithLifecycle()
    val codyItemListPersonHeightFilter by codyRecommendViewModel.codyItemListPersonHeightFilter.collectAsStateWithLifecycle()
    val codyItemData = codyRecommendViewModel.codyPagingItem.collectAsLazyPagingItems()
    CodyRecommendedScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        itemClick = itemClick,
        resetRequest = { codyRecommendViewModel.resetCodyItemFilter() },
        onCodyItemListGenderFilterChange = { codyRecommendViewModel.updateCodyItemListGendersFilter(it) },
        onCodyItemListSportFilterChange = { codyRecommendViewModel.updateCodyItemListSportFilter(it) },
        onCodyItemListPersonHeightFilterChange = { codyRecommendViewModel.updateCodyItemListPersonHeightFilter(it) },
        codyItemListGenderFilterList = codyItemListGenderFilterList,
        codyItemListSportFilter = codyItemListSportFilter,
        codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
        codyItemData = codyItemData,
    )

    if (codyItemData.loadState.refresh is LoadState.Error) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { codyRecommendViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }
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