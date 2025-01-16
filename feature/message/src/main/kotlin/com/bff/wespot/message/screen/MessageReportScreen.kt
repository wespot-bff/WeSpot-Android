package com.bff.wespot.message.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.model.ReportReason
import com.bff.wespot.message.state.report.ReportAction
import com.bff.wespot.message.state.report.ReportSideEffect
import com.bff.wespot.message.viewmodel.ReportViewModel
import com.bff.wespot.ui.component.ListBottomGradient
import com.bff.wespot.ui.component.WSSelectionItem
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface MessageReportNavigator {
    fun navigateUp()
    fun popUpToMessageScreen()
}

data class MessageReportScreenArgs(
    val messageId: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = MessageReportScreenArgs::class)
@Composable
fun MessageReportScreen(
    viewModel: ReportViewModel = hiltViewModel(),
    showToast: (ToastState) -> Unit,
    navigator: MessageReportNavigator,
) {
    val scrollState = rememberScrollState()

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            ReportSideEffect.NavigateToMessage -> {
                navigator.popUpToMessageScreen()
            }

            is ReportSideEffect.ShowToast -> {
                showToast(
                    ToastState(
                        message = it.message,
                        show = true,
                        type = WSToastType.Success,
                    ),
                )
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.report_title),
                canNavigateBack = true,
                navigateUp = navigator::navigateUp,
            )
        },
    ) { innerPadding ->
        SubcomposeLayout(modifier = Modifier.padding(innerPadding)) { constraints ->
            val listGradientPlaceable = subcompose("listGradient") {
                ListBottomGradient(height = 120)
            }.first().measure(constraints)

            val buttonPlaceable = subcompose("button") {
                WSButton(
                    text = stringResource(R.string.choice_done),
                    content = { it() },
                    onClick = {
                        if (state.reportReason.index != -1) {
                            action(ReportAction.OnMessageReportButtonClicked)
                        }
                    },
                )
            }.first().measure(constraints)

            val contentMaxHeight = constraints.maxHeight - buttonPlaceable.height
            val contentPlaceable = subcompose("content") {
                Column(
                    modifier = Modifier.verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp, start = 30.dp, end = 30.dp),
                        text = stringResource(R.string.message_report_title),
                        style = StaticTypeScale.Default.header1,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                    )

                    Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.error),
                                contentDescription = "error_icon",
                            )

                            Text(
                                text = stringResource(id = R.string.notice_warning),
                                style = StaticTypeScale.Default.body3,
                                color = WeSpotThemeManager.colors.txtTitleColor,
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(id = R.string.notice_restriction_warning),
                            style = StaticTypeScale.Default.body6,
                            color = WeSpotThemeManager.colors.txtTitleColor,
                        )
                    }

                    Text(
                        modifier = Modifier.padding(horizontal = 30.dp),
                        text = stringResource(id = R.string.notice_message_block),
                        style = StaticTypeScale.Default.body8,
                        color = WeSpotThemeManager.colors.txtSubColor,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        persistentListOf(
                            stringResource(id = R.string.report_category_leakage),
                            stringResource(id = R.string.report_category_obscenity),
                            stringResource(id = R.string.report_category_abuse),
                            stringResource(id = R.string.report_category_advertisement),
                            stringResource(id = R.string.report_category_custom),
                        ).forEachIndexed { index, reason ->
                            val isUserInputItem = stringResource(R.string.report_category_custom) == reason
                            val selected = index == state.reportReason.index

                            WSSelectionItem(
                                title = if (isUserInputItem && selected) state.inputReportReason else reason,
                                selected = selected,
                                isEditable = isUserInputItem,
                                onTitleChanged = {
                                    action(ReportAction.OnReportReasonChanged(it))
                                },
                                onClick = {
                                    action(ReportAction.OnReportReasonSelected(ReportReason(index, reason)))
                                },
                            )
                        }
                    }
                }
            }.first().measure(constraints.copy(maxHeight = contentMaxHeight))

            layout(constraints.maxWidth, constraints.maxHeight) {
                contentPlaceable.placeRelative(0, 0)

                listGradientPlaceable.placeRelative(0, constraints.maxHeight - listGradientPlaceable.height)

                buttonPlaceable.placeRelative(0, contentMaxHeight)
            }
        }
    }
}
