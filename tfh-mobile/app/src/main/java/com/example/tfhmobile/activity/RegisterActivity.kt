package com.example.tfhmobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.tfhmobile.R
import com.example.tfhmobile.dto.RegisterRequest
import com.example.tfhmobile.network.NetworkModule
import com.example.tfhmobile.network.RetrofitService
import com.example.tfhmobile.presentation.RegisterViewModel
import com.example.tfhmobile.presentation.RegisterViewModelFactory
import com.example.tfhmobile.presentation.Resource
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), LifecycleOwner {

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(NetworkModule.getClient(this).create(RetrofitService::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {

            val password = password.text.toString()
            val confirmPassword = confirm_password.text.toString()

            if(!arePasswordsTheSame(password, confirmPassword)) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show()
            } else {

                val request = RegisterRequest(
                    first_name.text.toString(),
                    last_name.text.toString(),
                    phone.text.toString(),
                    email.text.toString(),
                    password
                )
                registerViewModel.register(request)
                registerViewModel.message.observe(this) { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            Toast.makeText(this, resource.data.getMessage(), Toast.LENGTH_LONG)
                                .show()
                            val intent: Intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        is Resource.Failure -> {
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                            resource.error.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun arePasswordsTheSame(password: String, confirmPassword: String) : Boolean{
        return password == confirmPassword
    }
}