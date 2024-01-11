package com.smilehunter.ablebody.presentation.home.my.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.result.FileTooLargeException
import com.smilehunter.ablebody.model.UserInfoData
import com.smilehunter.ablebody.presentation.home.my.EditProfileViewModel
import com.smilehunter.ablebody.presentation.home.my.data.EditProfileUiStatus
import com.smilehunter.ablebody.presentation.home.my.data.NicknameCheckUiState
import com.smilehunter.ablebody.presentation.onboarding.data.ProfileImages
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.launch
import java.io.InputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileRoute(
    onBackRequest: () -> Unit,
    defaultImageSelectableViewRequest: () -> Unit,
    selectedProfileImageNumber: Int?,
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uploadStatus by editProfileViewModel.uploadStatus.collectAsStateWithLifecycle()
    val userInfo by editProfileViewModel.userInfoData.collectAsStateWithLifecycle()
    val nickname by editProfileViewModel.nickname.collectAsStateWithLifecycle()
    val isNicknameAvailable by editProfileViewModel.isNicknameAvailable.collectAsStateWithLifecycle()
    val name by editProfileViewModel.userName.collectAsStateWithLifecycle()
    val userHeight by editProfileViewModel.userHeight.collectAsStateWithLifecycle()
    val userWeight by editProfileViewModel.userWeight.collectAsStateWithLifecycle()
    val job by editProfileViewModel.userJob.collectAsStateWithLifecycle()
    val introduction by editProfileViewModel.introduction.collectAsStateWithLifecycle()

    EditProfileScreen(
        onBackRequest = onBackRequest,
        confirmButtonClick = editProfileViewModel::changeProfile,
        defaultImageSelectableViewRequest = defaultImageSelectableViewRequest,
        changeOnNickname = editProfileViewModel::updateNickname,
        changeOnName = editProfileViewModel::updateUserName,
        changeOnUserHeight = editProfileViewModel::updateUserHeight,
        changeOnUserWeight = editProfileViewModel::updateUserWeight,
        changeOnJob = editProfileViewModel::updateUserJob,
        changeOnIntroduction = editProfileViewModel::updateIntroduction,
        selectedProfileImageNumber = selectedProfileImageNumber,
        userInfo = userInfo,
        nickname = nickname,
        isNicknameAvailable = isNicknameAvailable,
        name = name,
        userHeight = userHeight,
        userWeight = userWeight,
        job = job,
        introduction = introduction
    )

    if (uploadStatus is EditProfileUiStatus.Uploading) {
        Box(Modifier.fillMaxSize()) {
            Scrim(
                color = BottomSheetDefaults.ScrimColor,
                visible = true
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }

    if (uploadStatus is EditProfileUiStatus.LoadFail) {
        if ((uploadStatus as EditProfileUiStatus.LoadFail).t is FileTooLargeException) {
            val context = LocalContext.current
            Toast.makeText(context, "이미지 사이즈가 너무 커서 전송 할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    if (uploadStatus is EditProfileUiStatus.Success) {
        onBackRequest()
    }

}

@Composable
private fun Scrim(
    color: Color,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        Canvas(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {

                }
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackRequest: () -> Unit,
    confirmButtonClick: (InputStream?, Int?) -> Unit,
    defaultImageSelectableViewRequest: () -> Unit,
    changeOnNickname: (String) -> Unit,
    changeOnName: (String) -> Unit,
    changeOnUserHeight: (String) -> Unit,
    changeOnUserWeight: (String) -> Unit,
    changeOnJob: (String) -> Unit,
    changeOnIntroduction: (String) -> Unit,
    selectedProfileImageNumber: Int?,
    userInfo: UserInfoData?,
    nickname: String,
    isNicknameAvailable: NicknameCheckUiState,
    name: String,
    userHeight: String,
    userWeight: String,
    job: String,
    introduction: String,
) {
    var isEditProfileImageBottomSheetShow by rememberSaveable { mutableStateOf(false) }

    var profileImageInputStream by remember { mutableStateOf<InputStream?>(null) }
    var previewUri: Uri? by remember { mutableStateOf(null) }

    val defaultProfileImageNumber = remember { mutableStateListOf<Int?>() }
    val defaultProfileImageResourceId by remember(defaultProfileImageNumber) {
        derivedStateOf {
            val current = defaultProfileImageNumber.lastOrNull() ?: return@derivedStateOf null
            ProfileImages.values().getOrNull(current)?.resourcesID
        }
    }

    if (selectedProfileImageNumber != defaultProfileImageNumber.lastOrNull()) {
        defaultProfileImageNumber.add(selectedProfileImageNumber)
        profileImageInputStream = null
        previewUri = null
    }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) {
            return@rememberLauncherForActivityResult
        }
        context.contentResolver.openInputStream(uri)?.let {
            profileImageInputStream = it
            previewUri = uri
        }
    }

    if (isEditProfileImageBottomSheetShow) {
        EditProfileImageBottomSheet(
            onDismissRequest = {
                if (it == "사진첩에서 선택") {
                    val request = PickVisualMediaRequest.Builder().run {
                        setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        build()
                    }
                    launcher.launch(request)
                }

                if (it == "기본 캐릭터") {
                    defaultImageSelectableViewRequest()
                }

                isEditProfileImageBottomSheetShow = false
            },
        )
    }

    var isSaveAlertDialogShow by rememberSaveable { mutableStateOf(false) }
    if (isSaveAlertDialogShow) {
        SaveAlertDialog(
            onDismissRequest = { isSaveAlertDialogShow = false },
            positiveButtonOnClick = {
                val number = if (profileImageInputStream == null) {
                    defaultProfileImageNumber.lastOrNull()
                } else {
                    null
                }

                confirmButtonClick(profileImageInputStream, number)
                isSaveAlertDialogShow = false
            },
            negativeButtonOnClick = {
                onBackRequest()
                isSaveAlertDialogShow = false
            }
        )
    }

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "프로필 편집"
            ) {
                Text(
                    text = "완료",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleBlue,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.nonReplyClickable {
                        isSaveAlertDialogShow = true
                    }
                )
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .nonReplyClickable { isEditProfileImageBottomSheetShow = true }
            ) {
                AsyncImage(
                    model = previewUri ?: defaultProfileImageResourceId ?: userInfo?.profileUrl,
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.Medium,
                    placeholder = previewPlaceHolder(id = R.drawable.profile_man1),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Box(
                    modifier = Modifier
                        .background(PlaneGrey, CircleShape)
                        .padding(4.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "edit"
                    )
                }
            }

            val description: ProfileTextFieldDescription? = when (isNicknameAvailable) {
                is NicknameCheckUiState.Available -> {
                    ProfileTextFieldDescription(
                        value = "사용 가능한 닉네임이에요.",
                        color = AbleBlue
                    )
                }

                is NicknameCheckUiState.LoadFail -> {
                    ProfileTextFieldDescription(
                        value = "알 수 없는 에러가 발생하였습니다.",
                        color = AbleRed
                    )
                }

                is NicknameCheckUiState.Loading -> null
                is NicknameCheckUiState.UnAvailable -> {
                    ProfileTextFieldDescription(
                        value = "이미 사용 중인 닉네임이에요.",
                        color = AbleRed
                    )
                }
            }

            ProfileTextFieldLayout(
                value = nickname,
                onValueChange = changeOnNickname,
                placeHolder = "내용을 입력해주세요",
                label = "닉네임",
                description = description,
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp
                )
            )
            ProfileTextFieldLayout(
                value = name,
                onValueChange = changeOnName,
                placeHolder = "내용을 입력해주세요",
                label = "이름",
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp
                )
            )
            ProfileTextFieldLayout(
                value = userHeight,
                onValueChange = changeOnUserHeight,
                placeHolder = "cm",
                label = "키",
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ProfileTextFieldLayout(
                value = userWeight,
                onValueChange = changeOnUserWeight,
                placeHolder = "kg",
                label = "몸무게",
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ProfileTextFieldLayout(
                value = job,
                onValueChange = changeOnJob,
                placeHolder = "어떤 일을 하시는지 궁금해요",
                label = "직업",
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp
                )
            )
            ProfileTextFieldLayout(
                value = introduction,
                onValueChange = changeOnIntroduction,
                placeHolder = "자신을 표현해주세요.(최대 34자)",
                label = "소개글",
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 12.dp, start = 16.dp, end = 16.dp
                ),
                singleLine = false,
                minLines = 5,
                maxLines = 5
            )
        }
    }
}

@Composable
fun SaveAlertDialog(
    onDismissRequest: () -> Unit,
    positiveButtonOnClick: () -> Unit,
    negativeButtonOnClick: () -> Unit,
) {
    AbleBodyAlertDialog(
        onDismissRequest = onDismissRequest,
        positiveButtonOnClick = positiveButtonOnClick,
        negativeButtonOnClick = negativeButtonOnClick,
        positiveText = "예",
        negativeText = "아니오"
    ) {
        Text(
            text = "저장할까요?",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            text = "바꾼 정보를 저장합니다.",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )
    }
}

@Preview
@Composable
fun SaveAlertDialogPreview() {
    ABLEBODY_AndroidTheme {
        SaveAlertDialog(
            onDismissRequest = { },
            positiveButtonOnClick = { },
            negativeButtonOnClick = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileImageBottomSheet(
    onDismissRequest: (String?) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
) {
    val scope = rememberCoroutineScope()
    val animateToDismiss: (String?) -> Unit = { value ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest(value)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest(null) },
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        windowInsets = windowInsets
    ) {
        Text(
            text = "프로필 사진",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp)
        )
        Text(
            text = "기본 캐릭터",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { animateToDismiss("기본 캐릭터") }
                .padding(horizontal = 16.dp, vertical = 15.dp)
        )
        Text(
            text = "사진첩에서 선택",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { animateToDismiss("사진첩에서 선택") }
                .padding(horizontal = 16.dp, vertical = 15.dp)
        )
        Box(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun ProfileTextFieldLayout(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
        fontWeight = FontWeight(400),
        color = AbleDark,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    label: String? = null,
    description: ProfileTextFieldDescription? = null,
    placeHolder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    ) {
        Column {

            if (label != null) {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDeep,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.padding(
                        top = 4.dp,
                        bottom = 8.dp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = PlaneGrey, shape = RoundedCornerShape(size = 10.dp))
                    .padding(start = 10.dp, top = 15.dp, end = 10.dp, bottom = 15.dp)
            ) {
                it()
                if (placeHolder != null && value.isEmpty()) {
                    Text(
                        text = placeHolder,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = SmallTextGrey,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }

            if (description != null) {
                Text(
                    text = description.value,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = description.color,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 10.dp)
                )
            }

        }
    }
}

data class ProfileTextFieldDescription(
    val value: String,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun ProfileTextFieldLayoutPreview() {
    ABLEBODY_AndroidTheme {
        ProfileTextFieldLayout(
            value = "피아노위의스팸",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = "닉네임",
            description = ProfileTextFieldDescription(
                "사용 가능한 닉네임이에요.",
                AbleBlue
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(heightDp = 10)
@Composable
fun EditProfileImageBottomSheetPreview() {
    ABLEBODY_AndroidTheme {
        EditProfileImageBottomSheet(
            onDismissRequest = { },
        )
    }
}

@Preview
@Composable
fun EditProfileScreenPreview() {
    ABLEBODY_AndroidTheme {
        EditProfileScreen(
            onBackRequest = {},
            confirmButtonClick = { _, _ -> },
            defaultImageSelectableViewRequest = { },
            changeOnNickname = {},
            changeOnName = {},
            changeOnUserHeight = {},
            changeOnUserWeight = {},
            changeOnJob = {},
            changeOnIntroduction = {},
            userInfo = null,
            selectedProfileImageNumber = null,
            nickname = "",
            isNicknameAvailable = NicknameCheckUiState.Loading,
            name = "",
            userHeight = "",
            userWeight = "",
            job = "",
            introduction = ""
        )
    }
}