package com.smilehunter.ablebody.presentation.notification.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.NotificationItemData
import com.smilehunter.ablebody.model.NotificationPassedTime
import com.smilehunter.ablebody.model.fake.fakeNotificationItemData
import com.smilehunter.ablebody.presentation.main.ui.LocalNetworkConnectState
import com.smilehunter.ablebody.presentation.main.ui.error_handling.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.presentation.notification.NotificationViewModel
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.LightShaded
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.theme.White
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.HighlightText
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun NotificationRoute(
    onBackRequest: () -> Unit,
    itemClick: (String) -> Unit,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val notificationItemData = notificationViewModel.pagingItemList.collectAsLazyPagingItems()

    NotificationScreen(
        onBackRequest = onBackRequest,
        onAllCheckRequest = {
            notificationViewModel.allCheck()
                            },
        onCheckedRequest = {
            itemClick(it.uri)
            if (!it.checked) {
                notificationViewModel.itemCheck(it.id)
            }
                           },
        notificationItemData = notificationItemData
    )

    val isNetworkDisconnected = notificationItemData.loadState.refresh is LoadState.Error ||
            !LocalNetworkConnectState.current
    if (isNetworkDisconnected) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { notificationViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }
}

@Composable
fun NotificationScreen(
    onBackRequest: () -> Unit,
    onAllCheckRequest: () -> Unit,
    onCheckedRequest: (NotificationItemData.Content) -> Unit,
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
            items(
                count = notificationItemData.itemCount,
                key = notificationItemData.itemKey { it.id }
            ) { index ->
                NotificationContentItem(
                    itemClick = { notificationItemData[index]?.let { onCheckedRequest(it) } },
                    isChecked = notificationItemData[index]?.checked ?: true,
                    nickname = notificationItemData[index]?.senderNickname ?: "",
                    contentText = notificationItemData[index]?.text ?: "",
                    profileImageURL = notificationItemData[index]?.senderProfileImageURL ?: "",
                    passedTime = notificationItemData[index]?.passedTime ?: NotificationPassedTime.Minutes(0),
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}

@Composable
private fun NotificationContentItem(
    modifier: Modifier = Modifier,
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
        modifier = modifier
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