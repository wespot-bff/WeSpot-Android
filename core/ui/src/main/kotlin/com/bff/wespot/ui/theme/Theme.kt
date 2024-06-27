package com.bff.wespot.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

private val CustomDarkColors =
    CustomColors(
        primaryColor = Primary300,
        toastColor = White,
        txtTitleColor = Gray100,
        abledTxtColor = Gray100,
        abledIconColor = Gray200,
        txtSubColor = Gray300,
        disableIcnColor = Gray400,
        disableBtnTxtColor = Gray400,
        secondaryBtnColor = Gray400,
        primaryBtnColor = Gray500,
        disableBtnColor = Gray500,
        badgeColor = Gray500,
        modalColor = Gray600,
        cardBackgroundColor = Gray700,
        tertiaryBtnColor = Gray700,
        naviColor = Gray800,
        backgroundColor = Gray900,
        dangerColor = Destructive,
        alertBadgeColor = Destructive,
        toggleColor = Positive,
    )

private val CustomLightColors =
    CustomColors(
        primaryColor = Primary300,
        toastColor = White,
        txtTitleColor = Gray100,
        abledTxtColor = Gray100,
        abledIconColor = Gray200,
        txtSubColor = Gray300,
        disableIcnColor = Gray400,
        disableBtnTxtColor = Gray400,
        secondaryBtnColor = Gray400,
        primaryBtnColor = Gray500,
        disableBtnColor = Gray500,
        badgeColor = Gray500,
        modalColor = Gray600,
        cardBackgroundColor = Gray700,
        tertiaryBtnColor = Gray700,
        naviColor = Gray800,
        backgroundColor = Gray900,
        dangerColor = Destructive,
        alertBadgeColor = Destructive,
        toggleColor = Positive,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = CustomDarkColors.primaryColor,
        secondary = CustomDarkColors.secondaryBtnColor,
        tertiary = CustomDarkColors.tertiaryBtnColor,
        background = CustomDarkColors.backgroundColor,
        error = CustomDarkColors.dangerColor,
    )

private val LocalColorsProvider =
    staticCompositionLocalOf {
        CustomLightColors
    }

@Composable
private fun CustomLocalProvider(
    colors: CustomColors,
    content: @Composable () -> Unit,
) {
    val colorPalette =
        remember {
            colors.copy()
        }

    colorPalette.update(colors)
    CompositionLocalProvider(LocalColorsProvider provides colorPalette, content = content)
}

private val CustomTheme.colors: Pair<ColorScheme, CustomColors>
    get() =
        when (this) {
            CustomTheme.LIGHT -> lightColorScheme() to CustomLightColors
            CustomTheme.DARK -> DarkColorScheme to CustomDarkColors
        }

object WeSpotThemeManager {
    val colors: CustomColors
        @Composable
        get() = LocalColorsProvider.current

    val fontType
        @Composable
        get() = MaterialTheme.typography

    val shapes
        @Composable
        get() = MaterialTheme.shapes

    var customTheme by mutableStateOf(CustomTheme.DARK)

    fun isSystemInDarkTheme(): Boolean {
        return customTheme == CustomTheme.DARK
    }
}

@Composable
fun WeSpotTheme(
    customTheme: CustomTheme = WeSpotThemeManager.customTheme,
    content: @Composable () -> Unit,
) {
    val (colorPlatte, colors) = customTheme.colors
    CustomLocalProvider(colors = colors) {
        MaterialTheme(
            colorScheme = colorPlatte,
            content = content,
            typography = StaticTypeScale.Default.Copy(),
            shapes = Shapes,
        )
    }
}
