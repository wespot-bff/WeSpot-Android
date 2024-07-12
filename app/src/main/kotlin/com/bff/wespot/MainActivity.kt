package com.bff.wespot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bff.wespot.auth.screen.CompleteScreen
import com.bff.wespot.designsystem.theme.WeSpotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeSpotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CompleteScreen()
                }
            }
        }
    }
}
