package com.smilehunter.ablebody.presentation.comment.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlin.math.roundToInt

@Composable
fun CommentScreen() {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = {},
                titleText = "댓글"
            )
        },
        bottomBar = {
            CommentTextFieldLayout(
                onCommit = {},
                replyTargetNickname = null,
                onCancelReply = {},
                value = "",
                onValueChange = {},
                nickname = "nickname",
                profileImageURL = ""
            )
        }
    ) { paddingValue ->
        LazyColumn(modifier = Modifier.padding(paddingValue)) {

        }
    }
}

@Composable
private fun CommentLayout(
    onReplyButtonClick: () -> Unit,
    onLikeRequest: () -> Unit,
    onUserProfileVisitRequest: () -> Unit,
    isLiked: Boolean,
    nickname: String,
    contentText: String,
    passedTime: String,
    likeCount: Int,
    profileImageURL: String
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    val offsetTransition by animateFloatAsState(targetValue = offsetX)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    val original = Offset(offsetX, 0f)
                    val newValue = original + Offset(x = dragAmount, y = 0f)
                    if (change.positionChange() != Offset.Zero) change.consume()
                    offsetX = newValue.x
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ,
    ) {
        Row {
            AsyncImage(
                model = profileImageURL,
                contentDescription = "user profile image",
                placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .nonReplyClickable(onUserProfileVisitRequest)
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = nickname,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                        )
                    )
                    Text(
                        text = contentText,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(500),
                            color = AbleDark,
                        )
                    )
                }
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
                        )
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
private fun ReplyLayout(
    onLikeRequest: () -> Unit,
    onUserProfileVisitRequest: () -> Unit,
    isLiked: Boolean,
    nickname: String,
    contentText: String,
    passedTime: String,
    likeCount: Int,
    profileImageURL: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(start = 43.dp)
    ) {
        Row {
            AsyncImage(
                model = profileImageURL,
                contentDescription = "user profile image",
                placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .nonReplyClickable(onUserProfileVisitRequest)
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = nickname,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                        )
                    )
                    Text(
                        text = contentText,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(500),
                            color = AbleDark,
                        )
                    )
                }
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
                        )
                    )
                }
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            AsyncImage(
                model = profileImageURL,
                contentDescription = "user profile image",
                placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = onValueChange
            ) {
                Box(
                    modifier = Modifier
                        .height(37.dp)
                        .background(color = PlaneGrey, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "${nickname}(으)로 댓글 남기기",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                color = SmallTextGrey,
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    it()
                }
            }
            AnimatedVisibility(visible = value.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_circle_arrow_right_disable),
                    contentDescription = "commit button disable",
                )
            }
            AnimatedVisibility(visible = value.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_circle_arrow_right_enable),
                    contentDescription = "commit button enable",
                    modifier = Modifier.nonReplyClickable(onCommit)
                )
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
        isLiked = true,
        nickname = "nickname",
        contentText = "가나다라마바사",
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
        isLiked = true,
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
    CommentScreen()

}