package com.example.ablebody_android.onboarding.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun checkPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}