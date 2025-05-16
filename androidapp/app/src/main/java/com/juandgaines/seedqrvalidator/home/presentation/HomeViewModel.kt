package com.juandgaines.seedqrvalidator.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.seedqrvalidator.home.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) :ViewModel(){

    private val _eventChannel = Channel<HomeEvent>()
    val eventsHome = _eventChannel.receiveAsFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState:StateFlow<HomeState> = homeRepository
        .getScannedSeeds()
        .onStart {

        }.map { listSeeds->
            _homeState.update {
                it.copy(
                    seedList = listSeeds
                )
            }
            _homeState.value
        }.combine(_homeState){ previousHS, homeState ->
            homeState.copy(
                seedList = previousHS.seedList
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeState()
        )

    fun onIntent(intent: HomeIntent){
        viewModelScope.launch {
            when(intent){
                HomeIntent.GenerateQrIntent -> {
                    _homeState.update {
                        it.copy(
                            isMenuVisible = false
                        )
                    }
                    _eventChannel.trySend(HomeEvent.NavigateToQr(null))
                }
                is HomeIntent.OpenExistingQrIntent -> {
                    _homeState.update {
                        it.copy(
                            isMenuVisible = false
                        )
                    }
                    _eventChannel.trySend(HomeEvent.NavigateToQr(seed = intent.seed))
                }
                HomeIntent.ScanQrIntent -> {
                    _homeState.update {
                        it.copy(
                            isMenuVisible = false
                        )
                    }
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
}