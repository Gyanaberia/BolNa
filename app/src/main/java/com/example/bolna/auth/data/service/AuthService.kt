package com.example.bolna.auth.data.service

import com.example.bolna.auth.data.dto.AuthResponse
import com.example.bolna.auth.data.dto.GoogleOAuthRequest
import com.example.bolna.auth.data.dto.LoginRequest
import com.example.bolna.auth.data.dto.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface AuthService{
    @POST("chat-me/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @POST("chat-me/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("chat-me/oauth2/google")
    suspend fun googleOAuthLoginUser(@Body request: GoogleOAuthRequest): Response<AuthResponse>


}
