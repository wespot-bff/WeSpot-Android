package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.StaticTypeScale

@Composable
internal fun TitleSection(
    title: String,
    textAlign: TextAlign,
    width: Dp? = null,
    paddingValues: PaddingValues = PaddingValues(0.dp),
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(paddingValues)
            .let {
                if (width != null) {
                    it.width(width)
                } else {
                    it.fillMaxWidth()
                }
            },
        style = StaticTypeScale.Default.header1,
        color = Gray100,
        textAlign = textAlign,
    )
}
