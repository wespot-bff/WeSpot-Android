package com.bff.wespot.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.util.textDp

internal val plainTextFont =
    FontFamily(
        Font(R.font.pretendard_r),
        Font(R.font.pretendard_m),
        Font(R.font.pretendard_bold, weight = FontWeight.Bold),
        Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
    )

private const val LINE_HEIGHT_PERCENT = 1.5f

@Stable
class StaticTypeScale private constructor(
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val fontSize: Int,
    val lineHeight: Float,
) {
    companion object {
        val Default =
            TypeScale(
                _header1 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20,
                        lineHeight = 20 * LINE_HEIGHT_PERCENT,
                    ),
                _header2 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18,
                        lineHeight = 18 * LINE_HEIGHT_PERCENT,
                    ),
                _header3 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16,
                        lineHeight = 16 * LINE_HEIGHT_PERCENT,
                    ),
                _body1 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18,
                        lineHeight = 18 * LINE_HEIGHT_PERCENT,
                    ),
                _body2 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18,
                        lineHeight = 18 * LINE_HEIGHT_PERCENT,
                    ),
                _body3 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16,
                        lineHeight = 16 * LINE_HEIGHT_PERCENT,
                    ),
                _body4 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16,
                        lineHeight = 16 * LINE_HEIGHT_PERCENT,
                    ),
                _body5 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14,
                        lineHeight = 14 * LINE_HEIGHT_PERCENT,
                    ),
                _body6 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14,
                        lineHeight = 14 * LINE_HEIGHT_PERCENT,
                    ),
                _body7 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13,
                        lineHeight = 13 * LINE_HEIGHT_PERCENT,
                    ),
                _body8 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13,
                        lineHeight = 13 * LINE_HEIGHT_PERCENT,
                    ),
                _body9 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12,
                        lineHeight = 12 * LINE_HEIGHT_PERCENT,
                    ),
                _body10 =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10,
                        lineHeight = 10 * LINE_HEIGHT_PERCENT,
                    ),
                _body11 = StaticTypeScale(
                    fontFamily = plainTextFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10,
                    lineHeight = 10 * LINE_HEIGHT_PERCENT,
                ),
                _badge =
                    StaticTypeScale(
                        fontFamily = plainTextFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12,
                        lineHeight = 12 * LINE_HEIGHT_PERCENT,
                    ),
            )
    }
}

private val StaticTypeScale.textStyle: TextStyle
    @Composable get() =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize.textDp,
            lineHeight = lineHeight.textDp,
        )

@Immutable
data class TypeScale constructor(
    private val _header1: StaticTypeScale,
    private val _header2: StaticTypeScale,
    private val _header3: StaticTypeScale,
    private val _body1: StaticTypeScale,
    private val _body2: StaticTypeScale,
    private val _body3: StaticTypeScale,
    private val _body4: StaticTypeScale,
    private val _body5: StaticTypeScale,
    private val _body6: StaticTypeScale,
    private val _body7: StaticTypeScale,
    private val _body8: StaticTypeScale,
    private val _body9: StaticTypeScale,
    private val _body10: StaticTypeScale,
    private val _body11: StaticTypeScale,
    private val _badge: StaticTypeScale,
) {
    val header1: TextStyle @Composable get() = _header1.textStyle
    val header2: TextStyle @Composable get() = _header2.textStyle
    val header3: TextStyle @Composable get() = _header3.textStyle

    val body1: TextStyle @Composable get() = _body1.textStyle
    val body2: TextStyle @Composable get() = _body2.textStyle
    val body3: TextStyle @Composable get() = _body3.textStyle
    val body4: TextStyle @Composable get() = _body4.textStyle
    val body5: TextStyle @Composable get() = _body5.textStyle
    val body6: TextStyle @Composable get() = _body6.textStyle
    val body7: TextStyle @Composable get() = _body7.textStyle
    val body8: TextStyle @Composable get() = _body8.textStyle
    val body9: TextStyle @Composable get() = _body9.textStyle
    val body10: TextStyle @Composable get() = _body10.textStyle
    val body11: TextStyle @Composable get() = _body11.textStyle

    val badge: TextStyle @Composable get() = _badge.textStyle

    @Composable
    fun Copy() =
        Typography(
            displayLarge = header1,
            displayMedium = header2,
            displaySmall = header3,
            headlineLarge = header1,
            headlineMedium = header2,
            headlineSmall = header3,
            titleLarge = header1,
            titleMedium = header2,
            titleSmall = header3,
            bodyLarge = body1,
            bodyMedium = body2,
            bodySmall = body3,
            labelLarge = body4,
            labelMedium = body4,
            labelSmall = body5,
        )
}
