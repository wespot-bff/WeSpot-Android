package com.bff.wespot.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface Navigator {
    fun navigateToMain(
        context: Context,
        type: Pair<String, String> = Pair("", ""),
        date: Pair<String, String> = Pair("", ""),
        deepLink: Pair<String, String> = Pair("", ""),
    ): Intent

    fun navigateToAuth(context: Context): Intent

    fun navigateToAuthWithExtra(
        context: Context,
        type: Pair<String, String> = Pair("", ""),
        date: Pair<String, String> = Pair("", ""),
        deepLink: Pair<String, String> = Pair("", ""),
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

    fun navigateToWriteActivity(context: Context, category: String? = null): Intent

    fun navigateToEditPostActivity(
        context: Context,
        postId: String,
        title: String?,
        description: String,
        category: String,
        images: List<String>
    ): Intent

    fun navigateToPostDetailActivity(context: Context, postId: String, scrollToComments: Boolean = false): Intent

    fun navigateToCommunitySearch(context: Context): Intent

    fun navigateToCommunityAll(context: Context): Intent

    fun navigateToCategoryDetail(context: Context, categoryId: String, categoryText: String): Intent
    
    fun navigateToCommunityReport(context: Context, targetId: String, reportType: String): Intent
}
