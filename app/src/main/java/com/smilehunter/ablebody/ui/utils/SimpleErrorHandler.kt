package com.smilehunter.ablebody.ui.utils

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import retrofit2.HttpException

@Composable
fun SimpleErrorHandler(
    refreshRequest: () -> Unit,
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    isError: Boolean,
    throwable: Throwable?
) {
    var isNetworkDisConnectedDialogShow by remember { mutableStateOf(false) }

    if (isNetworkDisConnectedDialogShow) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = refreshRequest,
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }

    if (isError) {
        val httpException = throwable as? HttpException
        if (httpException?.code() == 404) {
            onErrorOccur(ErrorHandlerCode.NOT_FOUND_ERROR)
            return
        }
        if (httpException != null) {
            onErrorOccur(ErrorHandlerCode.INTERNAL_SERVER_ERROR)
            return
        }
        isNetworkDisConnectedDialogShow = true
    } else {
        if (isNetworkDisConnectedDialogShow) {
            isNetworkDisConnectedDialogShow = false
        }
    }
}