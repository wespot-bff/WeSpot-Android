package com.bff.wespot

import android.content.Context
import android.content.Intent
import com.danggeun.navigation.Navigator
import com.danggeun.navigation.util.buildIntent
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    override fun navigateToMain(
        context: Context,
    ): Intent {
        val intent = context.buildIntent<MainActivity>()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }
}
