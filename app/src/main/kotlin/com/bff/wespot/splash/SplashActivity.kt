package com.bff.wespot.splash

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.PushNotificationService.Companion.KEY_DATE
import com.bff.wespot.PushNotificationService.Companion.KEY_TARGET_ID
import com.bff.wespot.PushNotificationService.Companion.KEY_TYPE
import com.bff.wespot.PushNotificationService.Companion.KEY_USER_ID
import com.bff.wespot.R
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_DATE
import com.bff.wespot.navigation.util.EXTRA_TARGET_ID
import com.bff.wespot.navigation.util.EXTRA_TYPE
import com.bff.wespot.navigation.util.EXTRA_USER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: SplashViewModel by viewModels()
    private val isNavigateFromPushNotification by lazy { intent.getStringExtra(KEY_TYPE) != null }

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
        val targetId = intent.getStringExtra(KEY_TARGET_ID)?.toInt() ?: -1
        val userId = intent.getStringExtra(KEY_USER_ID) ?: ""
        val type = intent.getStringExtra(KEY_TYPE) ?: ""
        val date = intent.getStringExtra(KEY_DATE) ?: ""

        val intent = navigator.navigateToAuthWithExtra(
            context = this@SplashActivity,
            targetId = Pair(EXTRA_TARGET_ID, targetId),
            userId = Pair(EXTRA_USER_ID, userId),
            type = Pair(EXTRA_TYPE, type),
            date = Pair(EXTRA_DATE, date),
        )
        startActivity(intent)
        finish()
    }
}
