package com.example.tfhmobile.network

import android.app.Activity
import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkModule {
    private const val BASE_URL = "http://104.248.205.251:8081/"

    fun getClient(activity: Activity): Retrofit {
        val token = getToken(activity)
        Log.i("retrofit", token?: "null");
        val builder = OkHttpClient.Builder()
        if (token != null) {
            builder.addInterceptor(Interceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
                chain.proceed(request)
            })
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    private fun getToken(activity: Activity): String? {
        return activity.getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("token", "")
    }
}