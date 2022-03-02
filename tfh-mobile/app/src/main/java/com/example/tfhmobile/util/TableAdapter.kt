package com.example.tfhmobile.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.TableDTO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.table.view.*

class TableAdapter(val changeFragmentFunction: ((TableDTO) -> Unit)) : ListAdapter<TableDTO, TableAdapter.TableViewHolder>(TableComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.table, parent, false)
        return TableViewHolder(v)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(table: TableDTO) {
            itemView.id = table.getId().toInt()
            itemView.table_number.text = "Table #" + table.getId()

            itemView.setOnClickListener {
                changeFragmentFunction(table)
            }
        }
    }

    class TableComparator: DiffUtil.ItemCallback<TableDTO>() {
        override fun areItemsTheSame(oldItem: TableDTO, newItem: TableDTO): Boolean {
            return oldItem.getId() == newItem.getId()
        }

        override fun areContentsTheSame(oldItem: TableDTO, newItem: TableDTO): Boolean {
            return oldItem.getId() == newItem.getId()
        }
    }
}