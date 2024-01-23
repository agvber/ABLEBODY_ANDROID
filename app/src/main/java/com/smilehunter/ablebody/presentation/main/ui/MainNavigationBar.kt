package com.smilehunter.ablebody.presentation.main.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.presentation.main.data.NavigationItems
import com.smilehunter.ablebody.presentation.main.ui.utils.AbleBodyBottomNavigationItem
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.White

@Composable
fun MainNavigationBar(
    selected: NavigationItems,
    onChangeValue: (NavigationItems) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
        ,
        backgroundColor = White
    ) {
        NavigationItems.values().forEach { item ->
            val animateColor by animateColorAsState(
                targetValue = if (selected == item) AbleDark else AbleLight
            )

            AbleBodyBottomNavigationItem(
                selected = item == selected,
                onClick = { onChangeValue(item) },
                icon = {
                    Icon(
                        painter = painterResource(item.imageEnableResourceID),
                        contentDescription = null,
                        tint = animateColor
                    )
                },
                label = {
                    Text(
                        text = item.labelText,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight(500),
                            color = animateColor,
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                alwaysShowLabel = true,
                modifier = Modifier
                    .height(72.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }
    }
}

@Preview()
@Composable
fun MainNavigationBarPreview() {
    MainNavigationBar(selected = NavigationItems.Brand, onChangeValue = { })
}