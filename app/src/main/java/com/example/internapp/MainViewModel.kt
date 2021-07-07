package com.example.internapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.network.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _comicList = MutableLiveData<List<Comic>?>()
    val comicList: LiveData<List<Comic>?>
        get() = _comicList

    init {
        getMarvelAppComics()
    }

    fun getMarvelAppComics() {
        viewModelScope.launch {
            _comicList.value = MarvelApiRepository().getData()
        }
    }
}