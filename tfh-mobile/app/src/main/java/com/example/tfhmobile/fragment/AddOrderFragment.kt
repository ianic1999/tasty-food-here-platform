package com.example.tfhmobile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.*
import com.example.tfhmobile.network.NetworkModule
import com.example.tfhmobile.network.RetrofitService
import com.example.tfhmobile.util.AddMenuItemAdapter
import com.example.tfhmobile.util.AddOrderAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_add_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddOrderFragment(private val table: TableDTO) : Fragment() {

    private lateinit var items: List<MenuItemsByCategoryDTO>
    private var request: OrderRequest = OrderRequest(table.getCurrentBookingId()!!)
    private lateinit var menuItemAdapter: AddMenuItemAdapter
    private lateinit var orderAdapter: AddOrderAdapter
    private lateinit var menuViewManager: RecyclerView.LayoutManager
    private lateinit var orderViewManager: RecyclerView.LayoutManager
    private lateinit var retrofitService: RetrofitService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retrofitService =
            NetworkModule.getClient(requireActivity()).create(RetrofitService::class.java)

        val gson = Gson()
        val json = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("menuItems", "")
        val type = object : TypeToken<List<MenuItemsByCategoryDTO>>() {}.type
        items = gson.fromJson(json, type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        orderAdapter = AddOrderAdapter()
        orderViewManager = LinearLayoutManager(activity)
        addOrderView.apply {
            adapter = orderAdapter
            layoutManager = orderViewManager
        }

        menuItemAdapter = AddMenuItemAdapter(orderAdapter, request)
        menuViewManager = LinearLayoutManager(activity)
        addMenuItemsView.apply {
            adapter = menuItemAdapter
            layoutManager = menuViewManager
        }


        val menu: MutableList<MenuItemDTO> = mutableListOf();
        for (category in items) {
            for (i in category.getItems()) {
                menu.add(i)
            }
        }
        menuItemAdapter.submitList(menu)

        filterItems.addTextChangedListener {
            val text = filterItems.text.toString()
            val menu: MutableList<MenuItemDTO> = mutableListOf();
            for (category in items) {
                for (i in category.getItems()) {
                    if (i.getName().uppercase().contains(text.uppercase())) {
                        menu.add(i)
                    }
                }
            }
            menuItemAdapter.submitList(menu)
        }

        confirmOrderRequestButton.setOnClickListener {
            retrofitService.addOrder(request).enqueue(object :
                Callback<HttpResponse<OrderDTO>> {
                override fun onResponse(
                    call: Call<HttpResponse<OrderDTO>>,
                    response: Response<HttpResponse<OrderDTO>>
                ) {
                    changeToOrdersFragment(table)
                }

                override fun onFailure(call: Call<HttpResponse<OrderDTO>>, t: Throwable) {
                    showToast()
                }
            })
        }
    }

    private fun showToast() {
        Toast.makeText(requireContext(), "Some internet problems", Toast.LENGTH_LONG).show();
    }

    private fun changeToOrdersFragment(table: TableDTO) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, OrdersFragment.newInstance(table))
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(table: TableDTO) = AddOrderFragment(table)
    }
}