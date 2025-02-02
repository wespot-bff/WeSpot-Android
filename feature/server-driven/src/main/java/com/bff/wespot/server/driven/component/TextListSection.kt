package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.model.serverDriven.TextList

@Composable
internal fun TextListSection(
    textList: List<TextList>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 34.dp),
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

                Text(
                    text = item.text,
                    style = StaticTypeScale.Default.body6,
                    color = Gray100,
                )
            }
        }
    }
}
