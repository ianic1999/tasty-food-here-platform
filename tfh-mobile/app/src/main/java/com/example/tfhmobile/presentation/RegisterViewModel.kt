package com.example.tfhmobile.presentation

import androidx.lifecycle.*
import com.example.tfhmobile.dto.MessageDTO
import com.example.tfhmobile.dto.RegisterRequest
import com.example.tfhmobile.network.RetrofitService
import kotlinx.coroutines.launch

class RegisterViewModel(private val retrofitService: RetrofitService) : ViewModel() {

    private val _message: MutableLiveData<Resource<MessageDTO>> = MutableLiveData()
    val message: LiveData<Resource<MessageDTO>> get() = _message;

    fun register(request: RegisterRequest) {
        _message.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = retrofitService.register(request)
                _message.value = Resource.Success(response.getData())
            } catch (ex: Exception) {
                _message.value = Resource.Failure(ex)
            }
        }
    }
}