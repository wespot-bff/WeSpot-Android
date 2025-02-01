package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.StaticTypeScale

@Composable
internal fun TitleSection(
    title: String,
) {
    Text(
        text = title,
        style = StaticTypeScale.Default.header1,
        color = Gray100,
        modifier = Modifier.width(200.dp),
        textAlign = TextAlign.Center,
    )
}
