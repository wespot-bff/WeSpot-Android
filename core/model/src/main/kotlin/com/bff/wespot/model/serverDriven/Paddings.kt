package com.bff.wespot.model.serverDriven

data class Paddings(
    val top: Int,
    val start: Int,
    val end: Int,
    val bottom: Int,
) {
    companion object {
        val None = Paddings(0, 0, 0, 0)
    }
}
