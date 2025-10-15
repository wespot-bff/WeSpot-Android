package com.bff.wespot.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bff.wespot.community.uimodel.VoteItemUiModel
import com.bff.wespot.community.uimodel.VoteItemUiModel.VoteContentUiModel
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.GradationType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType
import com.bff.wespot.server.driven.type.Icon
import com.bff.wespot.server.driven.type.Text
import com.bff.wespot.server.driven.type.color
import com.bff.wespot.server.driven.type.toBrush

@Composable
internal fun VoteContentUiModel.Item() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(brush = gradation.toBrush()),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            badge.Item()

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                text.Text(
                    textStyle = StaticTypeScale.Default.body4,
                    modifier = Modifier.weight(1f),
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = actionIcon.backgroundColor.color(), shape = CircleShape)
                        .size(36.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    actionIcon.icon.Icon(
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun VoteContentUiModel.BadgeUiModel.Item() {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = backgroundColor.color(), shape = CircleShape),
    ) {
        text.Text(
            textStyle = StaticTypeScale.Default.badge,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

private object VoteItemPreviewData {
    val sampleVoteItem = VoteItemUiModel(
        id = "vote_1",
        content = VoteContentUiModel(
            badge = VoteContentUiModel.BadgeUiModel(
                backgroundColor = ColorType.Token("primary300"),
                text = RichTextType(
                    text = "투표",
                    color = ColorType.Token("white"),
                    typography = "badge",
                ),
            ),
            text = RichTextType(
                text = "오늘 점심 뭐 먹을까요?",
                color = ColorType.Token("white"),
                typography = "body4",
            ),
            actionIcon = VoteContentUiModel.ActionIconUiModel(
                backgroundColor = ColorType.Token("white"),
                icon = IconType(
                    url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                    color = ColorType.Token("primary500"),
                ),
            ),
            gradation = GradationType(
                startColor = ColorType.Token("primary100"),
                endColor = ColorType.Token("primary400"),
                angle = 45,
            ),
        ),
    )

    val sampleVoteItem2 = VoteItemUiModel(
        id = "vote_2",
        content = VoteContentUiModel(
            badge = VoteContentUiModel.BadgeUiModel(
                backgroundColor = ColorType.Token("gray300"),
                text = RichTextType(
                    text = "설문",
                    color = ColorType.Token("white"),
                    typography = "badge",
                ),
            ),
            text = RichTextType(
                text = "위스팟 앱에서 가장 자주 사용하는 기능은?",
                color = ColorType.Token("white"),
                typography = "body4",
            ),
            actionIcon = VoteContentUiModel.ActionIconUiModel(
                backgroundColor = ColorType.Token("white"),
                icon = IconType(
                    url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                    color = ColorType.Token("gray600"),
                ),
            ),
            gradation = GradationType(
                startColor = ColorType.Token("gray400"),
                endColor = ColorType.Token("gray700"),
                angle = 135,
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun VoteItemPreview() {
    WeSpotTheme {
        Surface {
            VoteItemPreviewData.sampleVoteItem.content.Item()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteItemSecondPreview() {
    WeSpotTheme {
        Surface {
            VoteItemPreviewData.sampleVoteItem2.content.Item()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteItemWithPaddingPreview() {
    WeSpotTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                VoteItemPreviewData.sampleVoteItem.content.Item()
                VoteItemPreviewData.sampleVoteItem2.content.Item()
            }
        }
    }
}
