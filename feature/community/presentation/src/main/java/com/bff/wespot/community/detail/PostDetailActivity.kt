package com.bff.wespot.community.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.bff.wespot.community.detail.screen.PostDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState

@AndroidEntryPoint
class PostDetailActivity : ComponentActivity() {
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.collectAsState()
            PostDetailScreen(
                uiModel = state.detail.content,
                comments = state.comments,
            )
        }
    }
}
