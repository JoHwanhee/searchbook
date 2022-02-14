package com.hwanhee.searchbook.ui.feature.books

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hwanhee.searchbook.base.BaseViewModel
import com.hwanhee.searchbook.base.Paging
import com.hwanhee.searchbook.base.SearchKeyword
import com.hwanhee.searchbook.model.BookRepository
import com.hwanhee.searchbook.model.toPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BookRepository
) : BaseViewModel<
        BooksContract.Event,
        BooksContract.State,
        BooksContract.Effect>() {

    private var page: Paging
    private var lastWordCache = ""

    // server has no response that count of page, so that client need to set default value to 10
    private val perCount = 10

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState


    init {
        page = Paging(
            currentPage = 1,
            total = 0,
            perCount = perCount
        )
        getNewBookItems()
    }

    override fun setInitialState() =
        BooksContract.State(books = listOf(), isLoading = true, isSearchOpened = false)

    override fun handleEvents(event: BooksContract.Event) {
        when (event) {
            is BooksContract.Event.BookSelection -> {
                bookSelection(event.id)
            }

            is BooksContract.Event.ScrollMeetsBottom -> {
                if (increaseIfNeedMore()){
                    searchBooks(SearchKeyword(lastWordCache), page)
                }
            }

            is BooksContract.Event.SearchOn -> {
                setSearchState()
            }

            is BooksContract.Event.SearchOff -> {
                clearSearchWord()
                getNewBookItems()
            }

            is BooksContract.Event.UpdateSearchText -> {
                updateSearchTextState(event.value)
            }

            is BooksContract.Event.Search -> {
                page.init()
                setState {
                    BooksContract.State(books = emptyList(), isLoading = false, isSearchOpened = true)
                }
                searchBooks(SearchKeyword(event.searchWord), page)
                lastWordCache = event.searchWord
            }
        }
    }

    private fun clearSearchWord() {
        _searchTextState.value = ""
        lastWordCache = ""
    }

    private fun increaseIfNeedMore() : Boolean {
        var searchMode = false
        currentState {
            searchMode = isSearchOpened
        }

        return searchMode && page.increaseIfNeedMore()
    }

    private fun setSearchState() {
        page.init()
        setState {
            BooksContract.State(books = books, isLoading = false, isSearchOpened = true)
        }
    }

    private fun updateSearchTextState(value: String) {
        _searchTextState.value = value
    }

    private fun bookSelection(id: String) {
        setEffect { BooksContract.Effect.Navigation.ToBookDetails(id) }
    }

    private fun errorNetwork() {
        setEffect { BooksContract.Effect.DataError }
    }

    private fun searchBooks(word: SearchKeyword, paging: Paging) {
        if (word.isEmpty() || paging.page < 0)
            return

        viewModelScope.launch {
            try {
                setState {
                    copy(books = books,
                        isLoading = false,
                        isLoadingMore = true,
                        isSearchOpened = true)
                }

                repository.search(word, paging).collect {
                    page = it.toPaging()

                    setState {
                        copy(books = books.plus(it.items),
                            isLoading = false,
                            isLoadingMore = false,
                            isSearchOpened = true)
                    }
                }
            }
            catch (e: Exception) {
                errorNetwork()
            }
        }
    }

    private fun getNewBookItems() {
        viewModelScope.launch {
            try {
                repository.latestNewBooks().collect {
                    page = it.toPaging()
                    setState {
                        copy(books = it.items, isLoading = false, isSearchOpened = false)
                    }
                    setEffect { BooksContract.Effect.DataWasLoaded }
                }
            }
            catch (e: Exception) {
                errorNetwork()
            }
        }
    }
}
