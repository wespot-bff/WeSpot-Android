package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bff.wespot.model.serverDriven.Paddings
import com.bff.wespot.model.serverDriven.RichText
import com.bff.wespot.server.driven.util.toColor
import com.bff.wespot.server.driven.util.toFontWeight
import com.bff.wespot.server.driven.util.toTextAlign

@Composable
internal fun TextSection(
    richText: RichText,
    paddings: Paddings,
    modifier: Modifier = Modifier,
) {
    Text(
        text = richText.text,
        fontSize = richText.fontSize.sp,
        fontWeight = richText.fontWeight.toFontWeight(),
        modifier = modifier
            .padding(
                start = paddings.start.dp,
                top = paddings.top.dp,
                end = paddings.end.dp,
                bottom = paddings.bottom.dp,
            ),
        textAlign = richText.align.toTextAlign(),
        color = richText.color.toColor(),
    )
}
