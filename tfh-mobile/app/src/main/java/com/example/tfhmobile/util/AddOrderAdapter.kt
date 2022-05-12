package com.example.tfhmobile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.MenuItemDTO
import com.example.tfhmobile.dto.MenuItemWithQuantityRequest
import kotlinx.android.synthetic.main.add_menu_item.view.*
import kotlinx.android.synthetic.main.order_request.view.*

class AddOrderAdapter : ListAdapter<MenuItemWithQuantityRequest, AddOrderAdapter.AddOrderViewHolder>(AddOrderComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOrderAdapter.AddOrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_request, parent, false);
        return AddOrderViewHolder(v);
    }

    override fun onBindViewHolder(holder: AddOrderAdapter.AddOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(order: MenuItemWithQuantityRequest) {
            itemView.id = order.getMenuItemId().toInt()
            itemView.addOrderMenuItem.text = order.getMenuItemName()
            itemView.addOrderQuantity.text = "#" + order.getQuantity()
        }
    }

    class AddOrderComparator: DiffUtil.ItemCallback<MenuItemWithQuantityRequest>() {
        override fun areItemsTheSame(oldItem: MenuItemWithQuantityRequest, newItem: MenuItemWithQuantityRequest): Boolean {
            return oldItem.getMenuItemId() == newItem.getMenuItemId()
        }

        override fun areContentsTheSame(oldItem: MenuItemWithQuantityRequest, newItem: MenuItemWithQuantityRequest): Boolean {
            return oldItem.getMenuItemId() == newItem.getMenuItemId()
        }
    }

}