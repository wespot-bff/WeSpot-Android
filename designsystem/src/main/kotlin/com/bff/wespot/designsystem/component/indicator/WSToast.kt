package com.bff.wespot.designsystem.component.indicator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import kotlinx.coroutines.delay

@Composable
fun WSToast(
    text: String,
    toastType: WSToastType,
    showToast: Boolean,
    closeToast: () -> Unit,
) {
    AnimatedVisibility(visible = showToast, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .clip(WeSpotThemeManager.shapes.extraLarge)
                .background(color = WeSpotThemeManager.colors.toastColor)
                .height(45.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = rememberVectorPainter(toastType.icon()),
                    contentDescription = stringResource(id = R.string.banner_icon),
                    tint = Color.Unspecified,
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    style = toastType.fontStyle(),
                    color = toastType.textColor(),
                    text = text,
                )
            }
        }
        LaunchedEffect(Unit) {
            delay(2000)
            closeToast.invoke()
        }
    }
}

sealed interface WSToastType {
    @Composable
    fun icon(): ImageVector

    @Composable
    fun textColor(): Color

    @Composable
    fun fontStyle(): TextStyle

    data object Success : WSToastType {
        @Composable
        override fun icon(): ImageVector = ImageVector.vectorResource(R.drawable.check)

        @Composable
        override fun textColor(): Color = WeSpotThemeManager.colors.backgroundColor

        @Composable
        override fun fontStyle(): TextStyle = StaticTypeScale.Default.body6
    }

    data object Error : WSToastType {
        @Composable
        override fun icon(): ImageVector = ImageVector.vectorResource(R.drawable.error)

        @Composable
        override fun textColor(): Color = WeSpotThemeManager.colors.backgroundColor

        @Composable
        override fun fontStyle(): TextStyle = StaticTypeScale.Default.body6
    }
}

@OrientationPreviews
@Composable
private fun WSToastPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                WSToast(
                    text = stringResource(id = R.string.succeed_reserve_message),
                    toastType = WSToastType.Success,
                    showToast = true,
                    closeToast = {},
                )

                WSToast(
                    text = stringResource(id = R.string.age_restriction_message),
                    toastType = WSToastType.Error,
                    showToast = true,
                    closeToast = {},
                )
            }
        }
    }
}
