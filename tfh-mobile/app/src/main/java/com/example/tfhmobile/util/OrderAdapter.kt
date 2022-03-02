package com.example.tfhmobile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.OrderDTO
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.order.view.*

class OrderAdapter(private val clickOrderFunction: ((OrderDTO) -> Unit)): ListAdapter<OrderDTO, OrderAdapter.OrderViewHolder>(OrderComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order, parent, false);
        return OrderViewHolder(v);
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(order: OrderDTO, position: Int) {
            itemView.id = order.getId().toInt()
            (position + 1).toString().also { itemView.orderNumber.text = it }

            itemView.orderButton.setOnClickListener {
                clickOrderFunction(order)
            }
        }
    }

    class OrderComparator: DiffUtil.ItemCallback<OrderDTO>() {
        override fun areItemsTheSame(oldItem: OrderDTO, newItem: OrderDTO): Boolean {
            return oldItem.getId() == newItem.getId()
        }

        override fun areContentsTheSame(oldItem: OrderDTO, newItem: OrderDTO): Boolean {
            return oldItem.getId() == newItem.getId()
        }
    }
}