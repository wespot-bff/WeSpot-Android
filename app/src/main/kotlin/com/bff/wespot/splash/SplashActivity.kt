package com.bff.wespot.splash

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.notification.PushNotificationService
import com.bff.wespot.R
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_DATE
import com.bff.wespot.navigation.util.EXTRA_DEEP_LINK
import com.bff.wespot.navigation.util.EXTRA_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: SplashViewModel by viewModels()
    private val isNavigateFromPushNotification by lazy {
        intent.getStringExtra(PushNotificationService.KEY_TYPE) != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Push Notification으로 접근 시, setOnExitAnimationListener이 호출되지 않는다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isNavigateFromPushNotification) {
            installSplashScreen().apply {
                setKeepOnScreenCondition {
                    return@setKeepOnScreenCondition viewModel.start.value.not()
                }
                setOnExitAnimationListener {
                    navigateToAuth()
                }
            }
        } else {
            setContentView(R.layout.activity_splash)
            actionBar?.hide()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.start.collect {
                        if (it) {
                            navigateToAuth()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToAuth() {
        val type = intent.getStringExtra(PushNotificationService.KEY_TYPE) ?: ""
        val date = intent.getStringExtra(PushNotificationService.KEY_DATE) ?: ""
        val deepLink = intent.getStringExtra(PushNotificationService.KEY_DEEP_LINK) ?: ""

        val intent = navigator.navigateToAuthWithExtra(
            context = this@SplashActivity,
            type = Pair(EXTRA_TYPE, type),
            date = Pair(EXTRA_DATE, date),
            deepLink = Pair(EXTRA_DEEP_LINK, deepLink)
        )
        startActivity(intent)
        finish()
    }
}
