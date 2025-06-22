package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import com.bff.wespot.message.common.HOME_SCREEN_INDEX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageViewModel : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow<Int>(HOME_SCREEN_INDEX)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    fun updateTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
}
