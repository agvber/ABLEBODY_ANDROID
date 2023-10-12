package com.smilehunter.ablebody.presentation.like_list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.LikeListData
import com.smilehunter.ablebody.presentation.like_list.LikeListViewModel
import com.smilehunter.ablebody.presentation.main.ui.scaffoldPaddingValueCompositionLocal
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun LikeListRoute(
    onBackRequest: () -> Unit,
    profileRequest: (String) -> Unit,
    contentID: Long,
    likeListViewModel: LikeListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { likeListViewModel.updateContentID(contentID) }

    val likeList by likeListViewModel.likeList.collectAsStateWithLifecycle()
    LikeListScreen(
        onBackRequest = onBackRequest,
        profileImageOnClick = profileRequest,
        likeList = likeList
    )
}


@Composable
fun LikeListScreen(
    onBackRequest: () -> Unit,
    profileImageOnClick: (String) -> Unit,
    likeList: List<LikeListData>
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "공감하기"
            )
        }
    ) { paddingValue ->
        LazyColumn(modifier = Modifier.padding(paddingValue)) {
            items(
                items = likeList,
                key = { it.uid }
            ) {
                LikeListContentLayout(
                    profileImageOnClick = { profileImageOnClick(it.uid) },
                    nickname = it.nickname,
                    userName = it.userName,
                    profileImageURL = it.profileImageURL
                )
            }
            item { Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current)) }
        }
    }
}

@Composable
fun LikeListContentLayout(
    profileImageOnClick: () -> Unit,
    nickname: String,
    userName: String,
    profileImageURL: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row {
            AsyncImage(
                model = profileImageURL,
                contentDescription = "user profile image",
                placeholder = previewPlaceHolder(id = R.drawable.profile_woman1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .nonReplyClickable(profileImageOnClick)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = nickname,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    )
                )
                Text(
                    text = userName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LikeListContentLayoutPreview() {
    LikeListContentLayout(
        profileImageOnClick = {},
        nickname = "nickname",
        userName = "피아노위의스팸스팸스",
        profileImageURL = "" // placeHolder
    )
}

@Preview(showBackground = true)
@Composable
fun LikeListScreenPreview() {
    LikeListScreen(
        onBackRequest = {},
        profileImageOnClick = {},
        likeList = listOf(
            LikeListData(
                uid = "1",
                nickname = "nickname1",
                userName = "userName1",
                profileImageURL = ""
            ),
            LikeListData(
                uid = "2",
                nickname = "nickname2",
                userName = "userName2",
                profileImageURL = ""
            ),
        )
    )
}