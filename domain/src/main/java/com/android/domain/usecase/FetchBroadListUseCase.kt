package com.android.domain.usecase

import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadList
import com.android.domain.repository.BroadRepository
import javax.inject.Inject

class FetchBroadListUseCase @Inject constructor(
    private val broadRepository: BroadRepository
) {

    suspend operator fun invoke(): ResultWrapper<BroadList> {
        return broadRepository.fetchBroadList()
    }
}
