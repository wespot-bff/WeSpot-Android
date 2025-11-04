package com.bff.wespot.main.component

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.main.model.BottomBarDestinations
import com.bff.wespot.ui.util.clickableSingle
import com.ramcosta.composedestinations.spec.NavGraphSpec

@Composable
internal fun BottomNavigationTab(
    selectedNavigation: NavGraphSpec,
    onNavigationSelected: (NavGraphSpec) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = WeSpotThemeManager.colors.naviColor,
        modifier = modifier.navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(69.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarDestinations.entries.forEach { destination ->
                TabItem(
                    icon = painterResource(id = destination.icon),
                    emptyIcon = painterResource(id = destination.emptyIcon),
                    title = stringResource(id = destination.title),
                    description = stringResource(id = destination.title),
                    selected = selectedNavigation == destination.screen,
                ) {
                    onNavigationSelected(destination.screen)
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabItem(
    icon: Painter,
    emptyIcon: Painter,
    title: String,
    description: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    var hapticEngine by remember { mutableStateOf<VibrationEffect?>(null) }

    LaunchedEffect(key1 = Unit) {
        val numberOfPulses = 1
        val pulseDuration = 40L
        val maxAmplitude = 255

        val timings = LongArray(numberOfPulses * 2)
        val amplitudes = IntArray(numberOfPulses * 2)

        for (i in 0 until numberOfPulses) {
            val amplitude =
                (maxAmplitude * (i + 1) / numberOfPulses)
            timings[i * 2 + 1] = pulseDuration
            amplitudes[i * 2] = 0
            amplitudes[i * 2 + 1] = amplitude
        }

        hapticEngine = VibrationEffect.createWaveform(timings, amplitudes, -1)
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .clickableSingle {
                hapticEngine?.let {
                    val vibrate = context.getSystemService(Vibrator::class.java)
                    vibrate.vibrate(it)
                }
                onClick.invoke()
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = if (selected) {
                    icon
                } else {
                    emptyIcon
                },
                contentDescription = description,
            )

            Text(
                text = title,
                style = StaticTypeScale.Default.body10,
                color = if (selected) {
                    WeSpotThemeManager.colors.abledIconColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
            )
        }
    }
}
