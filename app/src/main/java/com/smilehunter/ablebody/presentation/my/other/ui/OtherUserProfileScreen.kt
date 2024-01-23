package com.smilehunter.ablebody.presentation.my.other.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.UserInfoData
import com.smilehunter.ablebody.presentation.my.myprofile.MyProfileViewModel
import com.smilehunter.ablebody.presentation.my.myprofile.ui.MySportswearCodyButton
import com.smilehunter.ablebody.presentation.my.myprofile.ui.makeUserDescription
import com.smilehunter.ablebody.presentation.my.other.OtherUserViewModel
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun OtherNormalUserRoute(
    otherUserViewModel: OtherUserViewModel = hiltViewModel(),
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    onReport: (String) -> Unit,
    uid: String
) {

    val otherUserData by otherUserViewModel.otherUserLiveData.observeAsState()
    Log.d("otherUserData", otherUserData.toString())
    val otherUserProfile = otherUserViewModel.otherUserProfile(uid)
//    Log.d("받는 다른 유저 프로필", otherUserProfile.toString())
    val otherUserBoard = otherUserViewModel.otherUserBoard.collectAsLazyPagingItems()
    Log.d("otherUserBoard", otherUserBoard.toString())
    val errorData by otherUserViewModel.sendErrorLiveData.observeAsState()

    LaunchedEffect(key1 = true) {
        otherUserViewModel.getData()
    }

    SimpleErrorHandler(
        refreshRequest = { otherUserViewModel.getData() },
        onErrorOccur = onErrorOccur,
        isError = errorData != null,
        throwable = errorData
    )

    OtherUserScreen(
        onBackRequest = onBackRequest,
        nickname = otherUserData?.nickname ?: "",
        isCreator = otherUserData?.userType == UserInfoData.UserType.CREATOR,
        userName = otherUserData?.name ?: "",
        profileImage = otherUserData?.profileUrl ?: "",
        height = otherUserData?.height,
        weight = otherUserData?.weight,
        job = otherUserData?.job ?: "",
        introduction = otherUserData?.introduction ?: "",
        onReport = { onReport(uid) },
        uid = uid
    )

}

@Composable
fun OtherUserScreen(
    otherUserViewModel: OtherUserViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    nickname: String,
    isCreator: Boolean,
    userName: String,
    profileImage: String,
    height: Int?,
    weight: Int?,
    job: String?,
    introduction: String?,
    onReport: (String) -> Unit,
    uid: String
) {
    var isReportBottomSheetVisible by remember { mutableStateOf(false) }
    val otherUserBoard = otherUserViewModel.otherUserBoard.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleContent = {
                    Text(
                        text = nickname,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        )
                    )

                    if (isCreator) {
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_creator_badge),
                            contentDescription = "profile",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            ) {
                IconButton(onClick = {
                    isReportBottomSheetVisible = true
                }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Info")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {
                    Column() {
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
                                val description = makeUserDescription(height, weight, job)
                                androidx.compose.material.Text(
                                    text = description,
                                    color = SmallTextGrey
                                )
                            }

                            Spacer(modifier = Modifier.size(2.dp))

                            androidx.compose.material.Text(text = "$introduction")
                        }

                        if (isCreator) {
                            MySportswearCodyButton()
                        }
                    }
                }

                items(
                    count = otherUserBoard.itemCount
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = otherUserBoard[it]?.imageURL)
                            .build(),
                        contentDescription = "Detailed image description",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                item(
                    span = { GridItemSpan(3) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                    )
                }
            }


            if (isReportBottomSheetVisible) {
                ReportBottomSheet(
                    onDismiss = { isReportBottomSheetVisible = false },
                    onReport = {
                        onReport(uid)
                        Log.d("onReport2", "onReport")
                    },
                    uid = uid
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    onDismiss: () -> Unit,
    onReport: (String) -> Unit,
    uid: String
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "신고",
                fontSize = 16.sp,
                color = AbleRed,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp, top = 20.dp, bottom = 12.dp)
                    .nonReplyClickable {
                        onReport(uid)
                        Log.d("onReport1", "onReport")
                    }
            )
            Spacer(modifier = Modifier.size(60.dp))
        }
    }
}
