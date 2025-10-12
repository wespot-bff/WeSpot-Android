package com.bff.wespot.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.R
import com.bff.wespot.analytics.AnalyticsHelper
import com.bff.wespot.analytics.LocalAnalyticsHelper
import com.bff.wespot.common.checkCurrentScreen
import com.bff.wespot.common.currentScreenAsState
import com.bff.wespot.common.navigateToNavGraph
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.main.component.BottomNavigationTab
import com.bff.wespot.main.component.MainTopBar
import com.bff.wespot.main.component.OnBoardingSheet
import com.bff.wespot.main.component.RestrictionBottomSheet
import com.bff.wespot.main.model.BarType
import com.bff.wespot.main.model.NavigationBarPosition
import com.bff.wespot.main.model.RestrictionContent
import com.bff.wespot.main.model.VersionUpdateDialogState
import com.bff.wespot.main.state.MainAction
import com.bff.wespot.main.state.MainSideEffect
import com.bff.wespot.main.viewmodel.MainViewModel
import com.bff.wespot.model.common.RestrictionType
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.notification.PushNotificationData
import com.bff.wespot.navigation.AppNavigation
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.navigator.NotificationNavigatorImpl
import com.bff.wespot.ui.component.TopToast
import com.bff.wespot.ui.model.ToastState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MainScreen(
    viewModel: MainViewModel,
    navigator: Navigator,
    data: PushNotificationData,
    analyticsHelper: AnalyticsHelper,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    val navController = rememberNavController()
    val context = LocalContext.current
    var toast by remember { mutableStateOf(ToastState()) }
    var showVersionUpdateDialog by remember { mutableStateOf(VersionUpdateDialogState()) }

    val isTopNavigationScreen by navController.checkCurrentScreen(NavigationBarPosition.TOP)
    val isBottomNavigationScreen by navController.checkCurrentScreen(NavigationBarPosition.BOTTOM)
    val notificationNavigator = remember(navController) {
        NotificationNavigatorImpl(navController)
    }

    viewModel.collectSideEffect {
        when (it) {
            is MainSideEffect.ShowVersionUpdateDialog -> {
                showVersionUpdateDialog = VersionUpdateDialogState(
                    show = true,
                    versionUpdateType = it.versionUpdateType,
                )
            }
            is MainSideEffect.NavigateFromPushNotification -> {
                notificationNavigator.navigate(context, it.data)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopBar(
                isTopNavigationScreen = isTopNavigationScreen,
                navController = navController,
            )
        },
        bottomBar = {
            AnimatedContent(
                targetState = isBottomNavigationScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween()) togetherWith fadeOut(animationSpec = tween())
                },
                label = stringResource(R.string.top_bar_animated_content_label),
            ) { targetState ->
                if (targetState == BarType.DEFAULT) {
                    val currentSelectedItem by navController.currentScreenAsState()
                    BottomNavigationTab(
                        selectedNavigation = currentSelectedItem,
                        onNavigationSelected = { selected ->
                            navController.navigateToNavGraph(selected)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    ) {
        CompositionLocalProvider(
            LocalAnalyticsHelper provides analyticsHelper,
        ) {
            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(it),
                navigator = navigator,
                showToast = { toastState -> toast = toastState },
                restricted = state.restriction.restrictionType != RestrictionType.NONE,
            )
        }
    }

    TopToast(
        message = stringResource(toast.message),
        toastType = toast.type,
        showToast = toast.show,
    ) {
        toast = toast.copy(show = false)
    }

    if (showVersionUpdateDialog.show) {
        WSDialog(
            title = showVersionUpdateDialog.versionUpdateType.title,
            subTitle = showVersionUpdateDialog.versionUpdateType.subTitle,
            okButtonText = stringResource(R.string.update),
            cancelButtonText = stringResource(R.string.next_time_update),
            okButtonClick = {
                navigator.navigateToWebLink(context, state.playStoreLink)
            },
            cancelButtonClick = {
                showVersionUpdateDialog = showVersionUpdateDialog.copy(show = false)
            },
            onDismissRequest = {},
        )
    }

    if (state.restriction.restrictionType != RestrictionType.NONE) {
        RestrictionBottomSheet(
            content = when (state.restriction.restrictionType) {
                RestrictionType.TEMPORARY_BAN_MESSAGE_REPORT -> RestrictionContent.TYPE1
                RestrictionType.PERMANENT_BAN_MESSAGE_REPORT -> RestrictionContent.TYPE2
                RestrictionType.PERMANENT_BAN_VOTE_REPORT -> RestrictionContent.TYPE3
                else -> RestrictionContent.TYPE1
            },
            state = state,
            navigator = navigator,
        )
    }

    OnBoardingSheet(
        state = state,
        navController = navController,
        action = action,
    )

    LaunchedEffect(Unit) {
        val versinName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        action(MainAction.OnMainScreenEntered(appVersionName = versinName))
    }

    LaunchedEffect(data) {
        if (data.type != NotificationType.IDLE) {
            action(MainAction.OnEnteredByPushNotification(data))
        }
    }
}
