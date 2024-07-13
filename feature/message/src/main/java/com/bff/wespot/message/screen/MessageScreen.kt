package com.bff.wespot.message.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bff.wespot.ui.WSHomeTabRow
import com.bff.wespot.ui.WSHomeTopAppBar
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MessageScreen() {
    Scaffold(
        topBar = {
            WSHomeTopAppBar(onClick = {})
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            val tabList = persistentListOf("쪽지 홈", "내 쪽지함")
            var selectedTabIndex by remember { mutableIntStateOf(0) }

            WSHomeTabRow(
                selectedTabIndex = selectedTabIndex,
                tabList = tabList,
                onTabSelected = { index -> selectedTabIndex = index },
            )

            /*WSBanner(
                title = "예약 중인 쪽지 1개",
                icon = "",
                subTitle = ,
                bannerType = WSBannerType.Primary
            )*/
        }
    }
}
