package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.model.user.response.ProfileCharacter
import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.ReceivedResult
import com.bff.wespot.model.vote.response.ReceivedUser
import com.bff.wespot.model.vote.response.VoteOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class IndividualViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val individual = flow {
        val isReceived = savedStateHandle["isReceived"] ?: false
        val date = savedStateHandle["date"] ?: LocalDate.now().toDateString()
        val optionId = savedStateHandle["optionId"] ?: 0
        if (isReceived) {
            voteRepository.getReceivedVote(date, optionId)
                .onSuccess {
                    emit(it)
                }
                .onFailure {
                    emit(
                        IndividualReceived(
                            voteResult = ReceivedResult(
                                voteCount = 10,
                                rate = 1,
                                voteOption = VoteOption(
                                    id = 1,
                                    content = "이 사람은 반에서 어떤 사람인가요?"
                                ),
                                user = ReceivedUser(
                                    id = 1,
                                    name = "김철수",
                                    introduction = "안녕하세요",
                                    profile = ProfileCharacter(
                                        iconUrl = "r",
                                        backgroundColor = "#FF0000"
                                    )
                                )
                            )
                        )
                    )
                }
        } else {
            voteRepository.getSentVote(date, optionId)
                .onSuccess {
                    emit(it)
                }
        }
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), null)

}