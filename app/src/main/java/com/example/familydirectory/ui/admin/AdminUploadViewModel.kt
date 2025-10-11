package com.example.familydirectory.ui.admin

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.repository.FamilyRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ✅ Renamed to avoid conflict with Cloudinary UploadStatus
sealed class AdminUploadState {
    object Idle : AdminUploadState()
    data class Uploading(val current: Int, val total: Int) : AdminUploadState()
    data class Success(val count: Int) : AdminUploadState()
    data class Error(val message: String) : AdminUploadState()
}

class AdminUploadViewModel : ViewModel() {
    private val repository = FamilyRepository()
    private val gson = Gson()

    private val _uploadStatus = MutableStateFlow<AdminUploadState>(AdminUploadState.Idle)
    val uploadStatus: StateFlow<AdminUploadState> = _uploadStatus.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun resetStatus() {
        _uploadStatus.value = AdminUploadState.Idle
    }

    /**
     * Upload family from form data
     */
    fun uploadFamilyFromForm(
        familyHead: String,
        place: String,
        postOffice: String,
        region: String,
        parish: String,
        phone: String,
        email: String,
        job: String,
        education: String,
        bloodGroup: String,
        dob: String,
        gender: String,
        otherInfo: String
    ) {
        viewModelScope.launch {
            try {
                _uploadStatus.value = AdminUploadState.Uploading(1, 1)

                val family = Family(
                    familyHead = familyHead,
                    place = place,
                    postOffice = postOffice,
                    region = region,
                    parish = parish,
                    phone = phone,
                    email = email,
                    job = job,
                    education = education,
                    bloodGroup = bloodGroup,
                    dob = dob,
                    gender = gender,
                    familyMembers = emptyList(),
                    otherInfo = otherInfo,
                    searchTerms = generateSearchTerms(familyHead, place, phone, parish, region)
                )

                val result = repository.addFamily(family)

                _uploadStatus.value = if (result.isSuccess) {
                    AdminUploadState.Success(1)
                } else {
                    AdminUploadState.Error(result.exceptionOrNull()?.message ?: "Upload failed")
                }
            } catch (e: Exception) {
                _uploadStatus.value = AdminUploadState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Upload family/families from JSON
     * Supports both single object and array of objects
     */
    fun uploadFamilyFromJson(jsonString: String) {
        viewModelScope.launch {
            try {
                val trimmedJson = jsonString.trim()

                // Check if it's an array or single object
                if (trimmedJson.startsWith("[")) {
                    // Multiple families (array)
                    uploadMultipleFamilies(trimmedJson)
                } else if (trimmedJson.startsWith("{")) {
                    // Single family (object)
                    uploadSingleFamily(trimmedJson)
                } else {
                    _uploadStatus.value = AdminUploadState.Error("Invalid JSON format. Must start with [ or {")
                }
            } catch (e: JsonSyntaxException) {
                _uploadStatus.value = AdminUploadState.Error("Invalid JSON format: ${e.message}")
            } catch (e: Exception) {
                _uploadStatus.value = AdminUploadState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun uploadSingleFamily(jsonString: String) {
        try {
            _uploadStatus.value = AdminUploadState.Uploading(1, 1)

            val family = gson.fromJson(jsonString, Family::class.java)
            val familyWithSearchTerms = family.copy(
                searchTerms = generateSearchTerms(
                    family.familyHead,
                    family.place,
                    family.phone,
                    family.parish,
                    family.region
                )
            )

            val result = repository.addFamily(familyWithSearchTerms)

            _uploadStatus.value = if (result.isSuccess) {
                AdminUploadState.Success(1)
            } else {
                AdminUploadState.Error(result.exceptionOrNull()?.message ?: "Upload failed")
            }
        } catch (e: Exception) {
            _uploadStatus.value = AdminUploadState.Error("Error parsing JSON: ${e.message}")
        }
    }

    private suspend fun uploadMultipleFamilies(jsonString: String) {
        try {
            val listType = object : TypeToken<List<Family>>() {}.type
            val families: List<Family> = gson.fromJson(jsonString, listType)

            if (families.isEmpty()) {
                _uploadStatus.value = AdminUploadState.Error("No families found in JSON")
                return
            }

            val total = families.size
            var successCount = 0
            var errorCount = 0
            val errors = mutableListOf<String>()

            families.forEachIndexed { index, family ->
                _uploadStatus.value = AdminUploadState.Uploading(index + 1, total)

                try {
                    val familyWithSearchTerms = family.copy(
                        searchTerms = generateSearchTerms(
                            family.familyHead,
                            family.place,
                            family.phone,
                            family.parish,
                            family.region
                        )
                    )

                    val result = repository.addFamily(familyWithSearchTerms)

                    if (result.isSuccess) {
                        successCount++
                    } else {
                        errorCount++
                        errors.add("${family.familyHead}: ${result.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    errorCount++
                    errors.add("${family.familyHead}: ${e.message}")
                }

                // Small delay to avoid overwhelming Firestore
                kotlinx.coroutines.delay(100)
            }

            _uploadStatus.value = if (errorCount == 0) {
                AdminUploadState.Success(successCount)
            } else if (successCount > 0) {
                AdminUploadState.Error("Uploaded $successCount, Failed $errorCount. Errors: ${errors.take(3).joinToString(", ")}")
            } else {
                AdminUploadState.Error("All uploads failed. First error: ${errors.firstOrNull()}")
            }
        } catch (e: Exception) {
            _uploadStatus.value = AdminUploadState.Error("Error parsing JSON array: ${e.message}")
        }
    }

    private fun generateSearchTerms(
        familyHead: String,
        place: String,
        phone: String,
        parish: String,
        region: String
    ): List<String> {
        return buildList {
            // Add family head name terms
            addAll(familyHead.lowercase().split(" "))

            // Add place
            add(place.lowercase())

            // Add phone number (full and partial)
            add(phone)
            if (phone.length >= 6) {
                add(phone.takeLast(6))
            }

            // Add parish
            addAll(parish.lowercase().split(" "))

            // Add region
            add(region.lowercase())
        }.filter { it.isNotBlank() }.distinct()
    }
}