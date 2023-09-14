package com.example.ablebody_android.network

internal fun<T> removeSquareBrackets(list: List<T>) =
    list.joinToString (",","","",-1)