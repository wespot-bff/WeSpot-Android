package com.bff.wespot.server.driven.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.bff.wespot.designsystem.theme.StaticTypeScale

@Composable
fun String.toTextStyle(): TextStyle? {
    return when (this.lowercase()) {
        "header1" -> StaticTypeScale.Default.header1
        "header2" -> StaticTypeScale.Default.header2
        "header3" -> StaticTypeScale.Default.header3
        "body1" -> StaticTypeScale.Default.body1
        "body2" -> StaticTypeScale.Default.body2
        "body3" -> StaticTypeScale.Default.body3
        "body4" -> StaticTypeScale.Default.body4
        "body5" -> StaticTypeScale.Default.body5
        "body6" -> StaticTypeScale.Default.body6
        "body7" -> StaticTypeScale.Default.body7
        "body8" -> StaticTypeScale.Default.body8
        "body9" -> StaticTypeScale.Default.body9
        "body10" -> StaticTypeScale.Default.body10
        "body11" -> StaticTypeScale.Default.body11
        "badge" -> StaticTypeScale.Default.badge
        else -> null
    }
}
