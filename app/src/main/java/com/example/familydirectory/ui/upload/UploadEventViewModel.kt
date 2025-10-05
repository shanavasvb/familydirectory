package com.example.familydirectory.ui.upload

// UploadEventViewModel.kt

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familydirectory.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UploadEventViewModel : ViewModel() {

    private val repository = EventRepository()

    private val _uiState = MutableStateFlow<UploadUiState>(UploadUiState.Idle)
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> = _selectedImages.asStateFlow()

    fun addImages(uris: List<Uri>) {
        val currentImages = _selectedImages.value.toMutableList()
        currentImages.addAll(uris)
        _selectedImages.value = currentImages
    }

    fun removeImage(uri: Uri) {
        _selectedImages.value = _selectedImages.value.filter { it != uri }
    }

    fun clearImages() {
        _selectedImages.value = emptyList()
    }

    fun uploadEvent(
        title: String,
        description: String,
        uploadedBy: String = "current_user_id" // Get from Firebase Auth
    ) {
        if (title.isBlank()) {
            _uiState.value = UploadUiState.Error("Title cannot be empty")
            return
        }

        if (_selectedImages.value.isEmpty()) {
            _uiState.value = UploadUiState.Error("Please select at least one image")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = UploadUiState.Uploading(0)

                val result = repository.uploadEvent(
                    title = title,
                    description = description,
                    imageUris = _selectedImages.value,
                    uploadedBy = uploadedBy
                )

                result.onSuccess { eventId ->
                    _uiState.value = UploadUiState.Success(eventId)
                    clearImages()
                }.onFailure { error ->
                    _uiState.value = UploadUiState.Error(
                        error.message ?: "Upload failed"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = UploadUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = UploadUiState.Idle
    }
}

sealed class UploadUiState {
    object Idle : UploadUiState()
    data class Uploading(val progress: Int) : UploadUiState()
    data class Success(val eventId: String) : UploadUiState()
    data class Error(val message: String) : UploadUiState()
}