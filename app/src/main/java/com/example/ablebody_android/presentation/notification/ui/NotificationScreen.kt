package com.example.ablebody_android.presentation.notification.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.ablebody_android.R
import com.example.ablebody_android.model.NotificationItemData
import com.example.ablebody_android.model.NotificationPassedTime
import com.example.ablebody_android.model.fake.fakeNotificationItemData
import com.example.ablebody_android.presentation.notification.NotificationViewModel
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.LightShaded
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White
import com.example.ablebody_android.ui.utils.BackButtonTopBarLayout
import com.example.ablebody_android.ui.utils.HighlightText
import com.example.ablebody_android.ui.utils.previewPlaceHolder
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun NotificationRoute(
    onBackRequest: () -> Unit,
    itemClick: (Long) -> Unit,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val notificationItemData = notificationViewModel.pagingItemList.collectAsLazyPagingItems()

    NotificationScreen(
        onBackRequest = onBackRequest,
        onAllCheckRequest = {
            notificationViewModel.allCheck()
            notificationItemData.retry()
                            },
        onCheckedRequest = {
            notificationViewModel.itemCheck(it)
            itemClick(it)
//            notificationItemData.retry()
                           },
        notificationItemData = notificationItemData
    )
}

@Composable
fun NotificationScreen(
    onBackRequest: () -> Unit,
    onAllCheckRequest: () -> Unit,
    onCheckedRequest: (Long) -> Unit,
    notificationItemData: LazyPagingItems<NotificationItemData.Content>
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    onAllCheckRequest()
                                    scope.launch { lazyListState.scrollToItem(0) }
                                }
                            )
                    ) {
                        Text(
                            text = "모두 읽음",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(400),
                                color = AbleBlue
                            ),
                        )
                    }
                },
                titleText = "알림"
            )
        }
    ) { paddingValue ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.padding(paddingValue)
        ) {
            items(count = notificationItemData.itemCount) { index ->
                NotificationContentItem(
                    itemClick = {
                        notificationItemData[index]?.let { onCheckedRequest(it.id) }
                    },
                    isChecked = notificationItemData[index]?.checked ?: true,
                    nickname = notificationItemData[index]?.senderNickname ?: "",
                    contentText = notificationItemData[index]?.text ?: "",
                    profileImageURL = notificationItemData[index]?.senderProfileImageURL ?: "",
                    passedTime = notificationItemData[index]?.passedTime ?: NotificationPassedTime.Minutes(0)
                )
            }
        }
    }
}

@Composable
private fun NotificationContentItem(
    itemClick: () -> Unit,
    isChecked: Boolean,
    nickname: String,
    contentText: String,
    profileImageURL: String,
    passedTime: NotificationPassedTime
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) White else LightShaded
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { itemClick() }
            )
    ) {
        AsyncImage(
            model = profileImageURL,
            contentDescription = "user profile",
            modifier = Modifier
                .padding(top = 5.dp)
                .size(34.dp)
                .clip(shape = CircleShape),
            placeholder = previewPlaceHolder(id = R.drawable.profile_man1)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            HighlightText(
                string = contentText,
                applyStringList = listOf(nickname),
                applySpanStyle = SpanStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(700)
                ),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                )
            )
            Text(
                text = when (passedTime) {
                    is NotificationPassedTime.Year -> "${passedTime.year}년 전"
                    is NotificationPassedTime.Week -> "${passedTime.week}주 전"
                    is NotificationPassedTime.Month -> "${passedTime.month}개월 전"
                    is NotificationPassedTime.Day -> "${passedTime.day}일 전"
                    is NotificationPassedTime.Hour -> "${passedTime.hour}시간 전"
                    is NotificationPassedTime.Minutes -> "${passedTime.minutes}분 전"
                    is NotificationPassedTime.Second -> "${passedTime.second}초 전"
                },
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = SmallTextGrey,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationContentItemPreview() {
    NotificationContentItem(
        itemClick = {},
        isChecked = false,
        nickname = "nickname",
        contentText = "nickname님이 회원님의 댓글에 답글을 달았어요.",
        profileImageURL = "",
        passedTime = NotificationPassedTime.Minutes(2)
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    ABLEBODY_AndroidTheme {
        NotificationScreen(
            onBackRequest = {},
            onAllCheckRequest = {},
            onCheckedRequest = {},
            notificationItemData = flowOf(PagingData.from(fakeNotificationItemData.content)).collectAsLazyPagingItems()
        )
    }
}