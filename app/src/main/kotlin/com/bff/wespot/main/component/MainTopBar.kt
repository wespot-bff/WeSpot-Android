package com.bff.wespot.main.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bff.wespot.navigation.AppNavGraphs
import com.bff.wespot.main.model.BarType
import com.bff.wespot.R
import com.bff.wespot.common.navigateToNavGraph
import com.bff.wespot.designsystem.R.drawable
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.entire.screen.destinations.SettingScreenDestination
import com.bff.wespot.message.screen.destinations.MessageSettingScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainTopBar(
    isTopNavigationScreen: BarType,
    navController: NavController,
) {
    AnimatedContent(
        targetState = isTopNavigationScreen,
        transitionSpec = {
            fadeIn(animationSpec = tween()) togetherWith fadeOut(animationSpec = tween())
        },
        label = stringResource(R.string.bottom_bar_animated_content_label),
    ) { targetState ->
        if (targetState != BarType.NONE) {
            WSTopBar(
                title = "",
                navigation = {
                    if (isTopNavigationScreen.shouldShowMainLogo()) {
                        Image(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                                .size(width = 112.dp, height = 44.dp),
                            painter = painterResource(id = drawable.main_logo),
                            contentDescription = stringResource(
                                id = com.bff.wespot.message.R.string.wespot_logo,
                            ),
                        )
                    }
                },
                action = {
                    when (isTopNavigationScreen) {
                        BarType.DEFAULT -> {
                            IconButton(
                                modifier = Modifier.padding(end = 8.dp),
                                onClick = {
                                    navController.navigateToNavGraph(
                                        navGraph = AppNavGraphs.notification,
                                    )
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = drawable.icn_alarm),
                                    contentDescription = stringResource(
                                        id = com.bff.wespot.designsystem.R.string.notification_icon,
                                    ),
                                )
                            }
                        }
                        BarType.ENTIRE -> {
                            IconButton(
                                modifier = Modifier.padding(end = 8.dp),
                                onClick = {
                                    navController.navigate(
                                        SettingScreenDestination within AppNavGraphs.entire,
                                    )
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = drawable.icn_settings),
                                    contentDescription = stringResource(
                                        id = com.bff.wespot.designsystem.R.string.setting_icon,
                                    ),
                                )
                            }
                        }
                        BarType.MESSAGE -> {
                            Row(modifier = Modifier.padding(end = 16.dp)) {
                                IconButton(
                                    onClick = {
                                        navController.navigateToNavGraph(
                                            navGraph = AppNavGraphs.notification,
                                        )
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = drawable.icn_alarm),
                                        contentDescription = stringResource(
                                            id = com.bff.wespot.designsystem.R.string.notification_icon,
                                        ),
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        navController.navigate(
                                            MessageSettingScreenDestination within AppNavGraphs.message,
                                        )
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = drawable.icn_settings),
                                        contentDescription = stringResource(
                                            id = com.bff.wespot.designsystem.R.string.setting_icon,
                                        ),
                                    )
                                }
                            }
                        }
                        BarType.NONE -> { }
                    }
                },
            )
        }
    }
}
