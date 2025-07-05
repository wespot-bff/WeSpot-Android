package com.bff.wespot.server.driven.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.model.serverDriven.BaseComponent
import com.bff.wespot.model.serverDriven.ButtonsComponent
import com.bff.wespot.model.serverDriven.ImageComponent
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import com.bff.wespot.model.serverDriven.TextComponent
import com.bff.wespot.model.serverDriven.TextListComponent
import com.bff.wespot.model.serverDriven.section.BaseSection
import com.bff.wespot.model.serverDriven.section.BottomSection
import com.bff.wespot.model.serverDriven.section.ContentSection
import com.bff.wespot.server.driven.component.ButtonsSection
import com.bff.wespot.server.driven.component.ImageSection
import com.bff.wespot.server.driven.component.TextListSection
import com.bff.wespot.server.driven.component.TextSection
import com.bff.wespot.server.driven.onboarding.state.OnBoardingNotificationAction
import com.bff.wespot.server.driven.onboarding.state.OnBoardingSideEffect
import kotlinx.coroutines.launch

@Composable
fun OnBoardingBottomSheet(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    category: OnBoardingCategory,
    closeOnBoarding: () -> Unit,
) {
    val contents by viewModel.contents.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { contents.size }

    val action = viewModel::onAction

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        userScrollEnabled = false,
    ) { page ->
        OnBoardingPage(sections = contents[page].data) {
            if (page == contents.size - 1) {
                action(OnBoardingNotificationAction.ViewedOnBoarding(category))
                closeOnBoarding.invoke()
            } else {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page + 1)
                }
            }
        }
    }

    LaunchedEffect(category) {
        action(OnBoardingNotificationAction.GetOnBoarding(category))
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect {
            when (it) {
                is OnBoardingSideEffect.CloseOnBoarding -> {
                    closeOnBoarding.invoke()
                }
            }
        }
    }
}

@Composable
private fun OnBoardingPage(
    sections: List<BaseSection>,
    onClick: () -> Unit,
) {
    Column {
        sections.forEach {
            when (it) {
                is ContentSection -> {
                    ContentPart(
                        components = it.components,
                    )
                }

                is BottomSection -> {
                    BottomPart(
                        section = it,
                        onClick = onClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentPart(
    components: List<BaseComponent>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, top = 40.dp, end = 32.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(components) {
            when (it) {
                is TextComponent -> {
                    TextSection(
                        richText = it.richText,
                        paddings = it.paddings,
                    )
                }

                is ImageComponent -> {
                    ImageSection(
                        imageUrl = it.url,
                        width = it.width,
                        height = it.height,
                        paddings = it.paddings,
                    )
                }

                is TextListComponent -> {
                    TextListSection(
                        textList = it.textList,
                        paddings = it.paddings,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomPart(
    section: BottomSection,
    onClick: () -> Unit,
) {
    LazyColumn {
        items(section.components) {
            when (it) {
                is ButtonsComponent -> {
                    ButtonsSection(
                        buttonsComponent = it,
                        onClick = listOf(onClick),
                        paddings = it.paddings,
                    )
                }
            }
        }
    }
}
