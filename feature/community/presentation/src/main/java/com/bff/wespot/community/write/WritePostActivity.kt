package com.bff.wespot.community.write

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.bff.wespot.community.write.screen.WritePostScreen
import com.bff.wespot.designsystem.theme.WeSpotTheme
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState

@AndroidEntryPoint
class WritePostActivity : ComponentActivity() {
    private val viewModel: WritePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.collectAsState()
            val onAction = viewModel::onAction

            WeSpotTheme {
                WritePostScreen(
                    onAction = onAction,
                    uiState = uiState,
                )
            }
        }
    }
}
