package com.bff.wespot.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSDialog(
    dialogType: WSDialogType = WSDialogType.TowButton,
    title: String,
    okButtonText: String = "",
    cancelButtonText: String = "",
    okButtonClick: () -> Unit = {},
    cancelButtonClick: () -> Unit = {},
    subTitle: String = "",
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(WeSpotThemeManager.shapes.medium)
                .background(WeSpotThemeManager.colors.modalColor)
                .padding(top = 32.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
        ) {
            WSDialogContent(title = title, subTitle = subTitle)

            when (dialogType) {
                is WSDialogType.TowButton -> {
                    WSDialogTwoButton(
                        okButtonText = okButtonText,
                        cancelButtonText = cancelButtonText,
                        okButtonClick = okButtonClick,
                        cancelButtonClick = cancelButtonClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun WSDialogContent(
    title: String,
    subTitle: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = StaticTypeScale.Default.header1,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )

        Text(
            text = subTitle,
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
        )
    }
}

@Composable
private fun WSDialogTwoButton(
    okButtonText: String,
    cancelButtonText: String,
    okButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            WSButton(
                onClick = cancelButtonClick,
                buttonType = WSButtonType.Secondary,
                text = cancelButtonText,
                paddingValues = PaddingValues(0.dp),
            ) {
                it()
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            WSButton(
                onClick = okButtonClick,
                buttonType = WSButtonType.Primary,
                text = okButtonText,
                paddingValues = PaddingValues(0.dp),
            ) {
                it()
            }
        }
    }
}

sealed interface WSDialogType {
    data object TowButton : WSDialogType
}

@OrientationPreviews
@Composable
private fun WSDialogPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            WSDialog(
                onDismissRequest = {},
                title = stringResource(id = R.string.reserve_message),
                subTitle = stringResource(id = R.string.send_reserved_message_on_specific_time),
                okButtonText = stringResource(id = R.string.ok),
                cancelButtonText = stringResource(id = R.string.cancel),
            )
        }
    }
}
