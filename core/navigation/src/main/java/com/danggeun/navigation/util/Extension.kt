package com.danggeun.navigation.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf

inline fun <reified T : Activity> Context.buildIntent(
    vararg argument: Pair<String, Any?>,
) = Intent(this, T::class.java).apply {
    putExtras(bundleOf(*argument))
}