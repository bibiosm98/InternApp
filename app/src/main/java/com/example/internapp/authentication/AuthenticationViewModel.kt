package com.example.internapp.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.internapp.repository.FirebaseRepository
import com.example.internapp.repository.UIState
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {
    val repository = FirebaseRepository(FirebaseAuth.getInstance())

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    init {
        _uiState.value = UIState.OnWaiting
    }

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
            }.addOnCompleteListener {
                _uiState.value = UIState.OnWaiting
            }
    }

    fun navigationDone() {
        _uiState.value = UIState.OnWaiting
    }

    fun setUIState(state: UIState) {
        _uiState.value = state
    }

    fun isUserLoggedIn() {
        _uiState.value = UIState.InProgress
        if (repository.isUserLoggedIn()) {
            _uiState.value = UIState.OnSuccess
        } else {
            _uiState.value = UIState.OnNotLoggedIn
        }
    }
}