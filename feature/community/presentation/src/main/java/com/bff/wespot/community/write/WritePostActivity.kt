package com.bff.wespot.community.write

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.bff.wespot.community.write.screen.WritePostScreen
import com.bff.wespot.community.write.state.WritePostSideEffect
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.ui.util.handleSideEffect
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class WritePostActivity : ComponentActivity() {
    private val viewModel: WritePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
        )
        setContent {
            val uiState by viewModel.collectAsState()
            val onAction = viewModel::onAction

            WeSpotTheme {
                WritePostScreen(
                    onAction = onAction,
                    uiState = uiState,
                )
            }

            viewModel.collectSideEffect {
                when (it) {
                    WritePostSideEffect.ClosePage -> {
                        finish()
                    }
                    WritePostSideEffect.ClosePageWithSuccess -> {
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }

            handleSideEffect(viewModel.sideEffect)
        }
    }
}
