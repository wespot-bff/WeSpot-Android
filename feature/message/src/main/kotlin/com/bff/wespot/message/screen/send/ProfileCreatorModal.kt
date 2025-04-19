package com.bff.wespot.message.screen.send

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendUiState
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.ui.component.LetterCountIndicator
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.util.clickableSingle

@Composable
internal fun ProfileCreatorModal(
    state: SendUiState,
    action: (SendAction) -> Unit,
) {
    Dialog(
        onDismissRequest = { },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(WeSpotThemeManager.colors.modalColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(
                    modifier = Modifier.clickableSingle(
                        onClick = {
                            action(SendAction.OnProfileModalClosed)
                        },
                    ),
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = stringResource(R.string.close_sender_profile_creator_modal_button),
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "받는 사람에게 보여질\n익명 프로필을 설정해 주세요",
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
                textAlign = TextAlign.Center,
            )

            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickableSingle(
                        removeInteraction = true,
                        onClick = { action(SendAction.OnProfileImageClicked) },
                    ),
            ) {
                ProfileCircleImage(
                    size = 87.dp,
                    imageUrl = state.senderProfileInput.image,
                    contentDescription = stringResource(id = R.string.sender_profile_image),
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

            ProfileNameTextField(
                value = state.senderProfileInput.name,
                onValueChanged = { action(SendAction.OnProfileNameChanged(it)) },
            )

            if (state.hasProfileNameProfanity) {
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp, start = 12.dp)
                        .fillMaxWidth(),
                    text = stringResource(com.bff.wespot.designsystem.R.string.has_profanity),
                    style = StaticTypeScale.Default.body7,
                    color = WeSpotThemeManager.colors.dangerColor,
                )
            }

            WSButton(
                paddingValues = PaddingValues(top = 40.dp),
                text = "설정 완료",
                enabled = state.hasProfileNameProfanity.not() && state.senderProfileInput.name.length in 1..10,
                onClick = {
                    action(
                        SendAction.OnProfileModalSelected(
                            SenderProfile(
                                name = state.senderProfileInput.name,
                                image = state.senderProfileInput.image,
                            ),
                        ),
                    )
                },
                content = { it() },
            )
        }
    }

    if (state.showProfileImageOptionBottomSheet) {
        ProfileImageOptionBottomSheet(
            closeSheet = { action(SendAction.OnProfileOptionSheetClosed) },
            onPickerOpenOptionClicked = { action(SendAction.OnPickerOpenOptionClicked) },
            onRemoveProfileOptionClicked = { action(SendAction.OnRemoveProfileOptionClicked) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileImageOptionBottomSheet(
    closeSheet: () -> Unit,
    onPickerOpenOptionClicked: () -> Unit,
    onRemoveProfileOptionClicked: () -> Unit,
) {
    WSBottomSheet(
        closeSheet = closeSheet,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "앨범에서 사진 선택",
                modifier = Modifier
                    .clickableSingle(onClick = onPickerOpenOptionClicked)
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 16.dp, start = 28.dp, end = 28.dp)
                    .clip(RoundedCornerShape(8.dp)),
                textAlign = TextAlign.Center,
                style = StaticTypeScale.Default.body4,
            )

            HorizontalDivider(color = Color(0xFF4F5157))

            Text(
                text = "기본 이미지 적용",
                modifier = Modifier
                    .clickableSingle(onClick = onRemoveProfileOptionClicked)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 48.dp, start = 28.dp, end = 28.dp)
                    .clip(RoundedCornerShape(8.dp)),
                textAlign = TextAlign.Center,
                style = StaticTypeScale.Default.body4,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileNameTextField(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 36.dp),
        value = value,
        onValueChange = onValueChanged,
        textStyle = StaticTypeScale.Default.body4.copy(
            color = WeSpotThemeManager.colors.txtTitleColor,
        ),
        cursorBrush = SolidColor(WeSpotThemeManager.colors.txtTitleColor),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    Text(
                        text = "닉네임을 입력해 주세요",
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.disableBtnColor,
                    )
                },
                trailingIcon = {
                    LetterCountIndicator(
                        currentCount = value.length,
                        maxCount = 10,
                        paddingValues = PaddingValues(),
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WeSpotThemeManager.colors.modalColor,
                    unfocusedContainerColor = WeSpotThemeManager.colors.modalColor,
                    focusedIndicatorColor = WeSpotThemeManager.colors.disableBtnColor,
                    unfocusedIndicatorColor = WeSpotThemeManager.colors.disableBtnColor,
                    cursorColor = WeSpotThemeManager.colors.txtTitleColor,
                ),
                contentPadding = PaddingValues(0.dp),
            )
        },
    )
}
