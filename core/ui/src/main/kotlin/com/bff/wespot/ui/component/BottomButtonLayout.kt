package com.bff.wespot.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout

@Composable
fun BottomButtonLayout(
    modifier: Modifier = Modifier,
    showGradient: Boolean,
    button: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        val gradientPlaceable = subcompose("gradient") {
            ListBottomGradient(124)
        }.first().measure(constraints)

        val buttonPlaceable = subcompose("button") {
            button()
        }.first().measure(constraints)

        val contentMaxHeight = constraints.maxHeight - buttonPlaceable.height
        val contentPlaceable = subcompose("content") {
            content()
        }.first().measure(constraints.copy(maxHeight = contentMaxHeight))

        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlaceable.placeRelative(0, 0)

            if (showGradient) {
                gradientPlaceable.placeRelative(0, constraints.maxHeight - gradientPlaceable.height)
            }

            buttonPlaceable.placeRelative(0, contentMaxHeight)
        }
    }
}
