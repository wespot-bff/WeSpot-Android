package com.bff.wespot.navigation

import android.content.Context
import android.content.Intent

interface Navigator {

    fun navigateToMain(context: Context) : Intent

    fun navigateToAuth(context: Context): Intent

    fun navigateToSharing(context: Context)
}