package com.hwanhee.searchbook.ui.feature.books

import com.hwanhee.searchbook.base.SearchKeyword
import com.hwanhee.searchbook.base.ViewEvent
import com.hwanhee.searchbook.base.ViewSideEffect
import com.hwanhee.searchbook.base.ViewState
import com.hwanhee.searchbook.model.ui.BookItem

class BooksContract {
    sealed class Event : ViewEvent {
        data class BookSelection(val id: String) : Event()
        object SearchOn : Event()
        object SearchOff : Event()
        object ScrollMeetsBottom : Event()
        data class Search(val searchWord: SearchKeyword) : Event()
        data class UpdateSearchText(val value: String) : Event()
    }

    data class State(val books: List<BookItem> = listOf(),
                     val isSearchOpened: Boolean = false,
                     val isLoadingMore: Boolean = false,
                     val isLoading: Boolean = false) : ViewState

    sealed class Effect : ViewSideEffect {
        object DataWasLoaded : Effect()
        data class DataError(val e:Throwable) : Effect()

        sealed class Navigation : Effect() {
            data class ToBookDetails(val isbn13: String) : Navigation()
        }
    }
}