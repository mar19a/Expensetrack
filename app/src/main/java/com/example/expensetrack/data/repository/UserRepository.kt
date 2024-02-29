package com.example.expensetrack.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.example.expensetrack.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val userString = "user"
val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = userString)
private val USER = stringPreferencesKey(userString)


class UserRepository(val context: Context) {
    suspend fun save(data: User) {
        context.userStore.edit { preferences ->
            preferences[USER] = Gson().toJson(data)
        }
    }

    fun get(): Flow<User?> {
        return context.userStore.data.map { preferences ->
            Gson().fromJson(preferences[USER], User::class.java)
        }
    }

    suspend fun clear() {
        context.userStore.edit { preferences -> preferences.clear() }
    }
}
