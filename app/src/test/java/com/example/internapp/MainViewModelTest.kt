package com.example.internapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.internapp.repository.MarvelApiRepository
import com.example.internapp.repository.UIState
import com.google.firebase.FirebaseApp
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var marvelApiRepository: MarvelApiRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setupViewModel() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())

        val marvelApiRepository = mockk<MarvelApiRepository>()
        mainViewModel = MainViewModel(marvelApiRepository)
    }

//    @Test
//    fun aa(){
////        val value = mainViewModel.selectedComic.getOrAwaitValue()
////        assertThat(value.(), (not(nullValue())))
//
//        assertThat(0f, `is`(0f))
////        assertThat(result.completedTasksPercent, `is`(0f))
//    }

    @Test
    fun setUIState_onWaiting_returnsOnWaiting(){
        mainViewModel.setUIState(UIState.OnWaiting)
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnWaiting))
    }

    @Test
    fun setUIState_inProgress_returnsInProgress(){
        mainViewModel.setUIState(UIState.InProgress)
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.InProgress))
    }

    @Test
    fun overrideComicList_true_returnsTrue(){
        mainViewModel.overrideComicList(true)
        val overrideComicList = mainViewModel.overrideComicList.getOrAwaitValue()
        assertThat(overrideComicList, `is`(true))
    }

    @Test
    fun overrideComicList_false_returnsFalse(){
        mainViewModel.overrideComicList(false)
        val overrideComicList = mainViewModel.overrideComicList.getOrAwaitValue()
        assertThat(overrideComicList, `is`(false))
    }

    @Test
    fun clearComicList_listEmpty_returnsZero(){
        mainViewModel.clearComicList()
        val comicList = mainViewModel.comicList.getOrAwaitValue()
        assertThat(comicList?.size, `is`(0))
    }

    @Test
    fun clearComicList_listEmptyOverrideTrue_returnsZeroInProgress(){
        mainViewModel.overrideComicList(true)
        val overrideComicList = mainViewModel.overrideComicList.getOrAwaitValue()
        assertThat(overrideComicList, `is`(true))

        mainViewModel.clearComicList()
        val comicList = mainViewModel.comicList.getOrAwaitValue()
        assertThat(comicList?.size, `is`(0))

        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.InProgress))
    }

    @Test
    fun clearComicList_listEmptyOverrideFalse_returnsZeroOnWaiting(){
        mainViewModel.overrideComicList(false)
        val overrideComicList = mainViewModel.overrideComicList.getOrAwaitValue()
        assertThat(overrideComicList, `is`(false))

        mainViewModel.clearComicList()
        val comicList = mainViewModel.comicList.getOrAwaitValue()
        assertThat(comicList?.size, `is`(0))

        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnWaiting))
    }

    @Test
    fun isComicListEmpty_listEmpty_returnsInProgress(){
        //mainViewModel set Comics, required fake API  Repository, next step
        //test fail, cause Repo throw exception UIState.OnError
        mainViewModel.clearComicList()
        mainViewModel.isComicListEmpty()
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.InProgress))
        assertThat(mainViewModel.comicList.value?.size, `is`(0))
    }

    @Test
    fun isComicListEmpty_listNotEmpty_returnsOnSuccess(){
        //mainViewModel set Comics, required fake API  Repository, next step
        //test fail, cause Repo throw exception UIState.OnError
        mainViewModel.isComicListEmpty()
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnSuccess))
    }

    @Test
    fun checkBackupComicList_listEmpty_returnsInProgress(){
        //mainViewModel set Comics, required fake API  Repository, next step
        //test fail, cause Repo throw exception UIState.OnError
        mainViewModel.isComicListEmpty()
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.InProgress))
    }

    @Test
    fun checkBackupComicList_listNotEmpty_returnsComicListSizeNotZero(){
        mainViewModel.checkBackupComicList()
        mainViewModel.clearComicList()
        val comicList = mainViewModel.comicList.getOrAwaitValue()
        assertThat(comicList?.size, `is`(0))
    }

//TODO implement
//    getAuthors()
//    signOutUser()
//    setBottomSheetState()
//    getUserData()
//    isUserLoggedIn()
//    fake MarvelRepository
}