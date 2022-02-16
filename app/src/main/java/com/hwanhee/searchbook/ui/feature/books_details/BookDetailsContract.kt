package com.hwanhee.searchbook.ui.feature.books_details

import com.hwanhee.searchbook.base.ViewEvent
import com.hwanhee.searchbook.base.ViewSideEffect
import com.hwanhee.searchbook.base.ViewState
import com.hwanhee.searchbook.model.ui.BookItemDetail


class BookDetailsContract {
    sealed class Event : ViewEvent

    data class State(
        val bookItemDetail: BookItemDetail?
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object DataError : BookDetailsContract.Effect()
    }
}