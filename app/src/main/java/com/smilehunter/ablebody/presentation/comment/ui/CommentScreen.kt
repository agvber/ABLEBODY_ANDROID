package com.smilehunter.ablebody.presentation.comment.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.CommentListData
import com.smilehunter.ablebody.model.LikedLocations
import com.smilehunter.ablebody.model.LocalUserInfoData
import com.smilehunter.ablebody.presentation.comment.CommentViewModel
import com.smilehunter.ablebody.presentation.comment.data.CommentUiState
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.theme.White
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.HighlightText
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.CalculateUserElapsedTime
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.android.awaitFrame

@Composable
fun CommentRoute(
    onBackRequest: () -> Unit,
    onUserProfileVisitRequest: (String) -> Unit,
    likeUsersViewOnRequest: (Long, LikedLocations) -> Unit,
    commentViewModel: CommentViewModel = hiltViewModel()
) {
    val commentListData by commentViewModel.commentListData.collectAsStateWithLifecycle()
    val myUserInfoData by commentViewModel.myUserInfoData.collectAsStateWithLifecycle(initialValue = null)

    CommentScreen(
        onBackRequest = onBackRequest,
        onUserProfileVisitRequest = onUserProfileVisitRequest,
        likeUsersViewOnRequest = likeUsersViewOnRequest,
        onComment = { commentViewModel.updateCommentText(it) },
        onReply = { id, text -> commentViewModel.updateReplyText(id, text) },
        deleteComment = { commentViewModel.deleteComment(it) },
        deleteReply = commentViewModel::deleteReply,
        toggleLikeComment = commentViewModel::toggleLikeComment,
        toggleLikeReply = commentViewModel::toggleLikeReply,
        myUserInfoData = myUserInfoData,
        commentListData = commentListData
    )
}

@Composable
fun CommentScreen(
    onBackRequest: () -> Unit,
    onUserProfileVisitRequest: (String) -> Unit,
    likeUsersViewOnRequest: (Long, LikedLocations) -> Unit,
    onComment: (String) -> Unit,
    onReply: (Long, String) -> Unit,
    deleteComment: (Long) -> Unit,
    deleteReply: (Long) -> Unit,
    toggleLikeComment: (Long) -> Unit,
    toggleLikeReply: (Long) -> Unit,
    myUserInfoData: LocalUserInfoData?,
    commentListData: CommentUiState
) {
    val lazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember { FocusRequester() }
    var replyTargetNickname by remember { mutableStateOf<String?>(null) }
    var replyTargetID by remember { mutableLongStateOf(0) }

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "댓글"
            )
        },
        bottomBar = {
            if (commentListData is CommentUiState.CommentList) {
                var commentText by rememberSaveable { mutableStateOf("") }
                CommentTextFieldLayout(
                    onCommit = {
                        if (replyTargetNickname == null) {
                            onComment(commentText)
                        } else {
                            onReply(replyTargetID, commentText)
                            replyTargetNickname = null
                        }
                        focusManager.clearFocus()
                        commentText = ""
                    },
                    replyTargetNickname = replyTargetNickname,
                    onCancelReply = { replyTargetNickname = null },
                    value = commentText,
                    onValueChange = { commentText = it },
                    nickname = myUserInfoData?.nickname ?: "",
                    profileImageURL = myUserInfoData?.profileImageUrl ?: "",
                    modifier = Modifier.focusRequester(textFieldFocusRequester)
                )
            }
        }
    ) { paddingValue ->
        if (commentListData is CommentUiState.CommentList) {
            val context = LocalContext.current
            val toggleCommentLikeList = remember { mutableStateListOf<Long>() }
            val toggleReplyLikeList = remember { mutableStateListOf<Long>() }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.padding(paddingValue)
            ) {
                items(
                    items = commentListData.data,
                    key = { it.id }
                ) { items ->
                    val isMyComment = items.writer.uid == myUserInfoData?.uid
                    var isLiked by rememberSaveable { mutableStateOf(items.isLiked) }

                    CommentSwipeToDismissLayout(
                        dismissedToEndBackgroundColor = if (isMyComment) AbleRed else AbleLight,
                        dismissedToStartBackgroundColor = if (isMyComment) AbleRed else AbleLight,
                        dismissedToEnd = {
                            if (isMyComment) {
                                when (items.type) {
                                    CommentListData.CommentType.COMMENT -> deleteComment(items.id)
                                    CommentListData.CommentType.REPLY -> deleteReply(items.id)
                                }
                                true
                            } else {
                                Toast.makeText(context, "신고 해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                                false
                            }
                                         },
                        dismissedToStart = {
                            if (isMyComment) {
                                when (items.type) {
                                    CommentListData.CommentType.COMMENT -> deleteComment(items.id)
                                    CommentListData.CommentType.REPLY -> deleteReply(items.id)
                                }
                                true
                            } else {
                                Toast.makeText(context, "신고 해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                                false
                            }
                        },
                        dismissedToEndContent = {
                            if (isMyComment) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_bin_white),
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_warning_light),
                                    contentDescription = null,
                                    tint = White,
                                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                                )
                            }
                        },
                        dismissedToStartContent = {
                            if (isMyComment) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_bin_white),
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_warning_light),
                                    contentDescription = null,
                                    tint = White,
                                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                                )
                            }
                        }
                    ) {
                        when (items.type) {
                            CommentListData.CommentType.COMMENT -> {
                                CommentLayout(
                                    onReplyButtonClick = {
                                        replyTargetNickname = items.writer.nickname
                                        replyTargetID = items.id
                                    },
                                    onLikeRequest = {
                                        isLiked = !isLiked
                                        toggleCommentLikeList.addOrRemove(items.id)
                                                    },
                                    onUserProfileVisitRequest = { onUserProfileVisitRequest(items.writer.uid) },
                                    likeUsersViewOnRequest = { likeUsersViewOnRequest(items.id, LikedLocations.COMMENT) },
                                    isLiked = isLiked,
                                    nickname = items.writer.nickname,
                                    contentText = items.contents,
                                    passedTime = elapsedTimeToString(items.elapsedTime),
                                    likeCount = items.likeCount,
                                    profileImageURL = items.writer.profileUrl,
                                    modifier = Modifier.animateContentSize()
                                )
                            }
                            CommentListData.CommentType.REPLY -> {
                                ReplyLayout(
                                    onLikeRequest = {
                                        isLiked = !isLiked
                                        toggleReplyLikeList.addOrRemove(items.id)
                                                    },
                                    onUserProfileVisitRequest = { onUserProfileVisitRequest(items.writer.uid) },
                                    likeUsersViewOnRequest = { likeUsersViewOnRequest(items.id, LikedLocations.REPLAY) },
                                    isLiked = isLiked,
                                    nickname = items.writer.nickname,
                                    contentText = items.contents,
                                    passedTime = elapsedTimeToString(items.elapsedTime),
                                    likeCount = items.likeCount,
                                    profileImageURL = items.writer.profileUrl,
                                    modifier = Modifier.animateContentSize()
                                )
                            }
                        }
                    }
                }
            }
            val lifecycleOwner by rememberUpdatedState(newValue = LocalLifecycleOwner.current)
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { owner, event ->
                    if (event == Lifecycle.Event.ON_STOP) {
                        toggleCommentLikeList.forEach(toggleLikeComment)
                        toggleReplyLikeList.forEach(toggleLikeReply)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        }
    }
    LaunchedEffect(key1 = commentListData) {
        if (lazyListState.firstVisibleItemIndex == 0)
            lazyListState.animateScrollToItem(0)
    }
    LaunchedEffect(key1 = replyTargetNickname) {
        if (replyTargetNickname == null) {
            replyTargetID = 0
            focusManager.clearFocus()
        } else {
            awaitFrame()
            textFieldFocusRequester.requestFocus()
        }
    }
}

private fun<E> MutableList<E>.addOrRemove(element: E): MutableList<E> =
    if (this.contains(element)) {
        this.apply { remove(element) }
    } else {
        this.apply { add(element) }
    }

private fun elapsedTimeToString(elapsedTime: CalculateUserElapsedTime) = when (elapsedTime) {
    is CalculateUserElapsedTime.Date -> "${elapsedTime.month}월 ${elapsedTime.day}일"
    is CalculateUserElapsedTime.Hour -> "${elapsedTime.hour}시간 전"
    is CalculateUserElapsedTime.Minutes -> "${elapsedTime.minutes}분 전"
    is CalculateUserElapsedTime.Recent -> "방금 전"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentSwipeToDismissLayout(
    dismissedToEndBackgroundColor: Color = AbleRed,
    dismissedToStartBackgroundColor: Color = AbleLight,
    dismissedToEnd: () -> Boolean,
    dismissedToStart: () -> Boolean,
    dismissedToStartContent: @Composable (() -> Unit)? = null,
    dismissedToEndContent: @Composable (() -> Unit)? = null,
    dismissContent: @Composable (RowScope.() -> Unit),
) {
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.Default -> false
                DismissValue.DismissedToEnd -> dismissedToEnd()
                DismissValue.DismissedToStart -> dismissedToStart()
            }
        },
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val alignment = when (direction) {
                DismissDirection.EndToStart -> Alignment.CenterEnd
                DismissDirection.StartToEnd -> Alignment.CenterStart
            }
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> White
                    DismissValue.DismissedToEnd -> dismissedToEndBackgroundColor
                    DismissValue.DismissedToStart -> dismissedToStartBackgroundColor
                }
            )
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = color)
                    .padding(horizontal = 30.dp),
            ) {
                when (dismissState.targetValue) {
                    DismissValue.Default -> {}
                    DismissValue.DismissedToEnd -> dismissedToEndContent?.invoke() ?: {}
                    DismissValue.DismissedToStart -> dismissedToStartContent?.invoke() ?: {}
                }
            }
        },
        dismissContent = dismissContent
    )
}

@Composable
private fun CommentLayout(
    onReplyButtonClick: () -> Unit,
    onLikeRequest: () -> Unit,
    onUserProfileVisitRequest: () -> Unit,
    likeUsersViewOnRequest: () -> Unit,
    isLiked: Boolean,
    nickname: String,
    contentText: String,
    passedTime: String,
    likeCount: Int,
    profileImageURL: String?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
        ,
    ) {
        AsyncImage(
            model = profileImageURL,
            contentDescription = "user profile image",
            placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 5.dp)
                .size(34.dp)
                .clip(CircleShape)
                .align(Alignment.Top)
                .nonReplyClickable(onUserProfileVisitRequest)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            HighlightText(
                string = "$nickname $contentText",
                applyStringList = listOf(nickname),
                applySpanStyle = SpanStyle(
                    fontWeight = FontWeight(700)
                ),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    color = AbleDark,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = passedTime,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Right,
                    )
                )
                Text(
                    text = "좋아요 ${likeCount}개",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Right,
                    ),
                    modifier = Modifier.nonReplyClickable { likeUsersViewOnRequest() }
                )
                Text(
                    text = "답글 달기",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Right,
                    ),
                    modifier = Modifier.nonReplyClickable(onReplyButtonClick)
                )
            }
        }
        val color by animateColorAsState(
            targetValue = if (isLiked) AbleRed else Color(0xFFB0B9C2)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = "Like Button",
            tint = color,
            modifier = Modifier
                .nonReplyClickable(onLikeRequest)
                .padding(start = 16.dp)
        )
    }
}

@Composable
private fun ReplyLayout(
    modifier: Modifier = Modifier,
    onLikeRequest: () -> Unit,
    onUserProfileVisitRequest: () -> Unit,
    likeUsersViewOnRequest: () -> Unit,
    isLiked: Boolean,
    nickname: String,
    contentText: String,
    passedTime: String,
    likeCount: Int,
    profileImageURL: String?
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(start = 43.dp)
    ) {
        AsyncImage(
            model = profileImageURL,
            contentDescription = "user profile image",
            placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 5.dp)
                .size(34.dp)
                .clip(CircleShape)
                .align(Alignment.Top)
                .nonReplyClickable(onUserProfileVisitRequest)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            HighlightText(
                string = "$nickname $contentText",
                applyStringList = listOf(nickname),
                applySpanStyle = SpanStyle(
                    fontWeight = FontWeight(700)
                ),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    color = AbleDark,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = passedTime,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Right,
                    )
                )
                Text(
                    text = "좋아요 ${likeCount}개",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = SmallTextGrey,
                        textAlign = TextAlign.Right,
                    ),
                    modifier = Modifier.nonReplyClickable { likeUsersViewOnRequest() }
                )
            }
        }
        val color by animateColorAsState(
            targetValue = if (isLiked) AbleRed else Color(0xFFB0B9C2)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = "Like Button",
            tint = color,
            modifier = Modifier.nonReplyClickable(onLikeRequest)
        )
    }
}

@Composable
fun CommentTextFieldLayout(
    modifier: Modifier = Modifier,
    onCommit: () -> Unit,
    replyTargetNickname: String?,
    onCancelReply: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    nickname: String,
    profileImageURL: String
) {
    Column {
        AnimatedVisibility(visible = replyTargetNickname != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = PlaneGrey)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "${replyTargetNickname}님에게 답글 남기는 중",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = AbleDeep,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
                IconButton(
                    onClick = onCancelReply,
                    modifier = Modifier
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_xmark),
                        contentDescription = null
                    )
                }
            }
        }
        Divider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            AsyncImage(
                model = profileImageURL,
                contentDescription = "user profile image",
                placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
            BasicTextField(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                value = value,
                onValueChange = onValueChange,
                maxLines = 4,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            ) {
                Box(
                    modifier = Modifier
                        .background(color = PlaneGrey, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "${nickname}(으)로 댓글 남기기",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                color = SmallTextGrey,
                                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            )
                        )
                    }
                    it()
                }
            }
            Surface(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                AnimatedVisibility(
                    visible = value.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_circle_arrow_right_disable),
                        contentDescription = "commit button disable",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                AnimatedVisibility(
                    visible = value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_circle_arrow_right_enable),
                        contentDescription = "commit button enable",
                        modifier = Modifier
                            .size(24.dp)
                            .nonReplyClickable(onCommit)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentBottomLayoutPreview() {
    CommentTextFieldLayout(
        onCommit = {},
        replyTargetNickname = null,
        onCancelReply = {},
        value = "",
        onValueChange ={},
        nickname = "애블바디",
        profileImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Pictures/green.png"
    )
}

@Preview(showBackground = true)
@Composable
fun CommentBottomLayoutReplyPreview() {
    CommentTextFieldLayout(
        onCommit = {},
        replyTargetNickname = "아이폰",
        onCancelReply = {},
        value = "",
        onValueChange ={},
        nickname = "애블바디",
        profileImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Pictures/green.png"
    )
}

@Preview(showBackground = true)
@Composable
fun CommentLayoutPreview() {
    CommentLayout(
        onReplyButtonClick = {  },
        onLikeRequest = {  },
        onUserProfileVisitRequest = {},
        likeUsersViewOnRequest = {},
        isLiked = true,
        nickname = "nickname",
        contentText = "가나다라마바사아자차카타파하ABCDEFGHIJKLMNOPQRSTUVXYZㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊ",
        passedTime = "5분 전",
        likeCount = 10,
        profileImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Pictures/green.png"
    )
}

@Preview(showBackground = true)
@Composable
fun ReplyLayoutPreview() {
    ReplyLayout(
        onLikeRequest = {  },
        onUserProfileVisitRequest = {},
        likeUsersViewOnRequest = {},
        isLiked = false,
        nickname = "nick",
        contentText = "안녕!",
        passedTime = "5시간 전",
        likeCount = 100,
        profileImageURL = "https://ablebody-bucket.s3.ap-northeast-2.amazonaws.com/Pictures/green.png"
    )
}

@Preview
@Composable
fun CommentScreenPreview() {
    CommentScreen(
        onBackRequest = {  },
        onUserProfileVisitRequest = { },
        likeUsersViewOnRequest = { _, _ -> },
        onComment = { },
        onReply = { _, _ -> },
        deleteComment = {},
        deleteReply = {},
        toggleLikeComment = {},
        toggleLikeReply = {},
        myUserInfoData = null,
        commentListData = CommentUiState.Loading
    )
}