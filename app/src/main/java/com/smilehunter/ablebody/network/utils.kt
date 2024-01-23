package com.smilehunter.ablebody.network

internal fun<T> removeSquareBrackets(list: List<T>) =
    list.joinToString (",","","",-1)