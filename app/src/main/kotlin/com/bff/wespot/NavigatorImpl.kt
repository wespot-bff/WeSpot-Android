package com.bff.wespot

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bff.wespot.auth.AuthActivity
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.buildIntent
import timber.log.Timber
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    private val sharingName = listOf(
        "instagram",
        "kakao"
    )

    override fun navigateToMain(
        context: Context,
    ): Intent {
        val intent = context.buildIntent<MainActivity>()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToAuth(context: Context): Intent {
        val intent = context.buildIntent<AuthActivity>()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToSharing(context: Context) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.setType("text/plain")

        val chooser: MutableList<Intent> = mutableListOf()
        val resInfo = context.packageManager.queryIntentActivities(sendIntent, 0)


        if (resInfo.isEmpty()) {
            return
        }

        val instaIntent = Intent(Intent.ACTION_SEND).apply {
            setComponent(
                ComponentName(
                    "com.instagram.android",
                    "com.instagram.direct.share.handler.DirectShareHandlerActivity"
                )
            )
            setType("text/plain")
            putExtra(Intent.EXTRA_SUBJECT, "메시지 제목")
            putExtra(Intent.EXTRA_TEXT, "메시지 내용")
        }

        chooser.add(instaIntent)

        resInfo.forEach {
            val packageName = it.activityInfo.packageName.lowercase()
            val name = it.activityInfo.name.lowercase()

            Timber.d("$packageName $name")
            val containsString =
                sharingName.any { packageName.contains(it.lowercase()) } ||
                        sharingName.any { name.contains(it.lowercase()) }

            if (containsString) {
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "메시지 제목")
                sendIntent.putExtra(Intent.EXTRA_TEXT, "메시지 내용")
                sendIntent.setPackage(it.activityInfo.packageName)
                chooser.add(sendIntent)
            }
        }

        val choser = Intent.createChooser(chooser.removeAt(0), "타이틀")
        choser.putExtra(Intent.EXTRA_INITIAL_INTENTS, chooser.toTypedArray())
        context.startActivity(choser)
    }

}
