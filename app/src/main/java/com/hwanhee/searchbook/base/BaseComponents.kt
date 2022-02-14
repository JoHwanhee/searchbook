package com.hwanhee.searchbook.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwanhee.searchbook.ui.feature.books.BooksContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val LAUNCH_LISTEN_FOR_EFFECTS = "LAUNCH_LISTEN_FOR_EFFECTS"
const val LAUNCH_LISTEN_FOR_EFFECTS_DETAIL_VIEW = "LAUNCH_LISTEN_FOR_EFFECTS_DETAIL_VIEW"

interface ViewState

interface ViewEvent

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> :
ViewModel() {
    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState: State<UiState> = _viewState

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    protected fun currentState(reducer: UiState.() -> Unit) {
        viewState.value.reducer()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    abstract fun handleEvents(event: Event)

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}