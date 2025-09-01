package com.bff.wespot.community.report

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.bff.wespot.community.report.screen.CommunityReportScreen
import com.bff.wespot.community.report.state.CommunityReportAction
import com.bff.wespot.community.report.state.CommunityReportSideEffect
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.ui.util.handleSideEffect
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class CommunityReportActivity : ComponentActivity() {
    private val viewModel: CommunityReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.collectAsState()
            val onAction = viewModel::onAction

            WeSpotTheme {
                CommunityReportScreen(
                    uiState = state,
                    onAction = onAction,
                )
            }

            LaunchedEffect(Unit) {
                onAction(CommunityReportAction.LoadReportReasons)
            }

            handleSideEffect(viewModel.sideEffect)

            viewModel.collectSideEffect { sideEffect ->
                when (sideEffect) {
                    is CommunityReportSideEffect.OnBackClick -> {
                        finish()
                    }

                    is CommunityReportSideEffect.OnReportSuccess -> {
                        val resultIntent = Intent().apply {
                            putExtra("reportSuccess", true)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        }
    }
}
