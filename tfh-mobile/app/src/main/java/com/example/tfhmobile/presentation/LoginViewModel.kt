package com.example.tfhmobile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfhmobile.dto.JwtDTO
import com.example.tfhmobile.dto.LoginRequest
import com.example.tfhmobile.dto.MessageDTO
import com.example.tfhmobile.dto.RegisterRequest
import com.example.tfhmobile.network.RetrofitService
import kotlinx.coroutines.launch

class LoginViewModel(private val retrofitService: RetrofitService): ViewModel() {

    private val _jwt: MutableLiveData<Resource<JwtDTO>> = MutableLiveData()
    val jwt: LiveData<Resource<JwtDTO>> get() = _jwt;

    fun login(request: LoginRequest) {
        _jwt.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = retrofitService.login(request)
                _jwt.value = Resource.Success(response.getData())
            } catch (ex: Exception) {
                _jwt.value = Resource.Failure(Exception("Authentication error"))
            }
        }
    }

}