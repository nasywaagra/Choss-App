package com.project.chossapp.data.repository

import android.content.Context
import android.util.Log
import com.project.chossapp.data.Preferences
import com.project.chossapp.data.model.User
import com.project.chossapp.data.remote.ApiService
import com.project.chossapp.util.Dimen.EMAIL
import com.project.chossapp.util.Dimen.TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException

class AuthRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    private val _registerState = MutableStateFlow("")
    private val _loginState = MutableStateFlow("")

    suspend fun login(email: String, password: String): Flow<String> {
        try {
            val client = apiService.login(email, password)
            if (client.isSuccessful) {
                val token = client.body()?.data?.accessToken
                token?.let {
                    Preferences.saveToken(token, email, context)
                    _loginState.value = "success"
                }
            } else {
                throw HttpException(client)
            }
        } catch (e: HttpException) {
            Log.e("HTTP_EXCEPTION: ", e.message())
        }

        return _loginState.asStateFlow()
    }

    suspend fun register(username: String, email: String, password: String): Flow<String> {
        try {
            val client = apiService.register(username, email, password)
            if (client.isSuccessful) {
                _registerState.value = "success"
            } else {
                throw HttpException(client)
            }
        } catch (e: HttpException) {
            Log.e("HTTP_EXCEPTION: ", e.message())
        }

        return _registerState.asStateFlow()
    }

    fun getSignedInUser(): User {
        val sharedPref = Preferences.init(context, "session")
        val email = sharedPref.getString(EMAIL, "")

        return User(
            email = email,
        )
    }

    fun isUserSignedIn(): Boolean {
        val sharedPref = Preferences.init(context, "session")
        val token = sharedPref.getString(TOKEN, "")

        return token != ""
    }

    fun logOut() {
        Preferences.logOut(context)
    }
}