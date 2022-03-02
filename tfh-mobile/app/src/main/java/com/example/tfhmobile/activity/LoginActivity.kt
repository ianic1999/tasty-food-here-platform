package com.example.tfhmobile.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.JwtDTO
import com.example.tfhmobile.dto.LoginRequest
import com.example.tfhmobile.network.NetworkModule
import com.example.tfhmobile.network.RetrofitService
import com.example.tfhmobile.presentation.LoginViewModel
import com.example.tfhmobile.presentation.RegisterViewModelFactory
import com.example.tfhmobile.presentation.Resource
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels {
        RegisterViewModelFactory(NetworkModule.getClient(this).create(RetrofitService::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        removeToken()

        register_link.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val request = LoginRequest(
                phone.text.toString(),
                password.text.toString()
            )
            loginViewModel.login(request)
            loginViewModel.jwt.observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        handleLogin(resource.data)
                    }
                    is Resource.Failure -> {
                        Toast.makeText(this, resource.error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun handleLogin(jwt: JwtDTO) {
        val prefs: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.putString("token", jwt.getToken())
        edit.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun removeToken() {
        val prefs: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.remove("token")
        edit.apply()
    }


}