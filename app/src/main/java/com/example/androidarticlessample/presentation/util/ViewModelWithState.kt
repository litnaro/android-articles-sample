package com.example.androidarticlessample.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.minus
import kotlin.collections.plus

interface ViewModelWithState<UiState, UiStateEvent> {

    val state: StateFlow<UiStateHolder<UiState, UiStateEvent>>

    fun updateState(block: UiStateHolder<UiState, UiStateEvent>.() -> UiStateHolder<UiState, UiStateEvent>)

    fun onEventProcessed(event: UiStateEvent)
}

class BaseViewModelWithState<UiState, UiStateEvent>(
    initialState: UiStateHolder<UiState, UiStateEvent>
) : ViewModelWithState<UiState, UiStateEvent> {
    private val mutableState = MutableStateFlow(initialState)
    override val state = mutableState.asStateFlow()

    override fun onEventProcessed(event: UiStateEvent) {
        mutableState.update {
            it.copy(viewEvents = mutableState.value.viewEvents - event)
        }
    }

    override fun updateState(block: UiStateHolder<UiState, UiStateEvent>.() -> UiStateHolder<UiState, UiStateEvent>) {
        mutableState.update {
            it.block()
        }
    }
}

data class UiStateHolder<UiState, UiStateEvent>(
    val viewState: UiState,
    val viewEvents: List<UiStateEvent>
)

infix fun <UiState, UiStateEvent> UiStateHolder<UiState, UiStateEvent>.with(viewState: UiState): UiStateHolder<UiState, UiStateEvent> {
    return copy(viewState = viewState)
}

infix fun <UiState, UiStateEvent> UiStateHolder<UiState, UiStateEvent>.add(viewEvent: UiStateEvent): UiStateHolder<UiState, UiStateEvent> {
    return copy(viewEvents = viewEvents + viewEvent)
}

inline fun <reified UiState, reified UiStateEvent> UiState.asInitialState(): UiStateHolder<UiState, UiStateEvent> {
    return UiStateHolder(this, emptyList())
}
