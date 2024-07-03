package com.bff.wespot.designsystem.component.toggle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = WSSwitchType.Primary.checkedThumbColor(),
            checkedTrackColor = WSSwitchType.Primary.checkedTrackColor(),
            uncheckedThumbColor = WSSwitchType.Primary.uncheckedThumbColor(),
            uncheckedTrackColor = WSSwitchType.Primary.uncheckedTrackColor(),
            uncheckedBorderColor = Color.Transparent,
        ),
        thumbContent = {}
    )
}

sealed interface WSSwitchType {
    @Composable
    fun checkedThumbColor(): Color

    @Composable
    fun checkedTrackColor(): Color

    @Composable
    fun uncheckedThumbColor(): Color

    @Composable
    fun uncheckedTrackColor(): Color

    object Primary : WSSwitchType {
        @Composable
        override fun checkedThumbColor() = Gray100

        @Composable
        override fun checkedTrackColor() = WeSpotThemeManager.colors.toggleColor

        @Composable
        override fun uncheckedThumbColor() = Gray100

        @Composable
        override fun uncheckedTrackColor() = Gray600
    }
}

@OrientationPreviews
@Composable
private fun WSSwitchPreview() {
    var checked by remember { mutableStateOf(false) }

    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            WSSwitch(checked = checked, onCheckedChange = { checked = it })
        }
    }
}