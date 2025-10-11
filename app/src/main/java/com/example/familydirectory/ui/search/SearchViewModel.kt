package com.example.familydirectory.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familydirectory.data.model.SearchFilters
import com.example.familydirectory.data.model.SearchResult
import com.example.familydirectory.data.repository.FamilyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = FamilyRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    // ✅ Dynamic filter options from Firebase
    private val _availableParishes = MutableStateFlow<List<String>>(emptyList())
    val availableParishes: StateFlow<List<String>> = _availableParishes.asStateFlow()

    private val _availableRegions = MutableStateFlow<List<String>>(emptyList())
    val availableRegions: StateFlow<List<String>> = _availableRegions.asStateFlow()

    private val _isLoadingFilters = MutableStateFlow(false)
    val isLoadingFilters: StateFlow<Boolean> = _isLoadingFilters.asStateFlow()

    init {
        // Load available filter options when ViewModel is created
        loadFilterOptions()
    }

    /**
     * Load unique parishes and regions from Firebase - IMPROVED
     */
    private fun loadFilterOptions() {
        viewModelScope.launch {
            _isLoadingFilters.value = true
            try {
                // Use the new dedicated functions
                val parishes = repository.getAvailableParishes()
                val regions = repository.getAvailableRegions()

                println("DEBUG: Loaded ${parishes.size} unique parishes")
                println("DEBUG: Parishes: $parishes")
                println("DEBUG: Loaded ${regions.size} unique regions")
                println("DEBUG: Regions: $regions")

                _availableParishes.value = parishes
                _availableRegions.value = regions

            } catch (e: Exception) {
                println("DEBUG: Error loading filter options: ${e.message}")
                _availableParishes.value = emptyList()
                _availableRegions.value = emptyList()
            } finally {
                _isLoadingFilters.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        performSearch()
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _error.value = null
    }

    fun updateFilters(newFilters: SearchFilters) {
        _filters.value = newFilters
        performSearch()
    }

    fun clearFilters() {
        _filters.value = SearchFilters()
        performSearch()
    }

    fun toggleFilterSheet() {
        _showFilterSheet.value = !_showFilterSheet.value
    }

    fun closeFilterSheet() {
        _showFilterSheet.value = false
    }

    /**
     * Refresh filter options (call this after adding new families)
     */
    fun refreshFilterOptions() {
        loadFilterOptions()
    }

    /**
     * Normalize Firebase data - Call this once from Admin screen
     */
    fun normalizeData(onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            val result = repository.normalizeFirebaseData()
            result.onSuccess { count ->
                println("✅ Normalized $count families")
                loadFilterOptions() // Reload filters after normalization
                onComplete(count)
            }.onFailure { e ->
                println("❌ Normalization failed: ${e.message}")
                onComplete(0)
            }
        }
    }

    private fun performSearch() {
        val query = _searchQuery.value
        val currentFilters = _filters.value

        // Debug logging
        println("DEBUG: performSearch called")
        println("DEBUG: query = '$query'")
        println("DEBUG: parish filter = '${currentFilters.parish}'")
        println("DEBUG: region filter = '${currentFilters.region}'")
        println("DEBUG: bloodGroup filter = '${currentFilters.bloodGroup}'")
        println("DEBUG: gender filter = '${currentFilters.gender}'")

        // Don't search if no query and no filters
        if (query.isBlank() && !currentFilters.hasActiveFilters()) {
            _searchResults.value = emptyList()
            _error.value = null
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = repository.searchFamilies(query, currentFilters)

                result.onSuccess { searchResults ->
                    println("DEBUG: Found ${searchResults.size} results")
                    _searchResults.value = searchResults

                    if (searchResults.isEmpty() && (query.isNotBlank() || currentFilters.hasActiveFilters())) {
                        _error.value = "കുടുംബങ്ങളൊന്നും കണ്ടെത്തിയില്ല"
                    }
                }.onFailure { exception ->
                    println("DEBUG: Search failed: ${exception.message}")
                    _error.value = exception.message ?: "തിരയൽ പരാജയപ്പെട്ടു"
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                println("DEBUG: Exception in search: ${e.message}")
                _error.value = e.message ?: "തിരയൽ പരാജയപ്പെട്ടു"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Extension function for SearchFilters
private fun SearchFilters.hasActiveFilters(): Boolean {
    return parish != null || region != null || bloodGroup != null || gender != null
}