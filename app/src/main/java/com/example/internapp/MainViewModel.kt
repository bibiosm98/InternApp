package com.example.internapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.repository.Comic
import com.example.internapp.repository.FirebaseRepository
import com.example.internapp.repository.MarvelApiRepository
import com.example.internapp.repository.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val marvelApiRepository: MarvelApiRepository
) : ViewModel() {

    val repository = FirebaseRepository()

    private val _backupHomeComicList = MutableLiveData<List<Comic>?>()
    private val _comicList = MutableLiveData<List<Comic>?>()
    val comicList: LiveData<List<Comic>?>
        get() = _comicList

    private val _selectedComic = MutableLiveData<Comic>()
    val selectedComic: LiveData<Comic>
        get() = _selectedComic

    private val _navigatedFromHome = MutableLiveData<Boolean>()
    val navigatedFromHome: LiveData<Boolean>
        get() = _navigatedFromHome

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    private val _bottomSheetState = MutableLiveData<Int>()
    val bottomSheetState: LiveData<Int>
        get() = _bottomSheetState

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.value = UIState.OnError
    }

    init {
        _navigatedFromHome.value = true
        isUserLoggedIn()
    }

    fun getAllMarvelAppComics() {
        _uiState.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            _comicList.value = listOf()
            _comicList.value = marvelApiRepository.getAllData()
            _backupHomeComicList.value = _comicList.value
            _uiState.value = UIState.OnSuccess
        }
    }

    fun getMarvelAppComicsByTitle(title: String?) {
        _uiState.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            _comicList.value = listOf()
            _comicList.value = marvelApiRepository.getMoviesWithTitles(title)
            _uiState.value = UIState.OnSuccess
        }
    }

    fun selectComic(position: Int) {
        _selectedComic.value = _comicList.value?.get(position)
    }

    fun setUIState(state: UIState) {
        _uiState.value = state
    }

    fun navigatedFromHome(bool: Boolean) {
        _navigatedFromHome.value = bool
    }

    fun isComicListEmpty() {
        if (_comicList.value?.size == null) {
            _uiState.value = UIState.InProgress
            getAllMarvelAppComics()
        } else {
            _uiState.value = UIState.OnSuccess
        }
    }

    fun checkBackupComicList() {
        if (_backupHomeComicList.value?.size != 0) {
            _comicList.value = _backupHomeComicList.value
        } else {
            getAllMarvelAppComics()
        }
    }

    fun clearComicList() {
        _comicList.value = listOf()
        if (_navigatedFromHome.value == true) {
            _uiState.value = UIState.OnWaiting
        } else {
            _uiState.value = UIState.InProgress
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

    fun setBottomSheetState(state: Int) {
        _bottomSheetState.value = state
    }

    fun getUserData(): String? {
        return repository.currentUser()?.email
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