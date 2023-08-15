package com.example.ablebody_android.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ABLEBODY_AndroidTheme {
                MainScreen()
            }
        }
    }
}