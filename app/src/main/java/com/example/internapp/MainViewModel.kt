package com.example.internapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.repository.Comic
import com.example.internapp.repository.MarvelApiRepository
import com.example.internapp.repository.UIState
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
        Log.i("STATE", "ERROR?" + uiState.value.toString())
    }

    init {
        getMarvelAppComics(null)
    }

    fun getMarvelAppComics(title: String?) {
        _uiState.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            Log.i("TEXT2", title.toString())
            _comicList.value = listOf()
            _comicList.value = MarvelApiRepository().getData(title)
            _uiState.value = UIState.OnSuccess
        }
    }

    fun selectComic(position: Int) {
        _selectedComic.value = _comicList.value?.get(position)
    }

    fun setUIState(state: UIState) {
        _uiState.value = state
    }

    fun clearComicList(){
        _comicList.value = listOf()
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
}