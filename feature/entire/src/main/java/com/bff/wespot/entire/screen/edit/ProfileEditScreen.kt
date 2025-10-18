package com.bff.wespot.entire.screen.edit

import android.view.ViewTreeObserver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import com.bff.wespot.analytics.AnalyticsEvent
import com.bff.wespot.analytics.AnalyticsHelper
import com.bff.wespot.analytics.LocalAnalyticsHelper
import com.bff.wespot.analytics.TrackScreenViewEvent
import com.bff.wespot.analytics.logClickAction
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.modal.WSDialogType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.entire.R
import com.bff.wespot.entire.common.INTRODUCTION_MAX_LENGTH
import com.bff.wespot.entire.state.edit.ProfileEditAction
import com.bff.wespot.entire.state.edit.ProfileEditSideEffect
import com.bff.wespot.entire.viewmodel.ProfileEditViewModel
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.component.BottomButtonLayout
import com.bff.wespot.ui.component.LetterCountIndicator
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.TopToast
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.clickableSingle
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface ProfileEditNavigator {
    fun navigateToEntireScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ProfileEditScreen(
    navigator: ProfileEditNavigator,
    activityNavigator: Navigator,
    viewModel: ProfileEditViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var toast by remember { mutableStateOf(ToastState()) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver

    val action = viewModel::onAction
    val state by viewModel.collectAsState()

    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                action(ProfileEditAction.OnProfileImagePicked(it.toString()))
            }
        }

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            is ProfileEditSideEffect.ShowToast -> {
                toast = it.toastState
                focusManager.clearFocus()
            }

            is ProfileEditSideEffect.OpenPicker -> {
                pickImage.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.SingleMimeType(
                            "image/*",
                        ),
                    ),
                )
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.profile_edit),
                canNavigateBack = true,
                navigateUp = { navigator.navigateToEntireScreen() },
            )
        },
    ) {
        BottomButtonLayout(
            modifier = Modifier.padding(it),
            button = {
                val isEdited = state.profile.introduction != state.introductionInput ||
                    state.profilePath != state.profile.profileCharacter.iconUrl
                WSButton(
                    onClick = {
                        analyticsHelper.logClickAction(
                            name = "click_mypage_edit_profile_complete",
                            extras = listOf(
                                AnalyticsEvent.Param(
                                    "introduce_edit_contents",
                                    state.introductionInput,
                                ),
                            ),
                        )
                        action(ProfileEditAction.OnProfileEditDoneButtonClicked)
                    },
                    enabled =
                        isEdited &&
                            state.hasProfanity.not() &&
                            state.introductionInput.length in 0..20,
                    text = stringResource(id = R.string.edit_done),
                    content = { it() },
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickableSingle(removeInteraction = true) {
                            if (state.profilePath.isNullOrEmpty()) {
                                action(ProfileEditAction.OpenPicker)
                            } else {
                                action(ProfileEditAction.ChangeBottomSheetState(true))
                            }
                        },
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(state.profilePath)
                            .error(com.bff.wespot.ui.R.drawable.default_profile)
                            .fallback(com.bff.wespot.ui.R.drawable.default_profile)
                            .placeholder(com.bff.wespot.ui.R.drawable.default_profile)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(
                            com.bff.wespot.ui.R.string.user_character_image,
                        ),
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .clip(WeSpotThemeManager.shapes.small)
                            .background(WeSpotThemeManager.colors.secondaryBtnColor)
                            .zIndex(1f),
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(14.dp),
                            painter = painterResource(id = R.drawable.album),
                            contentDescription = stringResource(R.string.edit_icon),
                        )
                    }
                }

                ProfileEditLockedItem(
                    title = stringResource(R.string.name),
                    content = state.profile.name,
                    onClick = {
                        focusManager.clearFocus()
                        action(ProfileEditAction.OnRequestDialogShown)
                    },
                )

                ProfileEditLockedItem(
                    title = stringResource(R.string.gender),
                    content = state.profile.toGenderKorean(),
                    onClick = {
                        focusManager.clearFocus()
                        action(ProfileEditAction.OnRequestDialogShown)
                    },
                )

                ProfileEditLockedItem(
                    title = stringResource(R.string.school_info),
                    content = state.profile.toSchoolInfo(),
                    onClick = {
                        focusManager.clearFocus()
                        action(ProfileEditAction.OnRequestDialogShown)
                    },
                )

                ProfileIntroductionItem(
                    title = stringResource(com.bff.wespot.ui.R.string.introduction),
                    content = state.introductionInput,
                    hasProfanity = state.hasProfanity,
                    onValueChange = { value -> action(ProfileEditAction.OnIntroductionChanged(value)) },
                    onFocusChanged = { focusState ->
                        action(ProfileEditAction.OnProfileEditTextFieldFocused(focusState.isFocused))
                    },
                )
            }
        }
    }

    TopToast(
        message = stringResource(toast.message),
        toastType = toast.type,
        showToast = toast.show,
    ) {
        toast = toast.copy(show = false)
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat
                .getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime())
                ?: true

            if (isKeyboardOpen) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    if (state.requestDialog) {
        WSDialog(
            title = stringResource(R.string.request_change_profile),
            okButtonText = stringResource(R.string.request),
            cancelButtonText = stringResource(com.bff.wespot.designsystem.R.string.cancel),
            okButtonClick = {
                activityNavigator.navigateToWebLink(
                    context = context,
                    webLink = state.profileChangeGoogleFormUrl,
                )
            },
            cancelButtonClick = {
                action(ProfileEditAction.OnRequestDialogDismissed)
            },
            dialogType = WSDialogType.TwoButton,
        ) {
            action(ProfileEditAction.OnRequestDialogDismissed)
        }
    }

    if (state.changeBottomSheet) {
        WSBottomSheet(
            closeSheet = {
                action(ProfileEditAction.ChangeBottomSheetState(false))
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.change_image),
                    modifier = Modifier
                        .clickableSingle {
                            action(ProfileEditAction.OpenPicker)
                            action(ProfileEditAction.ChangeBottomSheetState(false))
                        }.fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 28.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    textAlign = TextAlign.Center,
                    style = StaticTypeScale.Default.body3,
                )
                HorizontalDivider(
                    color = Color(0xFF4F5157),
                )
                Text(
                    text = stringResource(R.string.remove_image),
                    modifier = Modifier
                        .clickableSingle {
                            action(ProfileEditAction.OnProfileImagePicked(null))
                            action(ProfileEditAction.ChangeBottomSheetState(false))
                        }.fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 28.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    textAlign = TextAlign.Center,
                    style = StaticTypeScale.Default.body3,
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        action(ProfileEditAction.OnProfileEditScreenEntered)
    }

    TrackScreenViewEvent("view_mypage_edit_profile")
}

@Composable
fun ProfileEditLockedItem(
    title: String,
    content: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            style = StaticTypeScale.Default.body4,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )

        Box(modifier = Modifier.clickable { onClick() }) {
            WsTextField(
                value = "",
                onValueChange = {},
                enabled = false,
                placeholder = content,
                textFieldType = WsTextFieldType.Lock,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileIntroductionItem(
    title: String,
    content: String,
    hasProfanity: Boolean,
    onValueChange: (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            style = StaticTypeScale.Default.body4,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )

        Spacer(modifier = Modifier.height(12.dp))

        WsTextField(
            value = content,
            onValueChange = onValueChange,
            placeholder = stringResource(R.string.introduction_placeholder),
            onFocusChanged = onFocusChanged,
            textFieldType = WsTextFieldType.Normal,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val warningMessage = when {
                content.length > INTRODUCTION_MAX_LENGTH -> {
                    stringResource(R.string.introduction_limit)
                }

                hasProfanity -> {
                    stringResource(com.bff.wespot.designsystem.R.string.has_profanity)
                }

                else -> ""
            }

            Text(
                modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),
                text = warningMessage,
                style = StaticTypeScale.Default.body7,
                color = WeSpotThemeManager.colors.dangerColor,
            )

            LetterCountIndicator(currentCount = content.length, maxCount = 20)
        }
    }
}
