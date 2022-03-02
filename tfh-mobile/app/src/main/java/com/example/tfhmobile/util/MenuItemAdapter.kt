package com.example.tfhmobile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.MenuItemWithCountDTO
import kotlinx.android.synthetic.main.menu_item.view.*

class MenuItemAdapter: ListAdapter<MenuItemWithCountDTO, MenuItemAdapter.MenuItemViewHolder>(
    MenuItemComparator()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false);
        return MenuItemViewHolder(v);
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MenuItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: MenuItemWithCountDTO) {
            itemView.id = item.getItem().getId().toInt()
            itemView.menuItemName.text = item.getItem().getName()
            itemView.menuItemCount.text = "#" + item.getCount()
            itemView.menuItemPrice.text = item.getPrice().toString() + " lei"
        }
    }

    class MenuItemComparator: DiffUtil.ItemCallback<MenuItemWithCountDTO>() {
        override fun areItemsTheSame(oldItem: MenuItemWithCountDTO, newItem: MenuItemWithCountDTO): Boolean {
            return oldItem.getItem().getId() == newItem.getItem().getId()
        }

        override fun areContentsTheSame(oldItem: MenuItemWithCountDTO, newItem: MenuItemWithCountDTO): Boolean {
            return oldItem.getItem().getId() == newItem.getItem().getId()
        }
    }
}