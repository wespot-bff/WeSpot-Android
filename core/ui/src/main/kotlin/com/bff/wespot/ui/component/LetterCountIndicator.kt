package com.bff.wespot.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@Composable
fun LetterCountIndicator(
    currentCount: Int,
    maxCount: Int,
    paddingValues: PaddingValues = PaddingValues(top = 4.dp, start = 10.dp, end = 10.dp),
) {
    Text(
        modifier = Modifier.padding(paddingValues),
        text = "$currentCount / $maxCount",
        style = StaticTypeScale.Default.body7,
        color = if (currentCount <= maxCount) Gray400 else WeSpotThemeManager.colors.dangerColor,
    )
}

@Composable
@Preview
private fun PreviewLetterCountIndicator() {
    WeSpotTheme {
        Column {
            LetterCountIndicator(currentCount = 100, maxCount = 200)
        }
    }
}
