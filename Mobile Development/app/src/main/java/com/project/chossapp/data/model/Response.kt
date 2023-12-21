package com.project.chossapp.data.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val status: String? = null,
    val data: String? = null
)

data class LoginResponse(
    val status: String? = null,
    val data: LoginData? = null
)

data class LoginData(
    @field:SerializedName("access_token")
    val accessToken: String? = null,
    val type: String? = null
)