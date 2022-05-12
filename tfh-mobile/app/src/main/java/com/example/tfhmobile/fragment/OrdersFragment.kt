package com.example.tfhmobile.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.OrderDTO
import com.example.tfhmobile.dto.TableDTO
import com.example.tfhmobile.network.NetworkModule
import com.example.tfhmobile.network.RetrofitService
import com.example.tfhmobile.presentation.OrderViewModel
import com.example.tfhmobile.presentation.RegisterViewModelFactory
import com.example.tfhmobile.presentation.Resource
import com.example.tfhmobile.presentation.TableViewModel
import com.example.tfhmobile.util.MenuItemAdapter
import com.example.tfhmobile.util.OrderAdapter
import com.example.tfhmobile.util.TableAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_tables.*


class OrdersFragment(private val table: TableDTO) : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var menuItemAdapter: MenuItemAdapter
    private lateinit var menuItemViewManager: RecyclerView.LayoutManager

    private val orderViewModel: OrderViewModel by viewModels {
        RegisterViewModelFactory(
            NetworkModule.getClient(requireActivity()).create(RetrofitService::class.java)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    private fun clickOrderFunction(order: OrderDTO) {
        menuItemAdapter.submitList(order.getItems())
        orderPrice.text = "= " + order.getItems().map { item -> item.getPrice() }
            .reduce {a, b -> a + b}
            .toString() + " MDL"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        menuItemAdapter = MenuItemAdapter()
        orderAdapter = OrderAdapter(::clickOrderFunction)
        viewManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        menuItemViewManager = LinearLayoutManager(activity)
        ordersView.apply {
            adapter = orderAdapter
            layoutManager = viewManager
        }

        menuItemsView.apply {
            adapter = menuItemAdapter
            layoutManager = menuItemViewManager
        }

        orderViewModel.getOrders(table.getId())
        orderViewModel.orders.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    orderLoading.visibility = ProgressBar.VISIBLE;
                }
                is Resource.Success -> {
                    orderLoading.visibility = ProgressBar.INVISIBLE;
                    orderAdapter.submitList(resource.data)
                    totalOrderPrice.text = "= " + resource.data.map { order -> order.getItems() }
                        .map { items ->
                            items.map { item -> item.getPrice() }.reduce { a, b -> a + b }
                        }
                        .reduce { a, b -> a + b }
                        .toString() + " lei"
                    clickOrderFunction(resource.data[0])
                }
                is Resource.Failure -> {
                    orderLoading.visibility = ProgressBar.INVISIBLE;
                    Toast.makeText(activity, resource.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        addOrderButton.setOnClickListener {
            changeToAddOrderFragment()
        }

        closeTableButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Please confirm.")
                .setMessage("Do you want to close this table?")
                .setPositiveButton("Yes") { _, _ ->
                    closeTable()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun closeTable() {
        orderViewModel.close(table.getCurrentBookingId()!!)
        orderViewModel.closed.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    if (resource.data) {
                        Toast.makeText(requireContext(), "Table closed.", Toast.LENGTH_LONG).show();
                        changeToTablesFragment()
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(activity, resource.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeToAddOrderFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, AddOrderFragment.newInstance(table))
        fragmentTransaction.commit()
    }

    private fun changeToTablesFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, TablesFragment.newInstance())
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(table: TableDTO) = OrdersFragment(table)
    }
}