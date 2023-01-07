package com.android.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.presentation.R
import com.android.presentation.databinding.FragmentBroadDetailBinding
import com.android.presentation.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BroadDetailFragment :
    BaseFragment<FragmentBroadDetailBinding>(R.layout.fragment_broad_detail) {

    private val viewModel: BroadDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }
}
