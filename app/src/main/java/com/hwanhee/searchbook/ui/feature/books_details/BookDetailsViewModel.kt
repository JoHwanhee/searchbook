package com.hwanhee.searchbook.ui.feature.books_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hwanhee.searchbook.NavigationKeys
import com.hwanhee.searchbook.base.BaseViewModel
import com.hwanhee.searchbook.model.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: BookRepository
) : BaseViewModel<
        BookDetailsContract.Event,
        BookDetailsContract.State,
        BookDetailsContract.Effect>() {

    init {
        viewModelScope.launch {
            stateHandle.get<String>(NavigationKeys.Arg.BOOK_ISBN13)?.let {
                repository.getBookByIsbn13(it)
                    .catch { errorNetwork() }
                    .collect {
                        setState { copy(bookItemDetail = it) }
                    }
            }
        }
    }

    private fun errorNetwork() {
        setEffect { BookDetailsContract.Effect.DataError }
    }
    
    override fun setInitialState() = BookDetailsContract.State(null)

    override fun handleEvents(event: BookDetailsContract.Event) {}
}
