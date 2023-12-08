package com.smilehunter.ablebody.presentation.my.myprofile.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.main.ui.LocalMainScaffoldPaddingValue
import com.smilehunter.ablebody.presentation.my.coupon.CouponViewModel
import com.smilehunter.ablebody.presentation.my.myprofile.MyProfileViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.LightShaded
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun MyProfileRoute(
    viewModel: MyProfileViewModel = hiltViewModel(),
    settingOnClick: () -> Unit,
    coupononClick: () -> Unit
) {
    val userInfoData by viewModel.userLiveData.observeAsState()

    Log.d("NormalUser or Creator?", userInfoData?.userType.toString())
    Log.d("NormalUser or Creator?", userInfoData.toString())

    if(userInfoData?.userType.toString() == "CREATOR"){
        Log.d("NormalUser or Creator?", "CreatorScreen")
        CreatorScreen(settingOnClick = settingOnClick, coupononClick = coupononClick)
    }else{
        Log.d("NormalUser or Creator?", "NormalUserScreen")
        NormalUserScreen(settingOnClick = settingOnClick, coupononClick = coupononClick)
    }

}

@Composable
fun CreatorScreen(
    myProfileViewModel: MyProfileViewModel = hiltViewModel(),
    couponViewModel: CouponViewModel = hiltViewModel(),
    settingOnClick: () -> Unit,
    coupononClick: () -> Unit
){
    val userBoard = myProfileViewModel.userBoard.collectAsLazyPagingItems()
    val userInfoData by myProfileViewModel.userLiveData.observeAsState()
    val couponData by couponViewModel.couponListLiveData.observeAsState()
    val orderItemData by myProfileViewModel.orderItemListLiveData.observeAsState()

    LaunchedEffect(key1 = true) {
        myProfileViewModel.getData()
        couponViewModel.getCouponData()
    }

    Box(
        modifier = Modifier.padding(15.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item(
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                Column() {
                    if (userInfoData != null) {
                        Log.d("CreatorUserInfo", "CreatorUserInfo에 들어옴")
                        CreatorUserInfo(
                            isCreator = true,
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
                            settingOnClick = settingOnClick,
                            coupononClick = coupononClick
                        )
                    }
                }
            }

            items(
                count = userBoard.itemCount
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = userBoard[it]?.imageURL)
                        .build(),
                    contentDescription = "Detailed image description",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 1.dp, vertical = 1.dp),
                    contentScale = ContentScale.Crop
                )
            }

            item(
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                Box(modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current))
            }
        }

        HomePostUploadButton(modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current))

    }
}
@Composable
fun NormalUserScreen(
    myProfileViewModel: MyProfileViewModel = hiltViewModel(),
    couponViewModel: CouponViewModel = hiltViewModel(),
    settingOnClick: () -> Unit,
    coupononClick: () -> Unit
){
    val userInfoData by myProfileViewModel.userLiveData.observeAsState()
    val couponData by couponViewModel.couponListLiveData.observeAsState()
    val orderItemData by myProfileViewModel.orderItemListLiveData.observeAsState()

    LaunchedEffect(key1 = true) {
        myProfileViewModel.getData()
        couponViewModel.getCouponData()
    }

    LazyColumn(){
        item{
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
                    settingOnClick = settingOnClick,
                    coupononClick = coupononClick
                )
            }
        }
    }
}

@Composable
fun NormalUserInfo(
    isCreator: Boolean,
    userName: String,
    profileImage: String,
    nickName: String,
    weight: Int?,
    height: Int?,
    job: String?,
    introduction: String?,
    orderManagement: Int,
    coupon: Int,
    creatorPoint: Int,
    settingOnClick: () -> Unit,
    coupononClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ){
        UserName(isCreator, userName, settingOnClick)
        UserInformation(profileImage, nickName, weight, height, job, introduction)
        OrderDetailBox(orderManagement, coupon, creatorPoint, coupononClick)
        profileEditButton()
        CreatorSupplyButton()
    }
}

@Composable
fun CreatorUserInfo(
    isCreator: Boolean,
    userName: String,
    profileImage: String,
    nickName: String,
    weight: Int?,
    height: Int?,
    job: String?,
    introduction: String?,
    orderManagement: Int,
    coupon: Int,
    creatorPoint: Int,
    settingOnClick: () -> Unit,
    coupononClick: () -> Unit
) {
    Column(){
        UserName(isCreator, userName, settingOnClick)
        UserInformation(profileImage, nickName, weight, height, job, introduction)
        OrderDetailBox(orderManagement, coupon, creatorPoint, coupononClick)
        profileEditButton()
        MySportswearCodyButton()
    }
}

@Composable
fun UserName(
    isCreator: Boolean,
    userName: String,
    settingOnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = userName,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Right,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
        )
        if(isCreator){
            Spacer(modifier = Modifier.size(5.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_creator_badge),
                contentDescription = "profile"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.Settings,
            contentDescription = "Expanded",
            modifier = Modifier
                .size(25.dp)
                .fillMaxWidth()
                .nonReplyClickable {
                    settingOnClick()
                },
            tint = AbleLight
        )

    }
}

@Composable
fun UserInformation(
    profileImage: String,
    userName: String,
    weight: Int?,
    height: Int?,
    job: String?,
    introduction: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
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

        Spacer(modifier = Modifier.width(16.dp)) // 예를 들면 16dp 간격을 줍니다.

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = userName,
//                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Right,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
            )
            Row(){
                val description = makeUserDescription(weight, height, job)
                Text(
                    text = description,
                    color = SmallTextGrey
                )
            }
            Text(text = "$introduction")
        }
    }
}

@Composable
fun OrderDetailBox(
    orderManagement: Int,
    coupon: Int,
    creatorPoint: Int,
    coupononClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(PlaneGrey)
    ){
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "주문/관리",
                    color = SmallTextGrey
                )
                Text(text = orderManagement.toString())
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .nonReplyClickable {
                        coupononClick()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "쿠폰",
                    color = SmallTextGrey
                )
                Text(text = coupon.toString())
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(text = "마일리지",
                        color = SmallTextGrey
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.information_icon), // 변환된 벡터 드로어블 리소스 ID
                        contentDescription = "Information Icon",
                        tint = SmallTextGrey,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .nonReplyClickable { showDialog = true }
                    )
                }
                Text(text = creatorPoint.toString()+"P")
            }
        }
    }
    if (showDialog) {
        CreatorMilegeDialog( {showDialog = false}  )
    }
}

@Composable
fun profileEditButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp, horizontal = 10.dp)
            .height(30.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = LightShaded),
        shape = RoundedCornerShape(8.dp), // 테두리를 둥글게 조정
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(3.dp)
    ) {
        Text(
            text = "프로필 편집",
            color = AbleBlue
        )
    }
}

@Composable
fun MySportswearCodyButton() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 16.dp, bottom = 12.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 8.dp
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
        ){
            Text(
                text = "내 운동복 코디",
                style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
            )
        }
    }
}

@Composable
fun CreatorSupplyButton() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        androidx.compose.material3.Button(
            onClick = {
                val url = "https://ubqotl23.paperform.co" // 여기에 원하는 링크 주소를 넣어주세요.
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(55.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AbleBlue),

            ) {
            Text(
                text = "\uD83D\uDCAA\uD83C\uDFFB 운동 크리에이터 지원하기",
                color = Color.White
            )
        }
    }
}

@Composable
fun HomePostUploadButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize()
    ){
        androidx.compose.material3.Button(
            onClick = {
                val url = "https://8vnzwllf.paperform.co"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(55.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AbleBlue),

            ) {
            Text(
                text = "\uD83D\uDCAA\uD83C\uDFFB 홈 게시물 업로드하기",
                color = Color.White
            )
        }

    }
}

//@Composable
//fun popupTest(
//    onDismiss: () -> Unit,
//    positiveText: String? = null,
//    positiveButtonOnClick: () -> Unit,
//    negativeText: String? = null,
//    negativeButtonOnClick: () -> Unit
//) {
//    Dialog(
//        onDismissRequest = onDismiss
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(size = 10.dp),
//            color = Color.White,
//            contentColor = Color.Black
//        ) {
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 15.dp)
//                    .padding(top = 20.dp, bottom = 15.dp)
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(5.dp)
//                ) {
//                    if (negativeText != null) {
//                        Button(
//                            onClick = negativeButtonOnClick,
//                            modifier = Modifier.weight(1f),
//                            isPositive = false,
//                            text = negativeText
//                        )
//                    }
//                    if (positiveText != null) {
//                        Button(
//                            onClick = positiveButtonOnClick,
//                            modifier = Modifier.weight(1f),
//                            isPositive = true,
//                            text = positiveText
//                        )
//                    }
//                }
//            }
//        }
//}

//@Composable
//fun ShowCreatorMileageInfoPopup(onDismiss: () -> Unit) {
//    var showDialog by remember { mutableStateOf(true) }
//
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = onDismiss,
//            title = {
//                Text(
//                    text = "📌 크리에이터 마일리지는 무엇인가요?",
//                    style = TextStyle(
//                        fontSize = 17.sp,
//                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
//                        fontWeight = FontWeight(500),
//                        platformStyle = PlatformTextStyle(includeFontPadding = false)
//                    ),
//                    modifier = Modifier.padding(top = 0.dp)
//                )
//            },
//            text = {
//                Text(
//                    text = "애블바디 크리에이터로서 운동복 코디 게시물을 1개 업로드할 때마다 200P 마일리지가 적립돼요. 이 마일리지는 결제 포인트로 사용할 수 있어요 🙂",
//                    modifier = Modifier.padding(0.dp)
//                )
//            },
//            buttons = {
//                Button(
//                    onClick = { showDialog = false },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = AbleBlue),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//                        .padding(10.dp),
//                    shape = RoundedCornerShape(10.dp),
//                ) {
//                    Text(text = "확인", color = Color.White)
//                }
//            },
//            shape = RoundedCornerShape(24.dp),
//            modifier = Modifier
//                .width(329.dp)
//                .height(201.dp)
//                .padding(0.dp)
//        )
//    }
//}

@Composable
fun CreatorMilegeDialog(
    onDismiss: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveButtonOnClick = { onDismiss() },
        negativeButtonOnClick = { },
        positiveText = "확인"
    ) {
        Text(
            text = "📌 크리에이터 마일리지는 무엇인가요?",
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(500),
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            text = "애블바디 크리에이터로서 운동복 코디 게시물을 1개 업로드할 때마다 200P 마일리지가 적립돼요. 이 마일리지는 결제 포인트로 사용할 수 있어요 🙂",
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreatorMilegeDialogPreview() {
    CreatorMilegeDialog({})
}


//@Preview(showBackground = true)
//@Composable
//fun ShowCreatorMileageInfoPopupPreview() {
//    ShowCreatorMileageInfoPopup({})
//}

fun makeUserDescription(height: Int?, weight: Int?, job: String?): String {
    val parts = listOfNotNull(height?.toString().plus("cm"), weight?.toString().plus("kg"), job)
    return parts.joinToString(separator = " · ")
}
@Preview(showSystemUi = true)
@Composable
fun NormalUserScreenPreview() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        UserName(false, "ablebody_1", {})
        UserInformation("", "일반유저", 70, 173, "개발자", "안녕하세요")
        OrderDetailBox(2,3,200, {})
        profileEditButton()
        CreatorSupplyButton()
    }
}
@Preview(showSystemUi = true)
@Composable
fun CreatorScreenPreview() {

    Box(modifier = Modifier.fillMaxSize()) { // Box는 전체 화면 크기를 차지합니다.
        Column(
            modifier = Modifier.fillMaxSize() // Column도 전체 화면 크기를 차지합니다.
        ) {
            UserName(true, "ablebody_2", {})
            UserInformation("", "크리에이터", 70, 173, "개발자", "안녕하세요")
            OrderDetailBox(2,1,100, {})
            profileEditButton()
            MySportswearCodyButton()
        }
        HomePostUploadButton()
    }
}

//@Composable
//fun CreatorScreen(
//    viewModel: MyProfileViewModel = hiltViewModel(),
//) {
//    val userBoard = viewModel.userBoard.collectAsLazyPagingItems()
//    val userInfoData by viewModel.userLiveData.observeAsState()
//    val couponData by viewModel.couponListLiveData.observeAsState()
//    val orderItemData by viewModel.orderItemListLiveData.observeAsState()
//
//    val codeImageUrls = listOf<String>("https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg", "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Home/1176/1839059%20bytes_1698899964898.jpg")
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        modifier = Modifier
//            .fillMaxWidth()
//    ){
//        items(count = userBoard.itemCount){
//            userBoard[it]?.isSingleImage
//            Log.d("LOG", userBoard[it]?.isSingleImage.toString())
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) { // Box는 전체 화면 크기를 차지합니다.
//        Column(
//            modifier = Modifier.fillMaxSize() // Column도 전체 화면 크기를 차지합니다.
//        ) {
//            UserName(true, "ablebody_2", {})
//            UserInformation("", "크리에이터", 70, 173, "개발자", "안녕하세요")
//            OrderDetailBox(2,1,100)
//            profileEditButton()
//            MySportswearCody(codeImageUrls)
//        }
//        HomePostUploadButton()
//    }
//}