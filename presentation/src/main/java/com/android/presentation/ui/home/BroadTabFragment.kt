package com.android.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.StringRes
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
    private val broadAdapter = BroadAdapter({
        performOptionsMenuClick(it)
    }) { broad ->
        val action = HomeFragmentDirections.actionHomeToBroadDetail(broad)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()
        initRecyclerViewScrollListener(
            recyclerView = binding.rvBroad,
            fetch = { categoryName?.let { viewModel.fetchBroadList(it) } }
        )
        initRefreshSwipe()
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
                        showSnackBar(getString(state.message))
                        showProgressbar(false)
                    }
                    is UiState.NetworkFailure -> showLottie(R.string.error_network, true)
                    is UiState.Loading -> showProgressbar(true)
                    is UiState.Success<*> -> {
                        showLottie(visible = false)
                        showProgressbar(false)
                    }
                    is UiState.EmptyResult -> {
                        showLottie(R.string.category_empty_result, true)
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

    private fun initRefreshSwipe() {
        val refreshLayout = binding.layoutRefresh
        refreshLayout.setOnRefreshListener {
            viewModel.refresh(categoryName)
            refreshLayout.isRefreshing = false
        }
    }

    private fun showLottie(@StringRes message: Int? = null, visible: Boolean) {
        binding.tvErrorMessage.text = message?.let { getString(it) }
        binding.groupEmptyResult.isVisible = visible
        if (visible) {
            binding.lottie.playAnimation()
        } else {
            binding.lottie.cancelAnimation()
        }
    }

    private fun showProgressbar(visible: Boolean) {
        binding.pbPaging.isVisible = visible
    }

    private fun performOptionsMenuClick(position: Int) {
        val broad = broadAdapter.currentList[position]
        PopupMenu(
            requireContext(),
            binding.rvBroad[position].findViewById(R.id.tv_more)
        ).apply {
            inflate(R.menu.broad_menu_list)

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_stations -> showSnackBar(
                        getString(
                            R.string.menu_move_stations,
                            broad.userNickname
                        )
                    )
                    R.id.action_bookmark -> showSnackBar(
                        getString(
                            R.string.menu_bookmark,
                            broad.userNickname
                        )
                    )
                    R.id.action_later -> showSnackBar(getString(R.string.menu_later))
                    R.id.action_share -> showSnackBar(getString(R.string.menu_share))
                    R.id.action_report -> showSnackBar(
                        getString(
                            R.string.menu_report,
                            broad.userNickname
                        )
                    )
                }
                false
            }
            show()
        }
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
