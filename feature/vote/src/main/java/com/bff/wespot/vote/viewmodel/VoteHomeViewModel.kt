package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.common.util.toDateTimeString
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.model.common.KakaoSharingType
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.vote.state.home.VoteAction
import com.bff.wespot.vote.state.home.VoteSideEffect
import com.bff.wespot.vote.state.home.VoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VoteHomeViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val commonRepository: CommonRepository,
) : BaseViewModel(), ContainerHost<VoteUiState, VoteSideEffect> {
    override val container = container<VoteUiState, VoteSideEffect>(
        VoteUiState(
            playStoreLink = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.PLAY_STORE_URL),
        ),
    )

    private val _currentDate = MutableStateFlow(LocalDate.now().toDateTimeString())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    private val dateJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(coroutineDispatcher) {
            var previousDate = LocalDate.now().toDateTimeString()
            while (isActive) {
                val currentDate = LocalDate.now().toDateTimeString()
                if (currentDate != previousDate) {
                    previousDate = currentDate
                    _currentDate.value = currentDate
                }
                delay(120_000L)
            }
        }
    }

    fun onAction(action: VoteAction) {
        when (action) {
            is VoteAction.StartDate -> startUpdatingDate()
            is VoteAction.EndDate -> stopUpdatingDate()
            is VoteAction.GetFirst -> getFirstVoteResults(action.date)
            is VoteAction.OnTabChanged -> onTabChanged(action.index)
            is VoteAction.GetSettingDialogOption -> getSetting()
            is VoteAction.ChangeSettingDialog -> changeSettingDialog(action.showDialog)
            VoteAction.GetKakaoContent -> getKakaoContent()
        }
    }

    private fun startUpdatingDate() {
        if (!dateJob.isActive) {
            dateJob.start()
        }
    }

    private fun stopUpdatingDate() {
        dateJob.cancel()
    }

    private fun getFirstVoteResults(date: String) = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch(coroutineDispatcher) {
            try {
                voteRepository.getFirstVoteResults(date)
                    .onSuccess {
                        reduce { state.copy(voteResults = it.voteResults, isLoading = false) }
                    }
                    .onNetworkFailure {
                        postSideEffect(it.toSideEffect())
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false) }
                        Timber.e(it)
                    }
            } catch (e: Exception) {
                reduce { state.copy(isLoading = false) }
                Timber.e(e)
            }
        }
    }

    private fun onTabChanged(index: Int) = intent {
        reduce { state.copy(selectedTabIndex = index) }
    }

    private fun getSetting() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            dataStoreRepository.getBoolean(DataStoreKey.SETTING_DIALOG).collect {
                if (!it) {
                    reduce { state.copy(showSettingDialog = !it) }
                }
            }
        }
    }

    private fun changeSettingDialog(showDialog: Boolean) = intent {
        reduce { state.copy(showSettingDialog = showDialog) }
        dataStoreRepository.saveBoolean(DataStoreKey.SETTING_DIALOG, true)
    }

    private fun getKakaoContent() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            commonRepository.getKakaoContent(KakaoSharingType.TELL.name)
                .onSuccess {
                    reduce { state.copy(kakaoContent = it) }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
