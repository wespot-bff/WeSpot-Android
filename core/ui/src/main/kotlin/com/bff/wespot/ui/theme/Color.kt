package com.bff.wespot.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

enum class CustomTheme {
    LIGHT, DARK
}


val Primary100 = Color(0xFFFDFFDC)
val Primary200 = Color(0xFFFBFFB7)
val Primary300 = Color(0xFFF6FE8B)
val Primary400 = Color(0xFFE4E7C1)
val Primary500 = Color(0xFF313229)

val White = Color(0xFFFFFFFF)
val Gray100 = Color(0xFFEAEBEC)
val Gray200 = Color(0xFFEAEBEC)
val Gray300 = Color(0xFFC8C8C8)
val Gray400 = Color(0xFF76777D)
val Gray500 = Color(0xFF5A5C63)
val Gray600 = Color(0xFF323439)
val Gray700 = Color(0xFF2E2F33)
val Gray800 = Color(0xFF212225)
val Gray900 = Color(0xFF1B1C1E)

val Destructive = Color(0xFFFF4D4D)
val Positive = Color(0xFF1ED45A)

@Stable
class CustomColors(
    primaryColor: Color,
    toastColor: Color,
    txtTitleColor: Color,
    abledTxtColor: Color,
    abledIconColor: Color,
    txtSubColor: Color,
    disableIcnColor: Color,
    disableBtnTxtColor: Color,
    secondaryBtnColor: Color,
    primaryBtnColor: Color,
    disableBtnColor: Color,
    badgeColor: Color,
    modalColor: Color,
    cardBackgroundColor: Color,
    tertiaryBtnColor: Color,
    naviColor: Color,
    backgroundColor: Color,
    dangerColor: Color,
    alertBadgeColor: Color,
    toggleColor: Color
) {
    var primaryColor by mutableStateOf(primaryColor)
        private set

    var toastColor by mutableStateOf(toastColor)
        private set

    var txtTitleColor by mutableStateOf(txtTitleColor)
        private set

    var abledIconColor by mutableStateOf(abledIconColor)
        private set

    var abledTxtColor by mutableStateOf(abledTxtColor)
        private set

    var txtSubColor by mutableStateOf(txtSubColor)
        private set

    var disableIcnColor by mutableStateOf(disableIcnColor)
        private set

    var disableBtnTxtColor by mutableStateOf(disableBtnTxtColor)
        private set

    var secondaryBtnColor by mutableStateOf(secondaryBtnColor)
        private set

    var primaryBtnColor by mutableStateOf(primaryBtnColor)
        private set

    var disableBtnColor by mutableStateOf(disableBtnColor)
        private set

    var badgeColor by mutableStateOf(badgeColor)
        private set

    var modalColor by mutableStateOf(modalColor)
        private set

    var cardBackgroundColor by mutableStateOf(cardBackgroundColor)
        private set

    var tertiaryBtnColor by mutableStateOf(tertiaryBtnColor)
        private set

    var naviColor by mutableStateOf(naviColor)
        private set

    var backgroundColor by mutableStateOf(backgroundColor)
        private set

    var dangerColor by mutableStateOf(dangerColor)
        private set

    var alertBadgeColor by mutableStateOf(alertBadgeColor)
        private set

    var toggleColor by mutableStateOf(toggleColor)
        private set

    fun update(colors: CustomColors) {
        this.primaryColor = colors.primaryColor
        this.toastColor = colors.toastColor
        this.txtTitleColor = colors.txtTitleColor
        this.abledTxtColor = colors.abledTxtColor
        this.abledIconColor = colors.abledIconColor
        this.txtSubColor = colors.txtSubColor
        this.disableIcnColor = colors.disableIcnColor
        this.disableBtnTxtColor = colors.disableBtnTxtColor
        this.secondaryBtnColor = colors.secondaryBtnColor
        this.primaryBtnColor = colors.primaryBtnColor
        this.disableBtnColor = colors.disableBtnColor
        this.badgeColor = colors.badgeColor
        this.modalColor = colors.modalColor
        this.cardBackgroundColor = colors.cardBackgroundColor
        this.tertiaryBtnColor = colors.tertiaryBtnColor
        this.naviColor = colors.naviColor
        this.backgroundColor = colors.backgroundColor
        this.dangerColor = colors.dangerColor
        this.alertBadgeColor = colors.alertBadgeColor
        this.toggleColor = colors.toggleColor
    }

    fun copy() = CustomColors(
        primaryColor = primaryColor,
        toastColor = toastColor,
        txtTitleColor = txtTitleColor,
        abledTxtColor = abledTxtColor,
        abledIconColor = abledIconColor,
        txtSubColor = txtSubColor,
        disableIcnColor = disableIcnColor,
        disableBtnTxtColor = disableBtnTxtColor,
        secondaryBtnColor = secondaryBtnColor,
        primaryBtnColor = primaryBtnColor,
        disableBtnColor = disableBtnColor,
        badgeColor = badgeColor,
        modalColor = modalColor,
        cardBackgroundColor = cardBackgroundColor,
        tertiaryBtnColor = tertiaryBtnColor,
        naviColor = naviColor,
        backgroundColor = backgroundColor,
        dangerColor = dangerColor,
        alertBadgeColor = alertBadgeColor,
        toggleColor = toggleColor
    )
}
