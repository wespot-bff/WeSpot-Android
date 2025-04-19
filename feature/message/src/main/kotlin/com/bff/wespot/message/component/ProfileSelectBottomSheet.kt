package com.bff.wespot.message.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.util.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileSelectBottomSheet(
    profileList: List<SenderProfile>,
    closeSheet: () -> Unit,
    onProfileAddButtonClicked: () -> Unit,
    onProfileSelected: (SenderProfile) -> Unit,
) {
    WSBottomSheet(closeSheet = closeSheet) {
        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 32.dp, bottom = 40.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.sender_profile_sheet_title),
                    style = StaticTypeScale.Default.body1,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(R.string.sender_profile_sheet_content),
                    style = StaticTypeScale.Default.body6,
                    color = WeSpotThemeManager.colors.txtSubColor,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            profileList.forEach {
                ProfileSheetItem(
                    profile = it,
                    onClick = { onProfileSelected(it) },
                )
            }

            if (profileList.size < 3) {
                ProfileAddSheetItem {
                    onProfileAddButtonClicked()
                }
            }
        }
    }
}

@Composable
private fun ProfileSheetItem(
    profile: SenderProfile,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickableSingle(removeInteraction = true, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileCircleImage(
            size = 34.dp,
            imageUrl = profile.image,
            contentDescription = stringResource(R.string.sender_profile_icon),
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            text = profile.name,
            style = StaticTypeScale.Default.body3,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )

        Text(
            modifier = Modifier
                .padding(start = 12.dp),
            text = "최근 " + profile.recentlyTalk.toStringWithDotSeparator(),
            style = StaticTypeScale.Default.body9,
            color = WeSpotThemeManager.colors.disableIcnColor,
        )

        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.right_arrow),
            contentDescription = stringResource(id = com.bff.wespot.designsystem.R.string.right_arrow),
        )
    }
}

@Composable
private fun ProfileAddSheetItem(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickableSingle(removeInteraction = true, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            modifier = Modifier.size(34.dp),
            painter = painterResource(id = R.drawable.add),
            contentDescription = stringResource(R.string.add_sender_profile_icon),
        )

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.create_sender_profile_button_title),
            style = StaticTypeScale.Default.body3,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )
    }
}
