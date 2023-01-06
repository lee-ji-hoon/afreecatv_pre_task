package com.android.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.presentation.databinding.ItemBroadBinding
import com.android.presentation.model.BroadUiModel

class BroadAdapter(
    private val onClick: (BroadUiModel) -> Unit
) : ListAdapter<BroadUiModel, BroadAdapter.ViewHolder>(BroadDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBroadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(
        private val binding: ItemBroadBinding,
        private val onClick: (BroadUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.broad?.let { broad ->
                    onClick(broad)
                }
            }
        }

        fun bind(broadUiModel: BroadUiModel) {
            binding.broad = broadUiModel
            binding.executePendingBindings()
        }
    }
}

class BroadDiffCallback : DiffUtil.ItemCallback<BroadUiModel>() {

    override fun areItemsTheSame(oldItem: BroadUiModel, newItem: BroadUiModel): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: BroadUiModel, newItem: BroadUiModel): Boolean {
        return oldItem == newItem
    }
}
