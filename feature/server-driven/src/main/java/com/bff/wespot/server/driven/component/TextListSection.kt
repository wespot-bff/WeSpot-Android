package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bff.wespot.model.serverDriven.Paddings
import com.bff.wespot.model.serverDriven.TextList
import com.bff.wespot.server.driven.util.toPaddingValues

@Composable
internal fun TextListSection(
    textList: List<TextList>,
    paddings: Paddings,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddings.toPaddingValues()),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        repeat(textList.size) {
            val item = textList[it]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AsyncImage(
                    model = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )

                TextSection(
                    richText = item.richText,
                    paddings = Paddings.None,
                )
            }
        }
    }
}
