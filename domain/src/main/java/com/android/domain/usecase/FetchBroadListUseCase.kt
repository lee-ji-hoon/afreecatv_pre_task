package com.android.domain.usecase

import com.android.domain.common.ResultWrapper
import com.android.domain.model.Broad
import com.android.domain.repository.BroadRepository
import javax.inject.Inject

class FetchBroadListUseCase @Inject constructor(
    private val broadRepository: BroadRepository
) {

    suspend operator fun invoke(categoryName: String, pageNumber: Int): ResultWrapper<List<Broad>> {
        return broadRepository.fetchBroadList(categoryName, pageNumber)
    }
}
