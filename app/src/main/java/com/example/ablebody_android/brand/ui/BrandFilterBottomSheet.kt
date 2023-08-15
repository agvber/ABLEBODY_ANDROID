package com.example.ablebody_android.brand.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.brand.data.OrderFilterType
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.White
import kotlinx.coroutines.launch

@Composable
private fun BrandFilterBottomSheetContent(
    onClick: (OrderFilterType) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        items(items = OrderFilterType.values()) { filterType ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onClick(filterType) }
                    ),
                color = White
            ) {
                Text(
                    text = stringResource(id = filterType.stringResourceID),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun BrandFilterBottomSheetContentPreview() {
    ABLEBODY_AndroidTheme {
        BrandFilterBottomSheetContent(onClick = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandFilterBottomSheet(
    onDismissRequest: (OrderFilterType?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest(null) },
        sheetState = sheetState,
        containerColor = White,
        dragHandle = null,
        content = {
            BrandFilterBottomSheetContent(
                onClick = { brandFilterType ->
                    scope.launch {
                        if (sheetState.currentValue == SheetValue.Expanded && sheetState.hasPartiallyExpandedState) {
                            scope.launch { sheetState.partialExpand() }
                        } else { // Is expanded without collapsed state or is collapsed.
                            scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissRequest(brandFilterType) }
                        }
                    }
                }
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BrandFilterBottomSheetPreview() {
    ABLEBODY_AndroidTheme {
        BrandFilterBottomSheet(onDismissRequest = {  })
    }
}