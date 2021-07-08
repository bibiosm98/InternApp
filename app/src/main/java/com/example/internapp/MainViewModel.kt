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

    private val _selectedComic = MutableLiveData<Comic>()
    val selectedComic: LiveData<Comic>
        get() = _selectedComic

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
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

    fun selectComic(position: Int) {
        _selectedComic.value = _comicList.value?.get(position)
    }

    fun getAuthors(): String {
        var authorsList = ""
        val items = _selectedComic.value?.creators?.items
        if (!items?.isEmpty()!!) {
            authorsList = "written by "
            items.forEach { auth ->
                authorsList += auth.name + ", "
            }
            authorsList = authorsList.substring(0, authorsList.length - 2);
        }
        return authorsList
    }
}