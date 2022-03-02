package com.example.tfhmobile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfhmobile.dto.MessageDTO
import com.example.tfhmobile.dto.TableDTO
import com.example.tfhmobile.network.RetrofitService
import kotlinx.coroutines.launch

class TableViewModel(private val retrofitService: RetrofitService): ViewModel() {

    private val _tables: MutableLiveData<Resource<List<TableDTO>>> = MutableLiveData()
    val tables: LiveData<Resource<List<TableDTO>>> get() = _tables

    fun getTables() {
        _tables.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = retrofitService.getTables()
                _tables.value = Resource.Success(response.getData())
            } catch (ex: Exception) {
                _tables.value = Resource.Failure(ex)
            }
        }
    }
}