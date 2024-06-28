package com.bff.wespot.designsystem.util

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 640,
    heightDp = 480
)
@Preview(
    name = "Portrait Mode",
    showBackground = true,
    device = Devices.PIXEL_4
)
annotation class OrientationPreviews

@Preview(
    name = "Default Font Size",
    fontScale = 1f
)
@Preview(
    name = "Large Font Size",
    fontScale = 1.5f
)
annotation class FontScalePreviews
