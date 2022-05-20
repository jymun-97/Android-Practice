package com.mjy.contactapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.mjy.contactapp.databinding.ItemContactBinding

class ContactAdapter(
    private val context: Context,
    private var contactItemList: ArrayList<ContactItem>
): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        return ViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        holder.bind(contactItemList[position])
    }

    override fun getItemCount(): Int = contactItemList.size

    inner class ViewHolder(
        private val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contactItem: ContactItem) {
            binding.contactItem = contactItem
        }
    }

    fun setData(itemList: ArrayList<ContactItem>) {
        contactItemList = itemList
        notifyDataSetChanged()
    }
}