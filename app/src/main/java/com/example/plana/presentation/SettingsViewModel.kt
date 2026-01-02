package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plana.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val currency = settingsRepository.currency.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        "USD"
    )

    val hideAmounts = settingsRepository.hideAmounts.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )

    fun setCurrency(code: String) {
        viewModelScope.launch {
            settingsRepository.setCurrency(code)
        }
    }

    fun toggleHideAmounts() {
        viewModelScope.launch {
            settingsRepository.setHideAmounts(!hideAmounts.value)
        }
    }
}
