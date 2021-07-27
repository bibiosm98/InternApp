package com.example.internapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.internapp.repository.*
import com.google.firebase.FirebaseApp
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.matchers.Null
import kotlin.reflect.jvm.internal.impl.resolve.constants.NullValue

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var marvelApiRepository: MarvelApiRepository
    private lateinit var mainViewModel: MainViewModel
    private var comicList = ArrayList<Comic>()

    @Before
    fun setupViewModel() {
        comicList.add(
            Comic(
                100,
                "Iron_Man_1",
                "Description IronMan1",
                Thumbnail("path to res", "JPG"),
                Creators(0, emptyList()),
                emptyList()
            )
        )
        comicList.add(
            Comic(
                101,
                "Iron_Man_2",
                "Description IronMan2",
                Thumbnail("path to res", "JPG"),
                Creators(0, emptyList()),
                emptyList()
            )
        )
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())

        val marvelApiRepository = mockk<MarvelApiRepository>()
        every {
            runBlocking {
                delay(3000)
                marvelApiRepository.getAllData()
            }
        } returns comicList
        mainViewModel = MainViewModel(marvelApiRepository)
    }

    @Test
    fun setUIState_onWaiting_returnsOnWaiting() {
        mainViewModel.setUIState(UIState.OnWaiting)
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnWaiting))
    }

    @Test
    fun setUIState_inProgress_returnsInProgress() {
        mainViewModel.setUIState(UIState.InProgress)
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.InProgress))
    }

    @Test
    fun overrideComicList_true_returnsTrue() {
        mainViewModel.overrideComicList(true)
        val overrideComicList = mainViewModel.overrideComicList.getOrAwaitValue()
        assertThat(overrideComicList, `is`(true))
    }

    @Test
    fun overrideComicList_false_returnsFalse() {
        mainViewModel.overrideComicList(false)
        val overrideComicList = mainViewModel.overrideComicList.getOrAwaitValue()
        assertThat(overrideComicList, `is`(false))
    }

    @Test
    fun clearComicList_listEmpty_returnsZero() {
        mainViewModel.clearComicList()
        val comicList = mainViewModel.comicList.getOrAwaitValue()
        assertThat(comicList?.size, `is`(0))
    }

    @Test
    fun clearComicList_listEmptyOverrideTrue_returnsZeroInProgress() {
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
    fun clearComicList_listEmptyOverrideFalse_returnsZeroOnWaiting() {
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
    fun isComicListEmpty_listEmpty_returnsOnSuccess() {
        mainViewModel.clearComicList()
        mainViewModel.isComicListEmpty()
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnSuccess))
        assertThat(mainViewModel.comicList.value?.size, `is`(2))
    }

    @Test
    fun isComicListEmpty_listNotEmpty_returnsOnSuccess() {
        mainViewModel.isComicListEmpty()
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnSuccess))
    }

    @Test
    fun checkBackupComicList_listEmpty_returnsOnSuccesss() {
        mainViewModel.clearBackupComicList()
        mainViewModel.checkBackupComicList()
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(UIState.OnSuccess))
    }

    @Test
    fun checkBackupComicList_listNotEmpty_returnsComicListSizeNotZero() {
        mainViewModel.checkBackupComicList()
        mainViewModel.clearComicList()
        val comicList = mainViewModel.comicList.getOrAwaitValue()
        assertThat(comicList?.size, `is`(0))
    }

    @Test
    fun selectComic_comicNotSelected_returnsNullValue() {
        assertThat(mainViewModel.selectedComic.value, `is`(nullValue()))
    }

    @Test
    fun selectComic_comicSelected_returnsNotNullValue() {
        mainViewModel.getAllMarvelAppComics()
        mainViewModel.selectComic(1)
        assertEquals(mainViewModel.comicList.value?.get(1), mainViewModel.selectedComic.value)
        assertThat(mainViewModel.selectedComic.value, `is`(not(nullValue())))
    }

    @Test
    fun signOutUser_userLoggedOut_returnsOnWaiting() {
        mainViewModel.signOutUser()
        assertEquals(mainViewModel.uiState.value, UIState.OnWaiting)
        val uiState = mainViewModel.uiState.getOrAwaitValue()
        assertThat(uiState, `is`(not(nullValue())))
        assertThat(mainViewModel.repository.currentUser(), `is`(nullValue()))
    }

    @Test
    fun setBottomSheetState_setNumberTwo_returnsNumberTwo() {
        mainViewModel.setBottomSheetState(2)
        assertThat(mainViewModel.bottomSheetState.value, `is`(2))
    }
}