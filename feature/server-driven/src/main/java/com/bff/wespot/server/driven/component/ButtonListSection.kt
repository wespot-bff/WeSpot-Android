package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.model.serverDriven.ButtonItemComponent
import com.bff.wespot.model.serverDriven.ClickAction
import com.bff.wespot.ui.component.ListBottomGradient

@Composable
internal fun ButtonListSection(
    buttonList: List<ButtonItemComponent>,
    onClick: (ClickAction) -> Unit,
) {
    ListBottomGradient(124)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        buttonList.forEach { button ->
            Box(modifier = Modifier.weight(1f)) {
                WSButton(
                    buttonType = WSButtonType.Custom(
                        textColor = button.textColor,
                        buttonColor = button.buttonColor,
                        pressColor = button.buttonColor,
                    ),
                    text = button.text,
                    paddingValues = PaddingValues(0.dp),
                    onClick = {
                        onClick(button.onClickAction)
                    },
                    content = { it() },
                )
            }
        }
    }
}
