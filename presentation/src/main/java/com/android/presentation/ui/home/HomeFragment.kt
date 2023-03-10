package com.android.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.presentation.R
import com.android.presentation.databinding.FragmentHomeBinding
import com.android.presentation.model.BroadCategoryUiModel
import com.android.presentation.ui.common.BaseFragment
import com.android.presentation.ui.common.UiState
import com.android.presentation.util.extenstion.repeatOnStarted
import com.android.presentation.util.extenstion.screenWidth
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Failure -> showSnackBar(getString(state.message))
                    is UiState.Success<*> -> Unit
                    is UiState.Loading -> Unit
                    is UiState.EmptyResult -> Unit
                    is UiState.NetworkFailure -> showSnackBar(getString(R.string.error_network))
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.category.collectLatest { data ->
                data ?: return@collectLatest
                initViewPager(data)
            }
        }
    }

    private fun initViewPager(item: List<BroadCategoryUiModel>) {
        binding.viewpagerBroad.adapter = BroadTabAdapter(this, item)
        TabLayoutMediator(binding.tabBoardCategory, binding.viewpagerBroad) { tab, position ->
            tab.view.minimumWidth = (screenWidth * 0.22).roundToInt()
            tab.text = item[position].name
        }.attach()
    }
}
