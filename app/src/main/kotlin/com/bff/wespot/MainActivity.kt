package com.bff.wespot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.danggeun.vote.screen.VoteHomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeSpotTheme {
                VoteHomeScreen()
            }
        }
    }
}
