package com.example.tfhmobile.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tfhmobile.R
import com.example.tfhmobile.fragment.TablesFragment
import com.example.tfhmobile.network.NetworkModule
import com.example.tfhmobile.network.RetrofitService
import com.example.tfhmobile.util.ConnectionLiveData
import com.example.tfhmobile.util.ConnectionModel
import com.example.tfhmobile.util.InternetConnectionDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerNetworkListener()
        setContentView(R.layout.activity_main)
        retrofitService = NetworkModule.getClient(this).create(RetrofitService::class.java)
        loadMenuItems()
        setFragment(TablesFragment.newInstance())
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragment_container.id, fragment)
        transaction.commit()
    }

    private fun loadMenuItems() {
        lifecycleScope.launch {
            try {
                val items = retrofitService.getMenuItemsByCategories().getData()
                val prefs: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val edit: SharedPreferences.Editor = prefs.edit()
                val gson = Gson()
                edit.putString("menuItems", gson.toJson(items))
                edit.apply()
                Toast.makeText(baseContext, "Menu items loaded", Toast.LENGTH_LONG).show()
            } catch (ex: IOException) {
                Toast.makeText(baseContext, ex.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerNetworkListener() {
        val noInternetDialogFragment = InternetConnectionDialogFragment()
        supportFragmentManager.beginTransaction()
            .add(noInternetDialogFragment, "INTERNET_CONNECTION")
        noInternetDialogFragment.isCancelable = false

        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(
            this
        ) { connection: ConnectionModel ->
            if (connection.isConnected()) {
                when (connection.getType()) {
                    1 -> {
                        noInternetDialogFragment.dismiss()
                        Toast.makeText(this, "Wifi turned ON", Toast.LENGTH_SHORT)
                            .show()
                    }
                    2 -> {
                        noInternetDialogFragment.dismiss()
                        Toast.makeText(
                            this,
                            "Mobile data turned ON",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                noInternetDialogFragment.show(
                    supportFragmentManager,
                    "INTERNET_CONNECTION"
                )
                Toast.makeText(this, "Connections turned OFF", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}