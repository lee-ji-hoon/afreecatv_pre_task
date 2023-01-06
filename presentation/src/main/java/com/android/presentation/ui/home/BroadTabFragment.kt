package com.android.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.presentation.R
import com.android.presentation.databinding.FragmentHomeTabBinding
import com.android.presentation.ui.common.BaseFragment
import com.android.presentation.ui.common.UiState
import com.android.presentation.util.Extras.KEY_CATEGORY
import com.android.presentation.util.extenstion.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class BroadTabFragment : BaseFragment<FragmentHomeTabBinding>(R.layout.fragment_home_tab) {

    private val categoryName: String? by lazy { arguments?.getString(KEY_CATEGORY) }
    private val viewModel: BroadViewModel by viewModels()
    private val broadAdapter = BroadAdapter { broad ->
        Timber.tag("TAG").d("${javaClass.simpleName} item click -> $broad")
        // TODO 이동
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()
        initRecyclerViewScrollListener(
            recyclerView = binding.rvBroad,
            fetch = { categoryName?.let { viewModel.fetchBroadList(it) } }
        )
    }

    private fun initAdapter() {
        binding.rvBroad.adapter = broadAdapter
    }

    private fun initViewModel() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        categoryName?.let { viewModel.fetchBroadList(it) }
        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collectLatest { state ->
                Timber.tag("TAG").d("${javaClass.simpleName} state -> $state")
                when (state) {
                    is UiState.Failure -> {
                        showSnackBar(state.message)
                        showProgressbar(false)
                    }
                    is UiState.Loading -> showProgressbar(true)
                    is UiState.Success<*> -> showProgressbar(false)
                    is UiState.EmptyResult -> {
                        showLottie()
                        showProgressbar(false)
                    }
                }
            }
        }
    }

    private fun initRecyclerViewScrollListener(
        recyclerView: RecyclerView,
        fetch: (() -> Unit),
        pagingFetchCount: Int = DEFAULT_PAGING_FETCH_COUNT
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager =
                    LinearLayoutManager::class.java.cast(recyclerView.layoutManager) ?: return
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                if (endScrolled(lastVisible, totalItemCount)) {
                    fetch.invoke()
                }
            }

            private fun endScrolled(lastVisible: Int, totalItemCount: Int) =
                lastVisible >= totalItemCount - pagingFetchCount
        })
    }

    private fun showLottie() {
        binding.groupEmptyResult.isVisible = true
        binding.lottie.playAnimation()
    }

    private fun showProgressbar(visible: Boolean) {
        binding.pbPaging.isVisible = visible
    }

    companion object {
        private const val DEFAULT_PAGING_FETCH_COUNT = 4

        fun newInstance(categoryName: String) =
            BroadTabFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY, categoryName)
                }
            }
    }
}
