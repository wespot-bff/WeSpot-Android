package com.bff.wespot.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.community.R
import com.bff.wespot.community.viewmodel.CommunityHomeViewModel
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
internal fun CommunityHomeScreen(
    viewModel: CommunityHomeViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            FilterChip()
        },
        floatingActionButton = {
            CommunityFABButton()
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            item {
                Text("Hello Testing here")
            }
        }
    }
}

@Composable
private fun FilterChip() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(2) {
            Box(
                modifier = Modifier.background(
                    WeSpotThemeManager.colors.abledIconColor,
                    shape = RoundedCornerShape(80.dp),
                ),
            ) {
                Text(
                    text = it.toString(),
                    style = StaticTypeScale.Default.body4,
                    color = WeSpotThemeManager.colors.backgroundColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                )
            }
        }
    }
}

@Composable
private fun CommunityFABButton() {
    FloatingActionButton(
        shape = CircleShape,
        onClick = {},
        containerColor = WeSpotThemeManager.colors.primaryColor,
        contentColor = WeSpotThemeManager.colors.backgroundColor,
        modifier = Modifier.size(50.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.post_article),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
        )
    }
}
