package com.bff.wespot.data.mapper.vote

import com.bff.wespot.data.remote.model.vote.request.VoteResultDto
import com.bff.wespot.data.remote.model.vote.request.VoteResultsDto
import com.bff.wespot.model.vote.request.VoteResult
import com.bff.wespot.model.vote.request.VoteResults

fun VoteResults.toDto() =
    VoteResultsDto(
        voteResultDtos = voteResults.map { it.toDto() }
    )

fun VoteResult.toDto() =
    VoteResultDto(
        userId = userId,
        voteOptionId = voteOptionId
    )