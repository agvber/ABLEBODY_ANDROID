package com.smilehunter.ablebody.presentation.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun OtherNormalUserScreen(
    viewModel: MyProfileViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    settingOnClick: () -> Unit
) {
    val userInfoData by viewModel.userLiveData.observeAsState()
    val couponData by viewModel.couponListLiveData.observeAsState()
    val orderItemData by viewModel.orderItemListLiveData.observeAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "알림",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
                if (userInfoData != null) {
                    NormalUserInfo(
                        isCreator = false,
                        userName = userInfoData?.name ?: "",
                        profileImage = userInfoData?.profileUrl ?: "",
                        nickName = userInfoData?.name ?: "",
                        weight = userInfoData?.weight ?: 0,
                        height = userInfoData?.height ?: 0,
                        job = userInfoData?.job ?: "직업",
                        introduction = userInfoData?.introduction ?: "소개글을 작성해주세요.",
                        orderManagement = orderItemData?.size ?: 0,
                        coupon = couponData?.size ?: 0,
                        creatorPoint = userInfoData?.creatorPoint ?: 0,
                        settingOnClick = settingOnClick
                    )
                }
            }

    }
}

//    LazyColumn(){
//
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLayout(
    onBackRequest: () -> Unit,
    nickname: String,
    isCreator: Boolean,
    userName: String,
    profileImage: String,
    weight: Int?,
    height: Int?,
    job: String?,
    introduction: String?
) {
    var isReportBottomSheetVisible by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = nickname,
                            style = TextStyle(
                                fontSize = 18.sp,
                            )
                        )

                        if (isCreator) {
                            Spacer(modifier = Modifier.size(5.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_creator_badge),
                                contentDescription = "profile"
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackRequest) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isReportBottomSheetVisible = true
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Info")
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val codeImageUrls = listOf<String>("https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg")
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = profileImage)
                        .placeholder(R.drawable.profile_man1)
                        .build(),
                    contentDescription = "user profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)  // use the calculated height
                )

                Spacer(modifier = Modifier.size(8.dp))

                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    androidx.compose.material.Text(
                        text = userName,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                            fontWeight = FontWeight(500),
                            textAlign = TextAlign.Right,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        ),
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Row() {
                        val description = makeUserDescription(weight, height, job)
                        androidx.compose.material.Text(
                            text = description,
                            color = SmallTextGrey
                        )
                    }

                    Spacer(modifier = Modifier.size(2.dp))

                    androidx.compose.material.Text(text = "$introduction")
                }

                if (isCreator) {
                    MySportswearCody(codeImageUrls)
                }
            }

            if (isReportBottomSheetVisible) {
                ReportBottomSheet(
                    onDismiss = { isReportBottomSheetVisible = false },
                    onReport = { /* Handle report action here */ }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    onDismiss: () -> Unit,
    onReport: () -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            // 바텀시트의 내용 및 동작 정의
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "신고",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // 추가적인 내용 및 동작 정의
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nonReplyClickable { onDismiss() }
        )
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun TopLayoutPreview() {
//    TopLayout({},"nickname")
//}

@Preview(showSystemUi = true)
@Composable
fun OtherNormalUserScreenPreview() {
    TopLayout({},"nickname",false,"피아노위의스팸", "", 70, 173, "개발자", "안녕하세요")
}

@Preview(showSystemUi = true)
@Composable
fun OtherCreatorScreenPreview() {
    TopLayout({},"nickname",true,"피아노위의스팸", "", 70, 173, "개발자", "안녕하세요")
}

@Preview
@Composable
fun OtherNormalUserScreenPreviewPreview() {
    OtherNormalUserScreenPreview()
}