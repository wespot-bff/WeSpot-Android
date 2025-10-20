package com.bff.wespot.community.report.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.bff.wespot.analytics.AnalyticsHelper
import com.bff.wespot.analytics.LocalAnalyticsHelper
import com.bff.wespot.analytics.TrackScreenViewEvent
import com.bff.wespot.analytics.logClick
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.report.state.CommunityReportAction
import com.bff.wespot.community.report.state.CommunityReportUiState
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.community.ReportReason

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityReportScreen(
    uiState: CommunityReportUiState,
    onAction: (CommunityReportAction) -> Unit,
) {
    val analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
    val colors = WeSpotThemeManager.colors
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            WSTopBar(
                titleContent = {
                    Text(
                        text = stringResource(R.string.postdetail_report),
                        style = StaticTypeScale.Default.header2,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                    )
                },
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    onAction(CommunityReportAction.OnBackClick)
                },
            )
        },
        bottomBar = {
            val hasSelectedReasons = uiState.selectedReasonIds.isNotEmpty()
            val hasCustomReport = uiState.customReportTexts.isNotEmpty()
            val canSubmit = (hasSelectedReasons || hasCustomReport) && !uiState.isSubmitting

            WSButton(
                onClick = {
                    analyticsHelper.logClick(
                        name = "click_choose_report_reason",
                        extras = uiState.getSelectedReasonParams(),
                    )
                    onAction(CommunityReportAction.OnSubmitReport)
                },
                enabled = canSubmit,
                text = stringResource(com.bff.wespot.ui.R.string.complete),
            ) {
                it.invoke()
            }
        },
        containerColor = colors.backgroundColor,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colors.primaryColor,
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) {
                                focusManager.clearFocus()
                            },
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 30.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.report_select_reason),
                                style = StaticTypeScale.Default.header1,
                                color = WeSpotThemeManager.colors.txtTitleColor,
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(com.bff.wespot.designsystem.R.drawable.error),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                )

                                Text(
                                    text = stringResource(R.string.report_caution),
                                    style = StaticTypeScale.Default.body3,
                                    color = WeSpotThemeManager.colors.txtTitleColor,
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stringResource(R.string.post_caution_description),
                                style = StaticTypeScale.Default.body6,
                                color = WeSpotThemeManager.colors.txtTitleColor,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.report_description),
                                style = StaticTypeScale.Default.body8,
                                color = WeSpotThemeManager.colors.txtSubColor,
                            )
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(vertical = 26.dp, horizontal = 20.dp),
                        ) {
                            items(uiState.reportReasons) { reason ->
                                if (reason.isReasonEditable) {
                                    CustomReportReasonItem(
                                        reasonId = reason.id,
                                        placeholder = reason.reason,
                                        value = uiState.customReportTexts[reason.id] ?: "",
                                        onValueChange = { text ->
                                            onAction(
                                                CommunityReportAction.OnCustomTextChanged(
                                                    reason.id,
                                                    text,
                                                ),
                                            )
                                        },
                                        focusRequester = focusRequester,
                                        onSelect = {
                                            val currentText =
                                                uiState.customReportTexts[reason.id] ?: ""
                                            if (currentText.isNotEmpty()) {
                                                onAction(
                                                    CommunityReportAction.OnCustomTextChanged(
                                                        reason.id,
                                                        "",
                                                    ),
                                                )
                                            }
                                        },
                                    )
                                } else {
                                    ReportReasonItem(
                                        reason = reason,
                                        isSelected = uiState.selectedReasonIds.contains(reason.id),
                                        onSelect = {
                                            onAction(
                                                CommunityReportAction.OnReasonSelected(
                                                    reason.id,
                                                ),
                                            )
                                            focusManager.clearFocus()
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    TrackScreenViewEvent("community_report")
}

@Composable
private fun toggleCheckIcon(
    checked: Boolean,
): Painter = if (checked) {
    rememberAsyncImagePainter(model = R.drawable.checked)
} else {
    rememberAsyncImagePainter(model = R.drawable.unchecked)
}

@Composable
private fun ReportReasonItem(
    reason: ReportReason,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = WeSpotThemeManager.colors.cardBackgroundColor,
                shape = RoundedCornerShape(size = 12.dp),
            ).clickable {
                onSelect.invoke()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
        ) {
            Image(
                painter = toggleCheckIcon(isSelected),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )

            Text(
                text = reason.reason,
                color = WeSpotThemeManager.colors.txtTitleColor,
                style = StaticTypeScale.Default.body3,
            )
        }
    }
}

@Composable
private fun CustomReportReasonItem(
    reasonId: Int,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onSelect: () -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val colors = WeSpotThemeManager.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.cardBackgroundColor,
                shape = RoundedCornerShape(size = 12.dp),
            ).then(
                if (isFocused) {
                    Modifier.border(
                        width = 1.dp,
                        color = Primary400,
                        shape = RoundedCornerShape(size = 12.dp),
                    )
                } else {
                    Modifier
                },
            ).clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                focusRequester.requestFocus()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
        ) {
            Image(
                painter = toggleCheckIcon(value.isNotEmpty()),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onSelect.invoke()
                    },
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = StaticTypeScale.Default.body3.copy(
                    color = colors.txtTitleColor,
                ),
                singleLine = true,
                cursorBrush = SolidColor(Primary400),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = StaticTypeScale.Default.body3,
                                color = Gray400,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}
