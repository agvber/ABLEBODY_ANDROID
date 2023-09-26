package com.smilehunter.ablebody.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
@Composable
fun previewPlaceHolder(@DrawableRes id: Int) = if (LocalInspectionMode.current) {
    painterResource(id = id)
} else {
    null
}