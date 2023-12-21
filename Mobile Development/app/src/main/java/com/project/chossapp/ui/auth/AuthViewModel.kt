package com.project.chossapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.chossapp.data.model.User
import com.project.chossapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _loginState = MutableStateFlow("")
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow("")
    val registerState = _registerState.asStateFlow()

    private val _user = MutableStateFlow(false)
    val user = _user.asStateFlow()

    private val _signedInUser = MutableStateFlow(User())
    val signedInUser = _signedInUser.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = authRepository.isUserSignedIn()
            _signedInUser.value = authRepository.getSignedInUser()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect {
                _loginState.value = it
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(username, email, password).collect {
                _registerState.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logOut()
        }
    }
}