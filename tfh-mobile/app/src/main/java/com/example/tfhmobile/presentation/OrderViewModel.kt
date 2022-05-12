package com.example.tfhmobile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfhmobile.dto.OrderDTO
import com.example.tfhmobile.network.RetrofitService
import kotlinx.coroutines.launch

class OrderViewModel(private val retrofitService: RetrofitService) : ViewModel() {

    private val _orders: MutableLiveData<Resource<List<OrderDTO>>> = MutableLiveData()
    val orders: LiveData<Resource<List<OrderDTO>>> get() = _orders
    private val _closed: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val closed: LiveData<Resource<Boolean>> get() = _closed;

    fun getOrders(tableId: Long) {
        _orders.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = retrofitService.getOrders(tableId)
                _orders.value = Resource.Success(response.getData())
            } catch (ex: Exception) {
                _orders.value = Resource.Failure(ex)
            }
        }
    }

    fun close(id: Long) {
        viewModelScope.launch {
            try {
                retrofitService.closeBooking(id)
                _closed.value = Resource.Success(true)
            } catch (ex: Exception) {
                ex.printStackTrace();
                _closed.value = Resource.Failure(ex)
            }
        }
    }
}