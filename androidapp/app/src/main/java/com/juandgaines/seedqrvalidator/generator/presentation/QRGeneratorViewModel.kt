package com.juandgaines.seedqrvalidator.generator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.seedqrvalidator.R
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.onError
import com.juandgaines.seedqrvalidator.core.domain.utils.onSuccess
import com.juandgaines.seedqrvalidator.generator.domain.QrGeneratorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class QRGeneratorViewModel @Inject constructor(
    private val qrGeneratorRepositoryModule: QrGeneratorRepository
):ViewModel(){

    private val _eventChannel = Channel<GeneratorEvent>()
    val eventsGenerator = _eventChannel.receiveAsFlow()

    private val _refresh = MutableStateFlow(0)
    private val _missingTime = MutableStateFlow("")
    private var timerJob: kotlinx.coroutines.Job? = null

    private val _generatorState = MutableStateFlow(GeneratorState())
    val generatorState = _refresh
        .onStart {
            _generatorState.update {
                it.copy(
                    isLoading = true
                )
            }
            fetchQrSeed()
        }
        .onEach {
            if (_refresh.value >=0){
                fetchQrSeed()
            }
        }
        .map {
            _generatorState.value
        }
        .combine(
            _missingTime
        ){state, missingTime ->
            state.copy(
                remainingTime = missingTime
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GeneratorState()
        )

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.US,"%02dh:%02dm:%02ds", hours, minutes, remainingSeconds)
    }

    private fun startTimer(expiresAt: String) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val expirationInstant = Instant.parse(expiresAt)
                val currentInstant = Instant.now()
                val remainingSeconds = ChronoUnit.SECONDS.between(currentInstant, expirationInstant)
                

                _missingTime.value= formatTime(remainingSeconds)
                _generatorState.update {
                    it.copy(
                        remainingTime = formatTime(remainingSeconds),
                        hasExpired = remainingSeconds <= 0
                    )
                }
                if (remainingSeconds <= 0) {
                    break
                }
                delay(1000)
            }
        }
    }

    private suspend fun fetchQrSeed() {
        qrGeneratorRepositoryModule.getSeed()
            .onSuccess { seed->
                _generatorState.update {
                    it.copy(
                        seed = seed.seed,
                        isLoading = false,
                        error = null
                    )
                }
                startTimer(seed.expiresAt)
            }
            .onError { error ->
                val errorMessage = when (error) {
                    DataError.Network.NO_INTERNET,
                    DataError.Network.REQUEST_TIMEOUT -> {
                        R.string.error_internet
                    }

                    else -> R.string.message_error
                }

                _generatorState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            }
    }

    fun onAction(intent:GeneratorIntent){
        viewModelScope.launch {
            when(intent){
                GeneratorIntent.NavigateBack -> {
                    _eventChannel.trySend(GeneratorEvent.NavigateBack)
                }
                GeneratorIntent.Retry -> {
                    _generatorState.update { it.copy(isLoading = true) }
                    _refresh.update { it + 1 }
                    fetchQrSeed()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}