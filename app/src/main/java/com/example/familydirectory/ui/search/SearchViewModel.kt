package com.example.familydirectory.ui.search

// SearchViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familydirectory.data.model.SearchFilters
import com.example.familydirectory.data.model.SearchResult
import com.example.familydirectory.data.repository.FamilyRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val repository = FamilyRepository()

    // Search query text
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Search results
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Filters
    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters.asStateFlow()

    // Filter options from Firestore
    private val _availableParishes = MutableStateFlow<List<String>>(emptyList())
    val availableParishes: StateFlow<List<String>> = _availableParishes.asStateFlow()

    private val _availableRegions = MutableStateFlow<List<String>>(emptyList())
    val availableRegions: StateFlow<List<String>> = _availableRegions.asStateFlow()

    // Show filter sheet
    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    init {
        // Load filter options
        loadFilterOptions()

        // Debounced search - wait 300ms after user stops typing
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        performSearch(query, filters.value)
                    } else {
                        _searchResults.value = emptyList()
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _error.value = null
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _error.value = null
    }

    fun updateFilters(newFilters: SearchFilters) {
        _filters.value = newFilters
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value, newFilters)
        }
    }

    fun clearFilters() {
        _filters.value = SearchFilters()
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value, SearchFilters())
        }
    }

    fun toggleFilterSheet() {
        _showFilterSheet.value = !_showFilterSheet.value
    }

    fun closeFilterSheet() {
        _showFilterSheet.value = false
    }

    private fun performSearch(query: String, filters: SearchFilters) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val result = repository.searchFamilies(query, filters)

                result.onSuccess { results ->
                    _searchResults.value = results
                    if (results.isEmpty()) {
                        _error.value = "No results found for '$query'"
                    }
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Search failed"
                    _searchResults.value = emptyList()
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadFilterOptions() {
        viewModelScope.launch {
            try {
                val parishes = repository.getAllParishes()
                _availableParishes.value = parishes

                val regions = repository.getAllRegions()
                _availableRegions.value = regions
            } catch (e: Exception) {
                // Silently fail - use default filter options
            }
        }
    }

    fun searchByFiltersOnly() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val result = repository.searchByFilters(filters.value)

                result.onSuccess { families ->
                    // Convert to SearchResult format
                    _searchResults.value = families.map { family ->
                        SearchResult(
                            family = family,
                            matchedIn = "Filter Match",
                            relevanceScore = 50
                        )
                    }
                    if (families.isEmpty()) {
                        _error.value = "No families found matching the filters"
                    }
                }.onFailure { exception ->
                    _error.value = exception.message ?: "Search failed"
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}