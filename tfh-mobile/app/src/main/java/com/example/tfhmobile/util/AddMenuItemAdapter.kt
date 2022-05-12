package com.example.tfhmobile.util

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.MenuItemDTO
import com.example.tfhmobile.dto.MenuItemWithQuantityRequest
import com.example.tfhmobile.dto.OrderRequest
import kotlinx.android.synthetic.main.add_menu_item.view.*

class AddMenuItemAdapter(private val orderAdapter: AddOrderAdapter,
                         private val request: OrderRequest) : ListAdapter<MenuItemDTO,
        AddMenuItemAdapter.AddMenuItemViewHolder>(AddMenuItemAdapter.AddMenuItemComparator()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMenuItemAdapter.AddMenuItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.add_menu_item, parent, false);
        return AddMenuItemViewHolder(v);
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: AddMenuItemAdapter.AddMenuItemViewHolder, position: Int) {
        holder.bind(getItem(position), request, orderAdapter)
    }

    inner class AddMenuItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(item: MenuItemDTO, request: OrderRequest, orderAdapter: AddOrderAdapter) {
            itemView.id = item.getId().toInt()
            itemView.addMenuItemName.text = item.getName()
            itemView.menuItemQuantity.text = "0"

            itemView.incQuantityButton.setOnClickListener {
                val quantity = itemView.menuItemQuantity.text.toString().toInt() + 1;
                if (quantity == 1) {
                    val itemToAdd = MenuItemWithQuantityRequest(item.getId(), item.getName(), 1);
                    request.addItem(itemToAdd)
                } else {
                    for (menuItem in request.getItems()) {
                        if (menuItem.getMenuItemId() == item.getId()) {
                            menuItem.increase()
                        }
                    }
                }
                itemView.menuItemQuantity.text = quantity.toString()
                orderAdapter.submitList(null)
                orderAdapter.submitList(request.getItems())
            }

            itemView.decQuantityButton.setOnClickListener {
                val quantity = itemView.menuItemQuantity.text.toString().toInt() - 1;
                if (quantity >= 0) {
                    if (quantity == 0) {
                        request.removeItem(item.getId())
                    } else {
                        for (menuItem in request.getItems()) {
                            if (menuItem.getMenuItemId() == item.getId()) {
                                menuItem.decrease()
                            }
                        }
                    }
                    itemView.menuItemQuantity.text = quantity.toString()
                    orderAdapter.submitList(null)
                    orderAdapter.submitList(request.getItems())
                }
            }
        }
    }

    class AddMenuItemComparator: DiffUtil.ItemCallback<MenuItemDTO>() {
        override fun areItemsTheSame(oldItem: MenuItemDTO, newItem: MenuItemDTO): Boolean {
            return oldItem.getId() == newItem.getId()
        }

        override fun areContentsTheSame(oldItem: MenuItemDTO, newItem: MenuItemDTO): Boolean {
            return oldItem.getId() == newItem.getId()
        }
    }

}