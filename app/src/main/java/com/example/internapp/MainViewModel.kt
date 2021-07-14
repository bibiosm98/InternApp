package com.example.internapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.repository.Comic
import com.example.internapp.repository.FirebaseRepository
import com.example.internapp.repository.MarvelApiRepository
import com.example.internapp.repository.UIState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val repository = FirebaseRepository()
    private val _comicList = MutableLiveData<List<Comic>?>()
    val comicList: LiveData<List<Comic>?>
        get() = _comicList

    private val _selectedComic = MutableLiveData<Comic>()
    val selectedComic: LiveData<Comic>
        get() = _selectedComic

    private val _navigatedFromHome = MutableLiveData<Boolean>()

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = UIState.OnError
    }

    init {
        _navigatedFromHome.value = true
        getAllMarvelAppComics()
    }

    fun getAllMarvelAppComics() {
        _uiState.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            _comicList.value = listOf()
            _comicList.value = MarvelApiRepository().getAllData()
            _uiState.value = UIState.OnSuccess
        }
    }

    fun getMarvelAppComicsByTitle(title: String?) {
        _uiState.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            _comicList.value = listOf()
            _comicList.value = MarvelApiRepository().getMoviesWithTitles(title)
            _uiState.value = UIState.OnSuccess
        }
    }

    fun selectComic(position: Int) {
        _selectedComic.value = _comicList.value?.get(position)
    }

    fun setUIState(state: UIState) {
        _uiState.value = state
    }

    fun navigatedFrom() {
        _navigatedFromHome.value = false
    }

    fun isComicListEmpty() {
        if (_comicList.value?.size == 0) {
            _uiState.value = UIState.InProgress
            getAllMarvelAppComics()
        } else {
            _uiState.value = UIState.OnSuccess
        }
    }

    fun clearComicList() {
        if (_navigatedFromHome.value == true) {
            _comicList.value = listOf()
            _uiState.value = UIState.OnWaiting
        }
    }

    fun getAuthors(): String {
        var authorsList = ""
        val items = _selectedComic.value?.creators?.items
        items?.let {
            authorsList = "written by "
            items.forEach { auth ->
                authorsList += auth.name + ", "
            }
            authorsList = authorsList.substring(0, authorsList.length - 2);
        }
        return authorsList
    }

    fun signOutUser() {
        _uiState.value = UIState.OnWaiting
        repository.signOut()
    }

    fun getUserData(): String? {
        return repository.currentUser()?.email
    }
}