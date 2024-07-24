package com.bff.wespot.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.util.OrientationPreviews
import com.bff.wespot.util.carouselTransition

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WSCarousel(
    pageCount: Int = 10,
    pagerState: PagerState = rememberPagerState(pageCount = { pageCount }),
    contentPadding: PaddingValues,
    pageSpacing: Dp,
    carouselItem: @Composable (Int) -> Unit,
) {
    Box {
        HorizontalPager(
            state = pagerState,
            contentPadding = contentPadding,
            pageSpacing = pageSpacing
        ) {
            carouselItem.invoke(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@OrientationPreviews
@Composable
private fun PreviewWSCarousel() {
    val pagerState = rememberPagerState(pageCount = { 10 })

    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                WSCarousel(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    pageSpacing = 12.dp,
                    pagerState = pagerState
                ) {
                    Box(modifier = Modifier.wrapContentSize()) {
                        Text("Item 1", modifier = Modifier.carouselTransition(it, pagerState))
                    }
                }
                DotIndicators(pagerState = pagerState, gapSize = 32.dp)
            }
        }
    }
}