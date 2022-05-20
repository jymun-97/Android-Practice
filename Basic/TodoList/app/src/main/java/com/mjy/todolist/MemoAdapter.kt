package com.mjy.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjy.todolist.databinding.ItemMemoBinding
import com.mjy.todolist.memo.MemoItem

class MemoAdapter(
    val onCheckBoxClicked: (MemoItem) -> Unit,
    val onDeleteClicked: (MemoItem) -> Unit
) : ListAdapter<MemoItem, MemoAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.ViewHolder {
        return ViewHolder(
            ItemMemoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemoAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(memoItem: MemoItem) {
            binding.apply {
                memoTextView.text = memoItem.memo
                checkBox.isChecked = memoItem.isChecked
                lineView.isVisible = memoItem.isChecked

                deleteButton.setOnClickListener {
                    onDeleteClicked(memoItem)
                }
                checkBox.setOnCheckedChangeListener { _, _ ->
                    memoItem.isChecked = !memoItem.isChecked
                    checkBox.isChecked = memoItem.isChecked
                    lineView.isVisible = memoItem.isChecked

                    onCheckBoxClicked(memoItem)
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MemoItem>() {
            override fun areItemsTheSame(oldItem: MemoItem, newItem: MemoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MemoItem, newItem: MemoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}