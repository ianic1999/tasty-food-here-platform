package com.example.tfhmobile.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.MenuItemsByCategoryDTO
import com.example.tfhmobile.dto.TableDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MenuItemsFragment(table: TableDTO) : Fragment() {

    private lateinit var items: List<MenuItemsByCategoryDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = Gson()
        val json = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("menuItems", "")
        val type = object : TypeToken<List<MenuItemsByCategoryDTO>>() {}.type
        items = gson.fromJson(json, type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_items, container, false)
    }

    companion object {
        fun newInstance(table: TableDTO) = MenuItemsFragment(table)
    }
}