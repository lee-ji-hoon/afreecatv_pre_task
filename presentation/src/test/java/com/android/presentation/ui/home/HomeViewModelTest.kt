package com.android.presentation.ui.home

import app.cash.turbine.Event
import app.cash.turbine.test
import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadCategory
import com.android.domain.usecase.FetchBroadCategoryUseCase
import com.android.presentation.ui.common.UiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val fetchBroadCategoryUseCase: FetchBroadCategoryUseCase = mockk()
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val viewModel: HomeViewModel by lazy { HomeViewModel(fetchBroadCategoryUseCase) }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("[성공] 카테고리를 갖고 오는데 성공하면 UiState는 Loading이였다가 Success가 된다.]")
    fun fetchCategorySuccess() = runTest {
        // given
        coEvery {
            fetchBroadCategoryUseCase()
        } returns ResultWrapper.Success(testCategory)

        // when
        viewModel.uiState.test {
            // then
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiState.Loading),
                Event.Item(UiState.Success(Unit))
            )
        }
    }

    @Test
    @DisplayName("[실패] 카테고리를 갖고 오는데 네트워크 에러가 발생한다면 UiState는 Loading이였다가 Success가 된다.")
    fun fetchCategoryFailed() = runTest {
        // given
        coEvery {
            fetchBroadCategoryUseCase()
        } returns ResultWrapper.Failed(ErrorData.Network)

        // when
        viewModel.uiState.test {
            // then
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiState.Loading),
                Event.Item(UiState.Failure(NETWORK_ERROR_STRING_RES)) // 네트워크 에러 StringRes 값
            )
        }
    }

    companion object {
        private const val NETWORK_ERROR_STRING_RES = 2132017215
        private val testCategory = listOf(BroadCategory("name", "number", emptyList()))
    }
}
