package com.bff.wespot.splash

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.R
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_DATE
import com.bff.wespot.navigation.util.EXTRA_TARGET_ID
import com.bff.wespot.navigation.util.EXTRA_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
                            delay(500)
                            navigateToAuth()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToAuth() {
        val targetId = intent.getStringExtra("targetId")?.toInt() ?: -1
        val date = intent.getStringExtra("date") ?: ""
        val type = intent.getStringExtra("type") ?: ""

        val intent = navigator.navigateToAuthWithExtra(
            context = this@SplashActivity,
            targetId = Pair(EXTRA_TARGET_ID, targetId),
            date = Pair(EXTRA_DATE, date),
            type = Pair(EXTRA_TYPE, type),
        )
        startActivity(intent)
        finish()
    }
}
