package com.android.presentation.ui.home

import android.os.Bundle
import android.view.View
import com.android.presentation.R
import com.android.presentation.databinding.FragmentHomeTabBinding
import com.android.presentation.ui.common.BaseFragment

class BroadTabFragment : BaseFragment<FragmentHomeTabBinding>(R.layout.fragment_home_tab) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewPager() {
    }
}
