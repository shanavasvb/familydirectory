package com.example.familydirectory.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey  // ✅ FIXED
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_prefs")

class SearchSuggestionsManager(private val context: Context) {

    private val RECENT_SEARCHES_KEY = stringSetPreferencesKey("recent_searches")  // ✅ FIXED
    private val MAX_RECENT_SEARCHES = 10

    fun getRecentSearches(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[RECENT_SEARCHES_KEY]?.toList() ?: emptyList()
        }
    }

    suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        context.dataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: mutableSetOf()
            currentSearches.remove(query)
            val newSearches = mutableListOf(query)
            newSearches.addAll(currentSearches)
            preferences[RECENT_SEARCHES_KEY] = newSearches.take(MAX_RECENT_SEARCHES).toSet()
        }
    }

    suspend fun clearRecentSearches() {
        context.dataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCHES_KEY)
        }
    }

    suspend fun removeRecentSearch(query: String) {
        context.dataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: return@edit
            currentSearches.remove(query)
            preferences[RECENT_SEARCHES_KEY] = currentSearches
        }
    }
}