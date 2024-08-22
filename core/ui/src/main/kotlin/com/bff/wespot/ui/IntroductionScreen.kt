package com.bff.wespot.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.theme.StaticTypeScale
import kotlinx.coroutines.delay

@Composable
fun IntroductionScreen(
    name: String,
    introduction: String,
    onTextChange: (String) -> Unit,
    onEditComplete: () -> Unit,
    error: Boolean,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.introduce_to_friend, name),
            style = StaticTypeScale.Default.header1,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            WsTextField(
                value = introduction,
                onValueChange = onTextChange,
                placeholder = stringResource(R.string.introduction_placeholder),
                focusRequester = focusRequester,
                singleLine = true,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            LetterCountIndicator(currentCount = introduction.length, maxCount = 20)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            WSButton(
                onClick = {
                    onEditComplete()
                },
                enabled = introduction.isNotEmpty() && !error,
                text = stringResource(R.string.edit_complete),
            ) {
                it()
            }
        }
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }
}
