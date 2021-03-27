package com.castmart.authentication.dto

data class LoginRequest(var username: String, var password: String)
data class LoginResponse(var token: String)
