package com.example.tfhmobile.network

import com.example.tfhmobile.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): HttpResponse<JwtDTO>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): HttpResponse<MessageDTO>

    @GET("/api/tables/waiter")
    suspend fun getTables(): HttpResponse<List<TableDTO>>

    @GET("/api/tables/{id}/orders")
    suspend fun getOrders(@Path("id") tableId: Long): HttpResponse<List<OrderDTO>>

    @GET("/api/menu_items/all")
    suspend fun getMenuItemsByCategories(): HttpResponse<List<MenuItemsByCategoryDTO>>
}