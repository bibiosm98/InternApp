package com.example.internapp.repository

sealed class UIState {
    object OnSuccess : UIState()
    object OnError : UIState()
    object InProgress : UIState()
}