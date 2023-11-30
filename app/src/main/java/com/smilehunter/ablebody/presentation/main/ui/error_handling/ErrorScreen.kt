package com.smilehunter.ablebody.presentation.main.ui.error_handling

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.utils.CustomButton

@Composable
fun ErrorScreen(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            CustomButton(
                text = buttonText,
                onClick = onClick
            )
        },
        modifier = modifier
    ) { paddingValue ->
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(75.dp)
            ) {
                content()

                Image(
                    painter = painterResource(id = R.drawable.ic_condition),
                    contentDescription = "error icon",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp)
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}