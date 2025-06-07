package com.bff.wespot.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.toggle.WSSwitch
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@Composable
fun SettingSwitchItem(
    title: String,
    subTitle: String,
    switchValue: Boolean,
    onSwitched: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = StaticTypeScale.Default.body2,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Text(
                text = subTitle,
                style = StaticTypeScale.Default.body8,
                color = Gray400,
            )
        }

        WSSwitch(
            checked = switchValue,
            onCheckedChange = onSwitched,
        )
    }
}
