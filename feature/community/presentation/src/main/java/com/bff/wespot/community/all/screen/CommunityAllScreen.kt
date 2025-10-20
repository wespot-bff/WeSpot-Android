package com.bff.wespot.community.all.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.community.presentation.R
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.util.clickableSingle

@Composable
fun CommunityAllScreen(
    onNavigateToAllPosts: () -> Unit,
    onNavigateToMyComments: () -> Unit,
    onNavigateToScraps: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        NavigationCard(
            title = stringResource(R.string.all_post_written),
            iconRes = R.drawable.post_article,
            onClick = onNavigateToAllPosts,
        )

        NavigationCard(
            title = stringResource(R.string.all_comment_written),
            iconRes = R.drawable.chat_bubble,
            onClick = onNavigateToMyComments,
        )

        NavigationCard(
            title = stringResource(R.string.all_post_scraped),
            iconRes = R.drawable.scrap,
            onClick = onNavigateToScraps,
        )
    }
}

@Composable
private fun NavigationCard(
    title: String,
    iconRes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingle {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Gray300,
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = title,
            style = StaticTypeScale.Default.body3,
            color = WeSpotThemeManager.colors.txtTitleColor,
            modifier = Modifier.weight(1f),
        )

        Icon(
            painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.right_arrow),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Gray400,
        )
    }
}
