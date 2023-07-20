package com.example.ablebody_android.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun redirectToURL(context: Context, Destination: String) {
    var websiteUrl = ""
    if(Destination == "kakaotalk channel") {
        websiteUrl = "http://pf.kakao.com/_FQyQb" // Replace with the URL you want to open
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
    context.startActivity(intent)
}