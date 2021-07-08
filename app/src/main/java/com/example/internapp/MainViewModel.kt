package com.example.internapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.repository.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _comicList = MutableLiveData<List<Comic>?>()
    val comicList: LiveData<List<Comic>?>
        get() = _comicList


    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = UIState.OnError
    }

    init {
        getMarvelAppComics()
    }

    private fun getMarvelAppComics() {
        _uiState.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            _comicList.value = MarvelApiRepository().getData()
            _uiState.value = UIState.OnSuccess
        }
    }
}