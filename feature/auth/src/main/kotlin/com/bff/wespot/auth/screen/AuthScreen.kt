package com.bff.wespot.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@Destination
@RootNavGraph(start = true)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Button(
            onClick = {
                viewModel.onAction(AuthAction.LoginWithKakao)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 60.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFFEE500)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFEE500),
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.kakao),
                contentDescription = stringResource(
                    id = R.string.kakao_icon,
                ),
                tint = Color.Black,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.continue_with_kakao),
                style = StaticTypeScale.Default.body4.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
            )
        }
    }
}
