package com.bff.wespot.server.driven.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.analytic.TrackScreenViewEvent
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.serverDriven.ButtonListComponent
import com.bff.wespot.model.serverDriven.ChipComponent
import com.bff.wespot.model.serverDriven.DescriptionComponent
import com.bff.wespot.model.serverDriven.DescriptionImageComponent
import com.bff.wespot.model.serverDriven.ImageComponent
import com.bff.wespot.model.serverDriven.SubTitleComponent
import com.bff.wespot.model.serverDriven.TitleComponent
import com.bff.wespot.model.serverDriven.TopBarComponent
import com.bff.wespot.server.driven.R
import com.bff.wespot.server.driven.component.ButtonListSection
import com.bff.wespot.server.driven.component.ChipSection
import com.bff.wespot.server.driven.component.DescriptionSection
import com.bff.wespot.server.driven.component.ImageSection
import com.bff.wespot.server.driven.component.TitleSection
import com.bff.wespot.server.driven.component.TopBarSection
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.util.handleSideEffect
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UpdateOverviewScreen(
    viewModel: UpdateOverviewViewModel = hiltViewModel(),
    notificationType: NotificationType,
    onDismiss: () -> Unit,
    onNavigate: (String) -> Unit,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            is UpdateOverviewSideEffect.NavigateDeepLink -> onNavigate(it.deepLink)
            UpdateOverviewSideEffect.DismissDialog -> onDismiss()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.contents) { content ->
            when (content) {
                is TopBarComponent -> {
                    TopBarSection(
                        title = content.text,
                        canNavigateBack = true,
                        navigateUp = {
                            action(UpdateOverviewAction.OnNavigateUpButtonClicked)
                        },
                    )
                }

                is TitleComponent -> {
                    TitleSection(
                        title = content.text,
                        textAlign = TextAlign.Start,
                        paddingValues = PaddingValues(horizontal = 30.dp),
                    )
                }

                is SubTitleComponent -> {
                    DescriptionSection(
                        text = content.text,
                        color = Gray400,
                        paddingValues = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
                    )
                }

                is ImageComponent -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        ImageSection(
                            imageUrl = content.url,
                            width = content.width,
                            height = content.height,
                            enableTilt = false,
                            contentDescription = stringResource(R.string.update_overview_description_image),
                            paddingValues = PaddingValues(vertical = 32.dp),
                        )
                    }
                }

                is ChipComponent -> {
                    ChipSection(
                        text = content.text,
                        paddingValues = PaddingValues(horizontal = 30.dp),
                    )
                }

                is DescriptionComponent -> {
                    DescriptionSection(
                        text = content.text,
                        color = Gray200,
                        paddingValues = PaddingValues(horizontal = 30.dp, vertical = 16.dp),
                    )
                }

                is DescriptionImageComponent -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        ImageSection(
                            imageUrl = content.url,
                            width = content.width,
                            height = content.height,
                            enableTilt = false,
                            contentDescription = stringResource(R.string.update_overview_description_image),
                            paddingValues = PaddingValues(vertical = 24.dp),
                        )
                    }
                }

                is ButtonListComponent -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        ButtonListSection(
                            buttonList = content.buttonList,
                            onClick = {
                                action(UpdateOverviewAction.OnButtonClicked(it))
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(70.dp))
        }
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    LaunchedEffect(Unit) {
        action(UpdateOverviewAction.OnScreenEntered(notificationType))
    }

    TrackScreenViewEvent(screenName = "update_overview_screen")
}
