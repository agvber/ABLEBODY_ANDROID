package com.smilehunter.ablebody.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

class KoreaMoneyFormatVisualTransformation: VisualTransformation {

    private val numberFormat = NumberFormat.getInstance(Locale.KOREA)

    override fun filter(text: AnnotatedString): TransformedText {
        val transferString = text.text.toTransfer()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = transferString.length
            override fun transformedToOriginal(offset: Int): Int = text.length
        }
        return TransformedText(
            text = AnnotatedString(transferString),
            offsetMapping = offsetMapping
        )
    }

    private fun String.toTransfer() = if (this.isNotEmpty()) {
        numberFormat.format(this.toLongOrNull() ?: 0)
    } else {
        this
    }
}