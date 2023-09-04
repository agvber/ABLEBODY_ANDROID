package com.example.ablebody_android.brand.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ablebody_android.CodyItemFilterTabFilterType
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.CodyItemFilterTabLayout
import com.example.ablebody_android.utils.BackButtonTopBarLayout

@Composable
fun BrandCodyListScreen() {
    val codyFilterSelectList = remember { mutableStateListOf<CodyItemFilterTabFilterType>() }
    val scrollableState = rememberLazyGridState()
    val currentFirstVisibleItemIndex by remember { derivedStateOf { scrollableState.firstVisibleItemIndex } }
    var firstVisibleItemIndex by remember { mutableIntStateOf(0) }

    var isFilterTabShow by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = currentFirstVisibleItemIndex) {
        isFilterTabShow = firstVisibleItemIndex >= scrollableState.firstVisibleItemIndex
        firstVisibleItemIndex = scrollableState.firstVisibleItemIndex
    }


    Column {
        BackButtonTopBarLayout(
            onClick = { /*TODO*/ },
            titleText = "오옴"
        )
        AnimatedVisibility(visible = isFilterTabShow) {
            CodyItemFilterTabLayout(
                selectedItemList = codyFilterSelectList,
                onItemSelectChange = { codyFilterType, checked ->
                    if (checked) codyFilterSelectList.remove(codyFilterType) else codyFilterSelectList.add(codyFilterType)
                }
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = scrollableState,
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(items = (0..30).toList()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.detail_page_image_test)
                        .placeholder(R.drawable.detail_page_image_test)
                        .build(),
                    contentDescription = "cody recommended image",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandCodyListScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandCodyListScreen()
    }
}