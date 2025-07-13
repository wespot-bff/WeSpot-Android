package com.bff.wespot.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.main.model.RestrictionContent
import com.bff.wespot.main.state.MainUiState
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.component.WSBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RestrictionBottomSheet(
    content: RestrictionContent,
    state: MainUiState,
    navigator: Navigator,
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    WSBottomSheet(
        sheetState = bottomSheetState,
        closeSheet = {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(content.title),
                style = StaticTypeScale.Default.body1,
                modifier = Modifier.padding(bottom = 10.dp),
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            BulletPoint(text = stringResource(content.body1))

            BulletPoint(
                text = stringResource(
                    content.body2,
                    state.restriction.toKoreanDate()
                ),
            )

            BulletPoint(text = stringResource(content.body3))

            if (content.body4 != null) {
                BulletPoint(text = stringResource(content.body4))
            }

            if (content.buttonNumber == 1) {
                WSButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    text = stringResource(com.bff.wespot.auth.R.string.confirm),
                    paddingValues = PaddingValues(
                        start = 0.dp,
                        end = 0.dp,
                        top = 24.dp,
                        bottom = 10.dp
                    ),
                ) {
                    it.invoke()
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        WSButton(
                            onClick = {
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            },
                            buttonType = WSButtonType.Secondary,
                            text = stringResource(R.string.close),
                            paddingValues = PaddingValues(0.dp),
                        ) {
                            it()
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        WSButton(
                            onClick = {
                                navigator.navigateToWebLink(context, state.kakaoChannel)
                            },
                            text = stringResource(com.bff.wespot.R.string.one_on_one),
                            paddingValues = PaddingValues(0.dp),
                        ) {
                            it()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "•",
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
        )

        Text(
            text = text,
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
        )
    }
}
