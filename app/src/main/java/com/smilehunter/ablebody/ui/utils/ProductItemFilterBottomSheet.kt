package com.smilehunter.ablebody.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItemFilterBottomSheet(
    onDismissRequest: (String?) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: LazyListScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest(null) },
        sheetState = sheetState,
        containerColor = White,
        dragHandle = null,
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                content = content
            )
        }
    )
}

@Composable
private fun ProductItemFilterBottomSheetContent(
    valueList: List<String>,
    clicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        items(items = valueList) { value ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { clicked(value) }
                    ),
                color = White
            ) {
                Text(
                    text = value,
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

@Preview(showBackground = true)
@Composable
private fun ProductItemFilterBottomSheetContentPreview() {
    ABLEBODY_AndroidTheme {
        ProductItemFilterBottomSheetContent(
            valueList = listOf("인기순", "이름순", "최신순"),
            clicked = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItemFilterBottomSheetItem(
    sheetState: SheetState,
    value: String,
    onValueChange: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    scope.launch {
                        if (sheetState.currentValue == SheetValue.Expanded && sheetState.hasPartiallyExpandedState) {
                            scope.launch { sheetState.partialExpand() }
                        } else { // Is expanded without collapsed state or is collapsed.
                            scope.launch { sheetState.hide() }.invokeOnCompletion { onValueChange() }
                        }
                    }
                }
            ),
        color = White
    ) {
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = AbleDark,
            )
        )
    }
}