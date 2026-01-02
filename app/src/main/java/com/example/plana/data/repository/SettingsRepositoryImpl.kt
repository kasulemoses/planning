package com.example.plana.data.repository

import com.example.plana.data.preferences.SettingsDataStore
import com.example.plana.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override val currency: Flow<String> = settingsDataStore.currency
    override val hideAmounts: Flow<Boolean> = settingsDataStore.hideAmounts

    override suspend fun setCurrency(code: String) {
        settingsDataStore.setCurrency(code)
    }

    override suspend fun setHideAmounts(hide: Boolean) {
        settingsDataStore.setHideAmounts(hide)
    }
}
