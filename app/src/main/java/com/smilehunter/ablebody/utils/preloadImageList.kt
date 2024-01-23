package com.smilehunter.ablebody.utils

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest

fun preloadImageList(context: Context, urls: List<String>) {
    urls.forEach {
        val imageRequest = ImageRequest.Builder(context = context)
            .data(it)
            .build()
        context.imageLoader.enqueue(imageRequest)
    }
}