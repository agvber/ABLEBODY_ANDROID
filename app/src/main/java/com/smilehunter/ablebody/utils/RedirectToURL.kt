package com.smilehunter.ablebody.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun redirectToURL(context: Context, Destination: String) {
    var websiteUrl = ""
    if(Destination == "kakaotalk channel") {
        websiteUrl = "http://pf.kakao.com/_FQyQb" // Replace with the URL you want to open
    }else if(Destination == "service agreement") {
        websiteUrl = "https://spiffy-vegetarian-7f4.notion.site/7cc3e1ce25284e45bfd9927e10e50d4c?pvs=4" // Replace with the URL you want to open
    }else if(Destination == "privacy policy") {
        websiteUrl = "https://spiffy-vegetarian-7f4.notion.site/b1f54c0fabfb4359bafcdf11bf6e2d1d?pvs=4" // Replace with the URL you want to open
    }else if(Destination == "thirdparty sharing consent") {
        websiteUrl = "https://spiffy-vegetarian-7f4.notion.site/3-aad37406127b4309aea46ce65e36c4da?pvs=4" // Replace with the URL you want to open
    }else if(Destination == "marketing information consent") {
        websiteUrl = "https://spiffy-vegetarian-7f4.notion.site/3ef674070cf945f79c35b80b3ee33909?pvs=4" // Replace with the URL you want to open
    }else if(Destination == "Personal Information Processing Policy") {
        websiteUrl = "https://spiffy-vegetarian-7f4.notion.site/38b4ac1baa874fbf8195784f7b4d1169"
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
    context.startActivity(intent)
}