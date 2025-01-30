package com.bff.wespot.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.R
import com.bff.wespot.analytic.TrackScreenViewEvent
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.state.featureoverview.FeatureOverviewAction
import com.bff.wespot.state.featureoverview.FeatureOverviewSideEffect
import com.bff.wespot.ui.util.handleSideEffect
import com.bff.wespot.viewmodel.FeatureOverviewViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureOverviewDialog(
    viewModel: FeatureOverviewViewModel = hiltViewModel(),
    notificationType: NotificationType,
    onDismissButtonClicked: () -> Unit,
    onNavigateButtonClicked: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            is FeatureOverviewSideEffect.NavigateScreen -> onNavigateButtonClicked(it.deepLink)
            FeatureOverviewSideEffect.DismissDialog -> onDismissButtonClicked()
        }
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Scaffold(
            topBar = {
                WSTopBar(
                    title = state.ui.headerText.text,
                    canNavigateBack = true,
                    navigateUp = {
                        action(FeatureOverviewAction.OnDismissButtonClicked)
                    },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .let {
                            if (state.ui.overview.isFillMaxWidth()) {
                                it.fillMaxWidth()
                            } else {
                                it.width(state.ui.overview.width.dp)
                            }
                            if (state.ui.overview.isFillMaxHeight()) {
                                it.fillMaxHeight()
                            } else {
                                it.height(state.ui.overview.height.dp)
                            }
                        },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.ui.overview.url)
                        .error(com.bff.wespot.designsystem.R.drawable.default_image)
                        .fallback(com.bff.wespot.designsystem.R.drawable.default_image)
                        .placeholder(com.bff.wespot.designsystem.R.drawable.default_image)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = stringResource(R.string.feature_overview_description),
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    WSButton(
                        buttonType = WSButtonType.Secondary,
                        text = state.ui.dismissButton.text,
                        paddingValues = PaddingValues(0.dp),
                        onClick = { action(FeatureOverviewAction.OnDismissButtonClicked) },
                        content = { it() },
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    WSButton(
                        buttonType = WSButtonType.Primary,
                        text = state.ui.navigateButton.text,
                        paddingValues = PaddingValues(0.dp),
                        onClick = {
                            action(
                                FeatureOverviewAction.OnNavigateButtonClicked(
                                    state.ui.navigateButton.link,
                                ),
                            )
                        },
                        content = { it() },
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        action(FeatureOverviewAction.OnFeatureOverViewDialogShow(notificationType))
    }

    TrackScreenViewEvent(screenName = "feature_overview_screen")
}
