package com.smilehunter.ablebody.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.offset

fun Modifier.ignoreParentPadding(offsetPx: Int): Modifier =
    layout { measurable, constraints ->
        val looseConstraints = constraints.offset(offsetPx * 2, 0)
        val placeable = measurable.measure(looseConstraints)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }