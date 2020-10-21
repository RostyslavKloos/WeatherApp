package com.example.weatherapp.utils

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SharedManager(context: Context) {
    private val dataStore = context.createDataStore(name = "cityStore")

    companion object {
        val CITY_NAME_KEY = preferencesKey<String>("CITY_NAME")
    }

    suspend fun storeCity (city: String) {
        dataStore.edit {
            it[CITY_NAME_KEY] = city
        }
    }

    val cityNameFlow: Flow<String> = dataStore.data.map {
        it[CITY_NAME_KEY] ?: ""
    }
}