package com.android.presentation.ui.home

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.presentation.databinding.ItemBroadBinding
import com.android.presentation.model.BroadUiModel

class BroadAdapter(
    private var optionsMenuClickListener: OptionsMenuClickListener,
    private val onClick: (BroadUiModel) -> Unit
) : ListAdapter<BroadUiModel, BroadAdapter.ViewHolder>(BroadDiffCallback()) {

    fun interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBroadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick,
            optionsMenuClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    class ViewHolder(
        private val binding: ItemBroadBinding,
        private val onClick: (BroadUiModel) -> Unit,
        private val optionsMenuClickListener: OptionsMenuClickListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {

        init {
            binding.root.setOnClickListener {
                binding.broad?.let { broad ->
                    onClick(broad)
                }
            }
        }

        fun bind(broadUiModel: BroadUiModel, position: Int) {
            binding.broad = broadUiModel
            binding.executePendingBindings()
            binding.tvMore.setOnClickListener {
                optionsMenuClickListener.onOptionsMenuClicked(position)
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
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
