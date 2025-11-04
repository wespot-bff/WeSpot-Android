package com.bff.wespot.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bff.wespot.analytics.AnalyticsHelper
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.main.state.MainAction
import com.bff.wespot.main.viewmodel.MainViewModel
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.notification.PushNotificationData
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_DATE
import com.bff.wespot.navigation.util.EXTRA_DEEP_LINK
import com.bff.wespot.navigation.util.EXTRA_TYPE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val notificationPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            viewModel.onAction(MainAction.OnNotificationSet(isGranted))
        }
    }

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
        )
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        setContent {
            WeSpotTheme {
                MainScreen(
                    navigator = navigator,
                    data = getPushNotificationData(),
                    analyticsHelper = analyticsHelper,
                    viewModel = viewModel,
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun getPushNotificationData(): PushNotificationData = with(intent) {
        val type = NotificationType.convertNotificationType(getStringExtra(EXTRA_TYPE).orEmpty())
        val date = getStringExtra(EXTRA_DATE).orEmpty()
        val deepLink = getStringExtra(EXTRA_DEEP_LINK).orEmpty()

        removeExtra(EXTRA_TYPE)
        removeExtra(EXTRA_DATE)
        removeExtra(EXTRA_DEEP_LINK)

        PushNotificationData(
            type = type,
            date = date,
            deepLink = deepLink,
        )
    }
}
