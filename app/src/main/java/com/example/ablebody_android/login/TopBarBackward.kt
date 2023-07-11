package com.example.ablebody_android.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ablebody_android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBackward(){
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(bottom = 130.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
//                    containerColor = Color.Blue
                ),
                title = {
//                    Text("NhKim", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {/*TODO backward 버튼 클릭*/}) {
//                        Icon(Icons.Filled.ArrowBack, tint=Color.White, contentDescription = "")
                        Image(
                            painter = painterResource(id = R.drawable.backward),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .width(19.11405.dp)
                                .height(16.dp)
                        )
                    }
                },
                //TopBar Menu
//                actions = {
//                    IconButton(onClick = {}) {
////                        Icon(imageVector = Icons.Filled.MoreVert, tint = AbleLight, contentDescription = "")
//                        Image(
//                            painter = painterResource(id = R.drawable.menu),
//                            contentDescription = "Logo",
//                            modifier = Modifier
//                                .width(3.77778.dp)
//                                .height(18.dp)
//                        )
//                    }
//                }
            )
        }
    ){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TopBarBackward()
}