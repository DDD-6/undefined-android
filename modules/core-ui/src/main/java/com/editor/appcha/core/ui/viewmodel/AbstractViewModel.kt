package com.editor.appcha.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.editor.appcha.core.ui.event.EventFlow
import com.editor.appcha.core.ui.event.MutableEventFlow
import com.editor.appcha.core.ui.event.ViewEvent
import com.editor.appcha.core.ui.event.asEventFlow
import com.editor.appcha.core.ui.state.LoadState
import com.editor.appcha.core.ui.state.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class AbstractViewModel<VE : ViewEvent, VS : ViewState>(
    initialState: VS
) : ViewModel() {

    @Suppress("unused", "PropertyName")
    protected inline val TAG: String
        get() = this::class.java.simpleName

    private val _state: MutableStateFlow<VS> = MutableStateFlow(initialState)
    val state: StateFlow<VS> = _state.asStateFlow()

    private val _loadState: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.NOT_LOADING)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()

    private val _event: MutableEventFlow<VE> = MutableEventFlow()
    val event: EventFlow<VE> = _event.asEventFlow()

    protected fun updateState(function: (state: VS) -> VS) = _state.update(function)

    protected fun event(event: VE): Job = viewModelScope.launch { _event.emit(event) }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.LAZY,
        onError: (Throwable) -> Unit = ::onError,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _loadState.value = LoadState.ERROR
            onError(throwable)
        }
        viewModelScope.launch(
            context = context + exceptionHandler,
            start = start
        ) {
            _loadState.value = LoadState.LOADING
            block()
            _loadState.value = LoadState.NOT_LOADING
        }
    }

    protected open fun onError(throwable: Throwable) = Unit
}