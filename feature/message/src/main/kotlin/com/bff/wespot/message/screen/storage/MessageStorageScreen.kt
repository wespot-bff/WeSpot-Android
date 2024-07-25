package com.bff.wespot.message.screen.storage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bff.wespot.message.common.RECEIVED_MESSAGE_INDEX
import com.bff.wespot.message.common.SENT_MESSAGE_INDEX
import com.bff.wespot.ui.WSHomeChipGroup
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MessageStorageScreen() {
    val chipList = persistentListOf(
        "받은 쪽지",
        "보낸 쪽지",
    )
    var selectedChipIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        WSHomeChipGroup(
            items = chipList,
            selectedItemIndex = selectedChipIndex,
            onSelectedChanged = { index -> selectedChipIndex = index },
        )

        when (selectedChipIndex) {
            RECEIVED_MESSAGE_INDEX -> {
            }

            SENT_MESSAGE_INDEX -> {
            }
        }
    }
}
