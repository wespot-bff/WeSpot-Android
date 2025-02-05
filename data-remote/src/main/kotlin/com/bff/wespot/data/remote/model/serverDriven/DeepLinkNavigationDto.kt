package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.ClickAction
import com.bff.wespot.model.serverDriven.DeepLinkNavigation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("deepLinkNavigation")
class DeepLinkNavigationDto (
    private val deepLink: String,
) : ClickActionDto {
    override fun toDomain(): ClickAction {
        return DeepLinkNavigation(
            deepLink = deepLink,
        )
    }
}
