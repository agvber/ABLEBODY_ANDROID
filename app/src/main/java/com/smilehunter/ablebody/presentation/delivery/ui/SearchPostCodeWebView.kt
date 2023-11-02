package com.smilehunter.ablebody.presentation.delivery.ui

import android.webkit.JavascriptInterface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchPostCodeWebView(
    onFinished: (String, String) -> Unit
) {
    val webViewState = rememberWebViewState(url = "https://smilehunter-ablebody.github.io/ABLEBODY_ADDRESS_ANDROID/")
    val webViewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()

    WebView(
        state = webViewState,
        client = webViewClient,
        chromeClient = webChromeClient,
        navigator = rememberWebViewNavigator(),
        onCreated = { webView ->
            with(webView) {
                settings.run {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    addJavascriptInterface(WebAppInterface(onFinished = onFinished), "Android")
                    webViewClient.onPageFinished(webView, "javascript:execDaumPostcode();")
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

private class WebAppInterface(
    private val onFinished: (String, String) -> Unit
) {
    @JavascriptInterface
    fun postMessage(roadAddress: String, jibunAddress: String, zoneCode: String, userSelectedType: String) {
        CoroutineScope(Dispatchers.Main).launch {
            when (userSelectedType) { //R(도로명), J(지번)
                "R" -> onFinished(roadAddress, zoneCode)
                "J" -> onFinished(jibunAddress, zoneCode)
            }
        }
    }
}