package com.bff.wespot.server.driven.onboarding

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
import com.bff.wespot.model.serverDriven.ButtonComponent
import com.bff.wespot.model.serverDriven.ImageComponent
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import com.bff.wespot.model.serverDriven.TextListComponent
import com.bff.wespot.model.serverDriven.TitleComponent
import com.bff.wespot.server.driven.component.ButtonSection
import com.bff.wespot.server.driven.component.ImageSection
import com.bff.wespot.server.driven.component.TextListSection
import com.bff.wespot.server.driven.component.TitleSection
import kotlinx.coroutines.launch

@Composable
fun OnBoardingBottomSheet(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    category: OnBoardingCategory,
    closeOnBoarding: () -> Unit
) {
    val contents by viewModel.contents.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { contents.size }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        userScrollEnabled = false,
    ) { page ->
        OnBoardingPage(contents[page].data) {
            if (page == contents.size - 1) {
                closeOnBoarding.invoke()
            } else {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page + 1)
                }
            }
        }
    }

    LaunchedEffect(category) {
        viewModel.getOnBoarding(category)
    }
}

@Composable
private fun OnBoardingPage(
    contents: List<BaseComponent>,
    onClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 40.dp, end = 20.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(contents) { content ->
            when (content) {
                is TitleComponent -> {
                    TitleSection(title = content.text)
                }

                is TextListComponent -> {
                    TextListSection(textList = content.textList)
                }

                is ImageComponent -> {
                    ImageSection(
                        imageUrl = content.url,
                        width = content.width,
                        height = content.height,
                    )
                }

                is ButtonComponent -> {
                    ButtonSection(text = content.text, onClick = onClick)
                }
            }
        }
    }
}
