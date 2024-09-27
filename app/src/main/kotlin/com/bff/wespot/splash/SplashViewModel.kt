package com.bff.wespot.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataSource: RemoteConfigRepository
) : ViewModel() {
    private val _start = MutableStateFlow(false)
    val start = _start.asStateFlow()

    private val _minVersion = MutableStateFlow("")
    val minVersion = _minVersion.asStateFlow()

    init {
        viewModelScope.launch {
            _start.value = dataSource.startRemoteConfig()
            if (_start.value) {
                _minVersion.emit(dataSource.fetchFromRemoteConfig(RemoteConfigKey.MIN_VERSION))
            }
        }
    }
}