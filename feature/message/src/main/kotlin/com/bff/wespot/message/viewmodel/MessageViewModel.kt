package com.bff.wespot.message.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bff.wespot.message.common.HOME_SCREEN_INDEX
import com.bff.wespot.message.common.STORAGE_SCREEN_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(HOME_SCREEN_INDEX)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    init {
        initTabIndex()
    }

    private fun initTabIndex() {
        val tab: String = savedStateHandle["tab"] ?: return
        if (tab == "STORAGE") {
            _selectedTabIndex.value = STORAGE_SCREEN_INDEX
        }
    }

    fun updateTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
}
