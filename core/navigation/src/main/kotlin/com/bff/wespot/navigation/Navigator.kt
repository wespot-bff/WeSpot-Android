package com.bff.wespot.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface Navigator {
    fun navigateToMain(
        context: Context,
        targetId: Pair<String, Int>,
        date: Pair<String, String>,
        type: Pair<String, String>,
    ): Intent

    fun navigateToAuth(context: Context): Intent

    fun navigateToAuthWithExtra(
        context: Context,
        targetId: Pair<String, Int>,
        date: Pair<String, String>,
        type: Pair<String, String>,
    ): Intent

    fun navigateToInstaStory(
        context: Context,
        file: Uri,
    ): Intent

    fun navigateToSharing(context: Context, text: String)

    fun navigateToKakao(
        context: Context,
        title: String,
        description: String,
        imageUrl: String,
        buttonText: String,
        url: String,
    )

    fun redirectToPlayStoreForInstagram(context: Context)

    fun navigateToWebLink(context: Context, webLink: String)
}
