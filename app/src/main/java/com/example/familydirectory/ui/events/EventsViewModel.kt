package com.example.familydirectory.ui.events

//EventsViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familydirectory.data.repository.Event
import com.example.familydirectory.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventsViewModel : ViewModel() {

    private val repository = EventRepository()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getEvents(limit = 50).collect { eventsList ->
                _events.value = eventsList
                _isLoading.value = false
            }
        }
    }

    fun toggleLike(eventId: String) {
        // TODO: Implement like functionality with Firestore
        // Update likes count in Firestore
    }
}