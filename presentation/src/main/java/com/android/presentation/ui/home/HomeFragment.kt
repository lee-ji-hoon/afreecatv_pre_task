package com.android.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.android.domain.model.BroadCategory
import com.android.presentation.R
import com.android.presentation.databinding.FragmentHomeBinding
import com.android.presentation.ui.common.BaseFragment
import com.android.presentation.ui.common.UiState
import com.android.presentation.ui.main.MainViewModel
import com.android.presentation.util.extenstion.repeatOnStarted
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collectLatest { state ->
                Timber.tag("TAG").d("${javaClass.simpleName} state -> $state")
                when (state) {
                    is UiState.Failure -> showSnackBar(state.message)
                    is UiState.Success -> Unit
                    is UiState.Loading -> Unit
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

    private fun initViewPager(item: List<BroadCategory>) {
        binding.viewpagerBroad.adapter = BroadTabAdapter(this, item)
        TabLayoutMediator(binding.tabBoardCategory, binding.viewpagerBroad) { tab, position ->
            tab.text = item[position].name
        }.attach()
    }
}
