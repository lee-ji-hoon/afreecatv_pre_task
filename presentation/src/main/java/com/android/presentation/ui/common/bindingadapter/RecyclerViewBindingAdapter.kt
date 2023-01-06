package com.android.presentation.ui.common.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("submitList")
fun <T> submitList(recyclerView: RecyclerView, list: List<T>?) {
    list ?: return
    val listAdapter = recyclerView.adapter ?: return

    if (listAdapter is ListAdapter<*, *>) {
        (listAdapter as ListAdapter<T, *>).submitList(list)
    }
}
