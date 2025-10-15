package com.bff.wespot.server.driven.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.bff.wespot.designsystem.theme.StaticTypeScale

@Composable
fun String.toTextStyle(): TextStyle? = when (this.lowercase()) {
    "header01" -> StaticTypeScale.Default.header1
    "header02" -> StaticTypeScale.Default.header2
    "header03" -> StaticTypeScale.Default.header3
    "body01" -> StaticTypeScale.Default.body1
    "body02" -> StaticTypeScale.Default.body2
    "body03" -> StaticTypeScale.Default.body3
    "body04" -> StaticTypeScale.Default.body4
    "body05" -> StaticTypeScale.Default.body5
    "body06" -> StaticTypeScale.Default.body6
    "body07" -> StaticTypeScale.Default.body7
    "body08" -> StaticTypeScale.Default.body8
    "body09" -> StaticTypeScale.Default.body9
    "body10" -> StaticTypeScale.Default.body10
    "body11" -> StaticTypeScale.Default.body11
    "badge" -> StaticTypeScale.Default.badge
    else -> null
}
