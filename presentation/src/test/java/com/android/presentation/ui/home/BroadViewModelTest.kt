package com.android.presentation.ui.home

import app.cash.turbine.Event
import app.cash.turbine.test
import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import com.android.domain.model.Broad
import com.android.domain.usecase.FetchBroadListUseCase
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
class BroadViewModelTest {
    private val fetchBroadListUseCase: FetchBroadListUseCase = mockk()
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val viewModel: BroadViewModel by lazy { BroadViewModel(fetchBroadListUseCase) }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("[성공] 카테고리를 받아서 방송 리스트를 갖고 오는데 성공하면 UiState는 Loading이였다가 Success되고 pageNumber는 증가한다.")
    fun fetchBroadSuccess() = runTest {
        // given
        coEvery {
            fetchBroadListUseCase(TEST_CATEGORY, 1)
        } returns ResultWrapper.Success(testBroad)

        // when
        val prevPageNum = viewModel.pageNumber
        viewModel.fetchBroadList(TEST_CATEGORY)

        // then
        viewModel.uiState.test {
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiState.Loading),
                Event.Item(UiState.Success(Unit))
            )
            assertThat(prevPageNum + 1).isEqualTo(viewModel.pageNumber)
        }
    }

    @Test
    @DisplayName("[빈결과] 방송 리스트를 갖고 오는데 성공 했지만 0개라면 UiState는 EmptyResult가 되고 pageNumber는 증가하지 않는다.")
    fun fetchBroadSuccessButEmptyResult() = runTest {
        // given
        coEvery {
            fetchBroadListUseCase(TEST_CATEGORY, 1)
        } returns ResultWrapper.Success(emptyList())

        // when
        val prevPageNum = viewModel.pageNumber
        viewModel.fetchBroadList(TEST_CATEGORY)

        // then
        viewModel.uiState.test {
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiState.Loading),
                Event.Item(UiState.EmptyResult)
            )
            assertThat(prevPageNum).isEqualTo(viewModel.pageNumber)
        }
    }

    @Test
    @DisplayName("[실패] 방송 리스트를 갖고 오는데 네트워크 오류가 난다면 UiState는 Failed가 되고 pageNumber는 증가하지 않는다.")
    fun fetchBroadFailedNetWorkError() = runTest {
        // given
        coEvery {
            fetchBroadListUseCase(TEST_CATEGORY, 1)
        } returns ResultWrapper.Failed(ErrorData.Network)

        // when
        val prevPageNum = viewModel.pageNumber
        viewModel.fetchBroadList(TEST_CATEGORY)

        // then
        viewModel.uiState.test {
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiState.Loading),
                Event.Item(UiState.Failure(NETWORK_ERROR_STRING_RES)) // 네트워크 에러 StringRes 값
            )
            assertThat(prevPageNum).isEqualTo(viewModel.pageNumber)
        }
    }

    @Test
    @DisplayName("[성공] 새로고침을 하면 현재 갖고 있던 방송 리스트와 pageNumber를 초기화하고 새로 갖고 온다.")
    fun refreshSuccess() = runTest {
        // given
        coEvery {
            fetchBroadListUseCase(TEST_CATEGORY, 1)
        } returns ResultWrapper.Success(testBroad)

        // when
        val prevPageNum = viewModel.pageNumber
        viewModel.fetchBroadList(TEST_CATEGORY)

        // then
        viewModel.uiState.test {
            viewModel.refresh(TEST_CATEGORY)
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiState.Loading),
                Event.Item(UiState.Success(Unit)),
                Event.Item(UiState.Loading),
                Event.Item(UiState.Success(Unit))
            )
            assertThat(prevPageNum + 1).isEqualTo(viewModel.pageNumber)
        }
    }

    @Test
    @DisplayName("[성공] refresh 요청이 연속으로 들어와도 data는 중복되지 않는다.")
    fun refreshOverlapSuccess() = runTest {
        // given
        coEvery {
            fetchBroadListUseCase(TEST_CATEGORY, 1)
        } returns ResultWrapper.Success(testBroad)

        // when
        viewModel.fetchBroadList(TEST_CATEGORY)
        viewModel.refresh(TEST_CATEGORY)
        viewModel.refresh(TEST_CATEGORY)
        viewModel.refresh(TEST_CATEGORY)

        // then
        viewModel.broadList.test {
            val data = awaitItem()
            assertThat(data?.size).isEqualTo(testBroad.size)
        }
    }

    companion object {
        private const val TEST_CATEGORY = "100"
        private const val NETWORK_ERROR_STRING_RES = 2132017215
        private val testBroad = listOf(
            Broad(
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"
            )
        )
    }
}
