package com.mjy.recyclerview_practice

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mjy.recyclerview_practice.databinding.ItemMemberBinding

class MemberAdapter(
    private val context: Context,
    private val memberItems: ArrayList<MemberItem>,
    private val itemClicked: (MemberItem) -> Unit
) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {
        return ViewHolder(
            ItemMemberBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int)
        = holder.bind(memberItems[position])

    override fun getItemCount(): Int = memberItems.size

    inner class ViewHolder(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(memberItem: MemberItem) {
            binding.apply {
                nameTextView.text = memberItem.name
                phoneTextView.text = memberItem.phone

                root.setOnClickListener {
                    itemClicked(memberItem)
                }
            }
        }
    }
}