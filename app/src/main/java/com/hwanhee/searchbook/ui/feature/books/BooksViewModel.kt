package com.hwanhee.searchbook.ui.feature.books

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hwanhee.searchbook.base.BaseViewModel
import com.hwanhee.searchbook.base.logger.Logger
import com.hwanhee.searchbook.base.Paging
import com.hwanhee.searchbook.base.SearchKeyword
import com.hwanhee.searchbook.model.BookRepository
import com.hwanhee.searchbook.model.toPaging
import com.hwanhee.searchbook.model.ui.BooksItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BookRepository
) : BaseViewModel<
        BooksContract.Event,
        BooksContract.State,
        BooksContract.Effect>() {
    private var pageCache: Paging
    private var keywordCache = SearchKeyword("")

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    init {
        pageCache = Paging()
        getNewBookItems()
    }

    override fun handleEvents(event: BooksContract.Event) {
        when (event) {
            is BooksContract.Event.BookSelection -> {
                bookSelectedEffect(event.id)
            }

            is BooksContract.Event.ScrollMeetsBottom -> {
                if (increaseIfNeedMore()){
                    searchBooks(keywordCache, pageCache)
                }
            }

            is BooksContract.Event.SearchOn -> {
                pageCache.init()
                setSearchState()
            }

            is BooksContract.Event.SearchOff -> {
                initSearchWord()
                getNewBookItems()
            }

            is BooksContract.Event.UpdateSearchText -> {
                updateSearchTextState(event.value)
            }

            is BooksContract.Event.Search -> {
                pageCache.init()
                setSearchOpenedState()
                searchBooks(event.searchWord, pageCache)
                keywordCache = event.searchWord
            }
        }
    }

    private fun searchBooks(word: SearchKeyword, paging: Paging) {
        if (word.isEmpty() || paging.page < 0)
            return

        viewModelScope.launch {
            copySearchStartsState()
            repository.searchBooks(word, paging)
                .catch {
                    e-> run {
                        Logger.e(e)
                        errorEffect(e)
                    }
                }
                .collect {
                    pageCache = it.toPaging()
                    copySearchEndedState(it)
                }
        }
    }

    private fun getNewBookItems() {
        viewModelScope.launch {
            repository.getNewBooks()
                .catch {
                    e-> run {
                        Logger.e(e)
                        errorEffect(e)
                    }
                }
                .collect {
                    pageCache = it.toPaging()
                    setState { copy(books = it.items, isLoading = false, isSearchOpened = false) }
                    setEffect { BooksContract.Effect.DataWasLoaded }
                }
        }
    }

    override fun setInitialState() =
        BooksContract.State(books = listOf(), isLoading = true, isSearchOpened = false)

    private fun setSearchOpenedState() =
        setState {
            BooksContract.State(
                books = emptyList(),
                isLoading = false,
                isLoadingMore = false,
                isSearchOpened = true)
        }

    private fun setSearchState() =
        setState {
            BooksContract.State(
                books = books,
                isLoading = false,
                isSearchOpened = true)
        }

    private fun copySearchStartsState() =
        setState {
            copy(isLoading = pageCache.page == 1,
                 isLoadingMore = pageCache.page != 1)
        }

    private fun copySearchEndedState(newItem: BooksItem) =
        setState {
            copy(books = books + newItem.items,
                 isLoading = false,
                 isLoadingMore = false)
        }

    private fun bookSelectedEffect(id: String) =
        setEffect {
            BooksContract.Effect.Navigation.ToBookDetails(id)
        }

    private fun errorEffect(e: Throwable) =
        setEffect {
            BooksContract.Effect.DataError(e)
        }

    private fun initSearchWord() {
        _searchTextState.value = ""
        keywordCache = SearchKeyword("")
    }

    private fun increaseIfNeedMore() : Boolean {
        var searchMode = false
        currentState {
            searchMode = isSearchOpened
        }
        return searchMode && pageCache.increaseIfNeedMore()
    }

    private fun updateSearchTextState(value: String) {
        _searchTextState.value = value
    }
}
