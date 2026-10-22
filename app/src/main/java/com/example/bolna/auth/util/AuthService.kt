package com.example.bolna.auth.util

import com.example.bolna.auth.model.AuthResponse
import com.example.bolna.auth.model.GoogleOAuthRequest
import com.example.bolna.auth.model.LoginRequest
import com.example.bolna.auth.model.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response


private const val BASE_URL = "https://chatme-production-e072.up.railway.app/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface AuthService{
    @POST("chat-me/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @POST("chat-me/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("chat-me/oauth2/google")
    suspend fun googleOAuthLoginUser(@Body request: GoogleOAuthRequest): Response<AuthResponse>
}

object AuthApi{
    val retrofitService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
}