package com.example.bolna.auth.data.dto

import com.google.gson.annotations.SerializedName


data class AuthResponse(val token: String, val message: String)
data class LoginRequest(
    @SerializedName("name")
    val userName: String,
    val password: String
)


data class RegisterRequest(
    @SerializedName("name")
    val userName:String,
    val email:String,
    val mobileNumber: String,
    val password: String)

data class GoogleOAuthRequest(val token: String)