package com.example.tfhmobile.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfhmobile.R
import com.example.tfhmobile.activity.LoginActivity
import com.example.tfhmobile.dto.TableDTO
import com.example.tfhmobile.network.NetworkModule
import com.example.tfhmobile.network.RetrofitService
import com.example.tfhmobile.presentation.RegisterViewModel
import com.example.tfhmobile.presentation.RegisterViewModelFactory
import com.example.tfhmobile.presentation.Resource
import com.example.tfhmobile.presentation.TableViewModel
import com.example.tfhmobile.util.TableAdapter
import kotlinx.android.synthetic.main.fragment_tables.*

class TablesFragment : Fragment() {

    private lateinit var tableAdapter: TableAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val tableViewModel: TableViewModel by viewModels {
        RegisterViewModelFactory(NetworkModule.getClient(requireActivity()).create(RetrofitService::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tables, container, false)
    }

    private fun changeToOrdersFragment(table: TableDTO) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, OrdersFragment.newInstance(table))
        fragmentTransaction.commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tableAdapter = TableAdapter(::changeToOrdersFragment)
        viewManager = LinearLayoutManager(activity)
        tables_view.apply {
            adapter = tableAdapter
            layoutManager = viewManager
        }

        tableViewModel.getTables()
        tableViewModel.tables.observe(viewLifecycleOwner) {resource ->
            when(resource) {
                is Resource.Loading -> {
                    tableLoading.visibility = ProgressBar.VISIBLE
                }
                is Resource.Success -> {
                    tableLoading.visibility = ProgressBar.INVISIBLE
                    tableAdapter.submitList(resource.data)
                }
                is Resource.Failure -> {
                    tableLoading.visibility = ProgressBar.INVISIBLE
                    Toast.makeText(activity, resource.error.message, Toast.LENGTH_LONG).show()
                    if (resource.error.message!!.contains("401")) {
                        changeToLoginActivity()
                    }
                }
            }
        }
    }

    private fun changeToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = TablesFragment()
    }
}