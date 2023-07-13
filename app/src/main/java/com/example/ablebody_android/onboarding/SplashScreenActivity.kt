package com.example.ablebody_android.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.onboarding.utils.compose.LogoImage
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            SplashScreen(navController = rememberNavController())
            Navigation()
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen"){
        composable("splash_screen"){
            LogoImage()
            LaunchedEffect(key1 = true){
                delay(1000L)
                navController.navigate("main_screen")
            }
        }
        composable("main_screen"){
            IntroScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashPartLayoutPreview() {
    LogoImage()
}

