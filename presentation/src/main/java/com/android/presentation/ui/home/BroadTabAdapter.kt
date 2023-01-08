package com.android.presentation.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.presentation.model.BroadCategoryUiModel

class BroadTabAdapter(
    fragment: Fragment,
    private val item: List<BroadCategoryUiModel>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = item.count()

    override fun createFragment(position: Int): Fragment {
        return BroadTabFragment.newInstance(item[position].number)
    }
}
