package com.example.familydirectory.ui.events

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

    private val _deleteStatus = MutableStateFlow<DeleteStatus>(DeleteStatus.Idle)
    val deleteStatus: StateFlow<DeleteStatus> = _deleteStatus.asStateFlow()

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

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            _deleteStatus.value = DeleteStatus.Deleting
            val result = repository.deleteEvent(eventId)

            result.onSuccess {
                _deleteStatus.value = DeleteStatus.Success
                // Remove from local list immediately
                _events.value = _events.value.filter { it.id != eventId }
            }.onFailure { error ->
                _deleteStatus.value = DeleteStatus.Error(error.message ?: "Failed to delete event")
            }
        }
    }

    fun resetDeleteStatus() {
        _deleteStatus.value = DeleteStatus.Idle
    }
}

sealed class DeleteStatus {
    object Idle : DeleteStatus()
    object Deleting : DeleteStatus()
    object Success : DeleteStatus()
    data class Error(val message: String) : DeleteStatus()
}