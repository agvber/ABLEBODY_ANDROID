package com.example.ablebody_android.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.PlaneGrey
import com.example.ablebody_android.ui.theme.SmallTextGrey

@Composable
fun ItemSearchBar(
    textFiledOnClick: () -> Unit,
    alertOnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        BasicTextField(
            value = "",
            onValueChange = {  },
            modifier = Modifier
                .size(32.dp)
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = textFiledOnClick
                ),
            enabled = false,
            singleLine = true,
            decorationBox = {
                Row(
                    modifier = Modifier
                        .background(color = PlaneGrey)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = "브랜드, 제품명을 검색해 보세요",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                        )
                    )
                }
            }
        )

        IconButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .align(Alignment.CenterVertically),
            onClick = { alertOnClick() },
        ) {
            Image(painter = painterResource(id = R.drawable.alertbell), contentDescription = "alertBell")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemSearchBarPreview() {
    ItemSearchBar(
        textFiledOnClick = {  },
        alertOnClick = {  }
    )
}