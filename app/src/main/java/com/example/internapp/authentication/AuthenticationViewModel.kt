package com.example.internapp.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.internapp.repository.FirebaseRepository
import com.example.internapp.repository.UIState

class AuthenticationViewModel : ViewModel() {
    val repository = FirebaseRepository()

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    fun createUser(email: String, password: String) {
        _uiState.value = UIState.InProgress
        repository.signUp(email, password)
            .addOnSuccessListener {
                _uiState.value = UIState.OnSuccess
            }.addOnFailureListener {
                _uiState.value = UIState.OnError
            }
    }

    fun authenticateUser(email: String, password: String) {
        _uiState.value = UIState.InProgress
        repository.signIn(email, password)
            .addOnSuccessListener {
                _uiState.value = UIState.OnSuccess
            }.addOnFailureListener {
                _uiState.value = UIState.OnError
            }
    }

    fun navigationDone() {
        _uiState.value = UIState.OnWaiting
    }

    fun isCurrentUser() {
        if (repository.currentUser() != null) {
            _uiState.value = UIState.OnSuccess
        }
    }
}