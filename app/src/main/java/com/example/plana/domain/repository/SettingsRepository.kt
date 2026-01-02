package com.example.plana.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val currency: Flow<String>
    val hideAmounts: Flow<Boolean>
    suspend fun setCurrency(code: String)
    suspend fun setHideAmounts(hide: Boolean)
}
