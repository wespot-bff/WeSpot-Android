package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.util.DataStoreKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetMessageHomeTitleUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val messageRepository: MessageRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): String {
        checkAndCacheMessageHomeTitle()

        val title = dataStoreRepository.getString(DataStoreKey.MESSAGE_HOME_TITLE).firstOrNull()
        return if (title.isNullOrEmpty()) {
            getDefaultTitle()
        } else {
            title
        }
    }

    private fun checkAndCacheMessageHomeTitle() {
        CoroutineScope(dispatcher).launch {
            val cachedTimeString = dataStoreRepository.getString(
                DataStoreKey.MESSAGE_HOME_TITLE_CACHED_TIME,
            ).firstOrNull()
            val cachedTime = cachedTimeString?.toLongOrNull() ?: 0L
            val currentTime = System.currentTimeMillis()

            /** 아직 캐싱 유효 시간이 지나지 않았다면 바로 반환한다. */
            if ((currentTime - cachedTime) <= CACHE_EXPIRATION_TIME_MILLIS) return@launch

            messageRepository.getMessageHomeTitle()
                .onSuccess {
                    dataStoreRepository.saveString(DataStoreKey.MESSAGE_HOME_TITLE, it.title)
                    dataStoreRepository.saveString(
                        DataStoreKey.MESSAGE_HOME_TITLE_CACHED_TIME,
                        currentTime.toString(),
                    )
                }
        }
    }

    private suspend fun getDefaultTitle(): String {
        val name = profileRepository.getProfile().name
        return name + "을 설레게 한 친구에게\n쪽지로 마음을 표현해보세요"
    }

    companion object {
        private const val CACHE_EXPIRATION_TIME_MILLIS = 2 * 3600 * 1000L // 2시간
    }
}
