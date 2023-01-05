package com.android.domain.usecase

import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadCategory
import com.android.domain.repository.BroadRepository
import javax.inject.Inject

class FetchBroadCategoryUseCase @Inject constructor(
    private val broadRepository: BroadRepository
) {

    suspend operator fun invoke(): ResultWrapper<List<BroadCategory>> {
        return broadRepository.fetchBroadCategoryList()
    }
}
