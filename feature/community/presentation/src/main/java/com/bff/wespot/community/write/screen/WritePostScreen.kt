package com.bff.wespot.community.write.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.write.state.WritePostAction
import com.bff.wespot.community.write.state.WritePostUiState
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.community.chip.CategoryChips
import com.bff.wespot.model.community.chip.CategoryItem
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.util.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WritePostScreen(
    uiState: WritePostUiState,
    onAction: (WritePostAction) -> Unit,
) {
    val pickImage =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(
                maxItems = 3,
            ),
        ) {
            onAction(WritePostAction.OnImageChanged(it.map { it.toString() }))
        }

    var showCategoryBottomSheet by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(R.string.write_post),
                action = {
                    Icon(
                        painter = painterResource(com.bff.wespot.designsystem.R.drawable.icn_close),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 14.dp),
                    )
                },
            )
        },
        bottomBar = {
            WSButton(
                onClick = {},
                text = stringResource(R.string.write_upload_post),
                enabled = uiState.description.isNotEmpty(),
            ) {
                it.invoke()
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(top = 12.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
                .fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier.clickableSingle {
                        showCategoryBottomSheet = true
                    },
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                WeSpotThemeManager.colors.cardBackgroundColor,
                                RoundedCornerShape(12.dp),
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .heightIn(min = 56.dp)
                            .fillMaxWidth(0.5f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = uiState.selectedCategory.text.ifEmpty {
                                stringResource(R.string.write_select_category)
                            },
                            color = Gray200,
                            style = StaticTypeScale.Default.body3,
                            modifier = Modifier.padding(16.dp),
                            maxLines = 1,
                        )
                    }

                    Icon(
                        painter = rememberAsyncImagePainter(
                            com.bff.wespot.designsystem.R.drawable.right_arrow,
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(24.dp)
                            .padding(end = 16.dp),
                    )
                }

                WsTextField(
                    value = uiState.title,
                    onValueChange = {
                        onAction(WritePostAction.OnTitleChanged(it))
                    },
                    placeholder = stringResource(R.string.write_title_placeholder),
                )
                WsTextField(
                    value = uiState.description,
                    onValueChange = {
                        onAction(WritePostAction.OnDescriptionChanged(it))
                    },
                    placeholder = stringResource(R.string.write_description_placeholder),
                    textFieldType = WsTextFieldType.Message,
                )
            }
            Text(
                text = "${uiState.description.length} / 1200",
                style = StaticTypeScale.Default.body7,
                color = WeSpotThemeManager.colors.disableIcnColor,
                modifier = Modifier.align(Alignment.End),
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.write_upload_image),
                style = StaticTypeScale.Default.body2,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.write_upload_limit),
                style = StaticTypeScale.Default.body9,
                color = WeSpotThemeManager.colors.disableIcnColor,
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 12.dp, end = 14.dp),
            ) {
                item {
                    ImageBox(
                        imagePath = "",
                        onBoxClick = {
                            if (uiState.images.size == 3) return@ImageBox
                            pickImage.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.SingleMimeType(
                                        "image/*",
                                    ),
                                ),
                            )
                        },
                        onCloseClick = {},
                    )
                }

                items(uiState.images) {
                    ImageBox(
                        imagePath = it,
                        onBoxClick = {},
                        onCloseClick = {
                            onAction(WritePostAction.OnImageDelete(it))
                        },
                    )
                }
            }
        }
    }

    if (showCategoryBottomSheet) {
        CategoryBottomSheet(
            chips = uiState.categories,
            selectedChip = uiState.selectedCategory,
            onChipClicked = {
                onAction(WritePostAction.OnCategoryChanged(it))
            },
            closeSheet = {
                showCategoryBottomSheet = false
            },
        )
    }
}

@Composable
private fun ImageBox(
    imagePath: String,
    onBoxClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    Box {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(WeSpotThemeManager.colors.modalColor, RoundedCornerShape(16.dp))
                .clickableSingle(
                    enabled = imagePath.isEmpty(),
                    onClick = onBoxClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (imagePath.isEmpty()) {
                Icon(
                    painter = rememberAsyncImagePainter(R.drawable.add),
                    modifier = Modifier.size(20.dp),
                    contentDescription = null,
                )
            } else {
                AsyncImage(
                    model = imagePath,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        }

        if (imagePath.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .background(WeSpotThemeManager.colors.secondaryBtnColor, CircleShape)
                    .clip(CircleShape)
                    .size(28.dp)
                    .clickableSingle {
                        onCloseClick.invoke()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = rememberAsyncImagePainter(com.bff.wespot.designsystem.R.drawable.icn_close),
                    contentDescription = null,
                    tint = WeSpotThemeManager.colors.abledIconColor,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryBottomSheet(
    chips: List<CategoryChips>,
    selectedChip: CategoryItem,
    onChipClicked: (CategoryItem) -> Unit,
    closeSheet: () -> Unit,
) {
    WSBottomSheet(
        closeSheet = closeSheet,
    ) {
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 28.dp,
            ),
        ) {
            item {
                Text(
                    text = stringResource(R.string.write_category_detail_title),
                    style = StaticTypeScale.Default.body1,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            items(
                items = chips,
                key = {
                    it.category
                },
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = it.category,
                        style = StaticTypeScale.Default.body2,
                        color = WeSpotThemeManager.colors.abledIconColor,
                    )

                    LazyRow {
                        items(
                            items = it.chips,
                            key = { it.id },
                        ) { chip ->
                            val selected = selectedChip == chip

                            Box(
                                modifier = Modifier
                                    .clip(WeSpotThemeManager.shapes.extraLarge)
                                    .clickableSingle { onChipClicked(chip) }
                                    .background(
                                        if (selected) {
                                            WeSpotThemeManager.colors.abledTxtColor
                                        } else {
                                            WeSpotThemeManager.colors.disableBtnColor
                                        },
                                    ),
                            ) {
                                Text(
                                    text = chip.text,
                                    style = StaticTypeScale.Default.body9,
                                    color = if (selected) {
                                        Gray100
                                    } else {
                                        Gray600
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private object WritePostPreviewData {
    val emptyState = WritePostUiState(
        selectedCategory = CategoryItem.EMPTY,
        title = "",
        description = "",
        images = emptyList(),
        categories = listOf(
            CategoryChips(
                category = "질문",
                chips = listOf(
                    CategoryItem(id = "1", text = "개발"),
                    CategoryItem(id = "2", text = "디자인"),
                    CategoryItem(id = "3", text = "기획"),
                ),
            ),
            CategoryChips(
                category = "자유",
                chips = listOf(
                    CategoryItem(id = "4", text = "일상"),
                    CategoryItem(id = "5", text = "취미"),
                ),
            ),
        ),
    )

    val filledState = WritePostUiState(
        selectedCategory = CategoryItem(id = "1", text = "개발"),
        title = "React 18 새로운 기능들",
        description = "React 18에서 추가된 Concurrent Features에 대해 알아봅시다. Suspense, " +
            "useTransition, useDeferredValue 등 새로운 기능들을 실제 예제와 함께 설명해드리겠습니다.",
        images = listOf(
            "https://via.placeholder.com/300x200",
            "https://via.placeholder.com/300x200",
        ),
        categories = listOf(
            CategoryChips(
                category = "질문",
                chips = listOf(
                    CategoryItem(id = "1", text = "개발"),
                    CategoryItem(id = "2", text = "디자인"),
                    CategoryItem(id = "3", text = "기획"),
                ),
            ),
        ),
    )

    val maxImagesState = WritePostUiState(
        selectedCategory = CategoryItem(id = "2", text = "디자인"),
        title = "UI/UX 디자인 가이드",
        description = "효과적인 UI/UX 디자인을 위한 기본 원칙들",
        images = listOf(
            "https://via.placeholder.com/300x200",
            "https://via.placeholder.com/300x200",
            "https://via.placeholder.com/300x200",
        ),
        categories = emptyList(),
    )
}

@Preview(showBackground = true)
@Composable
private fun WritePostScreenEmptyPreview() {
    WeSpotTheme {
        WritePostScreen(
            uiState = WritePostPreviewData.emptyState,
            onAction = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WritePostScreenFilledPreview() {
    WeSpotTheme {
        WritePostScreen(
            uiState = WritePostPreviewData.filledState,
            onAction = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WritePostScreenMaxImagesPreview() {
    WeSpotTheme {
        WritePostScreen(
            uiState = WritePostPreviewData.maxImagesState,
            onAction = { },
        )
    }
}
