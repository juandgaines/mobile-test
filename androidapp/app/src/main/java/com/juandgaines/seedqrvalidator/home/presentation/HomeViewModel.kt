package com.juandgaines.seedqrvalidator.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel :ViewModel(){

    private val _eventChannel = Channel<HomeEvent>()
    val eventsHome = _eventChannel.receiveAsFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState:StateFlow<HomeState> = _homeState
        .onStart {

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeState()
        )

    fun onIntent(intent: HomeIntent){
        when(intent){
            HomeIntent.GenerateQrIntent -> {
                _eventChannel.trySend(HomeEvent.NavigateToQr)
            }
            HomeIntent.ScanQrIntent -> {
                _eventChannel.trySend(HomeEvent.NavigateToScan)
            }

            HomeIntent.CollapseFabMenu -> {
                _homeState.update {
                    it.copy(
                        isMenuVisible = false
                    )
                }
            }
            HomeIntent.ExpandFabMenu -> {
                _homeState.update {
                    it.copy(
                        isMenuVisible = true
                    )
                }
            }
        }
    }
}