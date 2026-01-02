package com.example.plana.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("settings") }
    )

    val currency: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_CURRENCY] ?: "USD"
    }

    val hideAmounts: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_HIDE_AMOUNTS] ?: false
    }

    suspend fun setCurrency(code: String) {
        dataStore.edit { prefs -> prefs[KEY_CURRENCY] = code }
    }

    suspend fun setHideAmounts(hide: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_HIDE_AMOUNTS] = hide }
    }

    private companion object {
        val KEY_CURRENCY = stringPreferencesKey("currency")
        val KEY_HIDE_AMOUNTS = booleanPreferencesKey("hide_amounts")
    }
}
