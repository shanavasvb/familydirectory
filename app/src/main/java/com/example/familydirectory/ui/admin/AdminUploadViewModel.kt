package com.example.familydirectory.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.FamilyMember
import com.example.familydirectory.data.repository.FamilyRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminUploadViewModel : ViewModel() {

    private val repository = FamilyRepository()
    private val gson = Gson()

    private val _uiState = MutableStateFlow<AdminUploadUiState>(AdminUploadUiState.Idle)
    val uiState: StateFlow<AdminUploadUiState> = _uiState.asStateFlow()

    fun uploadFromJson(jsonText: String) {
        viewModelScope.launch {
            try {
                _uiState.value = AdminUploadUiState.Uploading

                // Parse JSON to Family object
                val family = gson.fromJson(jsonText, Family::class.java)

                // Validate required fields
                if (family.familyHead.isBlank()) {
                    _uiState.value = AdminUploadUiState.Error("Family head name is required")
                    return@launch
                }

                if (family.place.isBlank()) {
                    _uiState.value = AdminUploadUiState.Error("Place is required")
                    return@launch
                }

                // Generate search terms
                val familyWithSearchTerms = family.copy(
                    searchTerms = generateSearchTerms(family)
                )

                // Upload to Firestore
                val result = repository.addFamily(familyWithSearchTerms)

                result.onSuccess { familyId ->
                    _uiState.value = AdminUploadUiState.Success(familyId)
                }.onFailure { error ->
                    _uiState.value = AdminUploadUiState.Error(
                        error.message ?: "Upload failed"
                    )
                }

            } catch (e: JsonSyntaxException) {
                _uiState.value = AdminUploadUiState.Error(
                    "Invalid JSON format: ${e.message}"
                )
            } catch (e: Exception) {
                _uiState.value = AdminUploadUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun uploadFromForm(
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
                _uiState.value = AdminUploadUiState.Uploading

                val family = Family(
                    familyHead = familyHead.trim(),
                    place = place.trim(),
                    postOffice = postOffice.trim(),
                    region = region.trim(),
                    parish = parish.trim(),
                    phone = phone.trim(),
                    email = email.trim(),
                    job = job.trim(),
                    education = education.trim(),
                    bloodGroup = bloodGroup.trim(),
                    dob = dob.trim(),
                    gender = gender.trim(),
                    otherInfo = otherInfo.trim(),
                    familyMembers = emptyList(),
                    searchTerms = emptyList()
                )

                // Generate search terms
                val familyWithSearchTerms = family.copy(
                    searchTerms = generateSearchTerms(family)
                )

                // Upload to Firestore
                val result = repository.addFamily(familyWithSearchTerms)

                result.onSuccess { familyId ->
                    _uiState.value = AdminUploadUiState.Success(familyId)
                }.onFailure { error ->
                    _uiState.value = AdminUploadUiState.Error(
                        error.message ?: "Upload failed"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = AdminUploadUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun generateSearchTerms(family: Family): List<String> {
        val terms = mutableSetOf<String>()

        // Family head
        if (family.familyHead.isNotEmpty()) {
            terms.add(family.familyHead.lowercase())
            family.familyHead.lowercase().split(" ").forEach { word ->
                if (word.length > 1) terms.add(word)
            }
        }

        // Location
        if (family.place.isNotEmpty()) terms.add(family.place.lowercase())
        if (family.region.isNotEmpty()) terms.add(family.region.lowercase())
        if (family.postOffice.isNotEmpty()) terms.add(family.postOffice.lowercase())

        // Parish
        if (family.parish.isNotEmpty()) {
            terms.add(family.parish.lowercase())
            family.parish.lowercase().split(" ").forEach { word ->
                if (word.length > 2) terms.add(word)
            }
        }

        // Phone
        if (family.phone.isNotEmpty()) {
            val cleanPhone = family.phone.replace(Regex("[^0-9]"), "")
            terms.add(cleanPhone)
            if (cleanPhone.length >= 4) {
                terms.add(cleanPhone.takeLast(4))
            }
        }

        // Email
        if (family.email.isNotEmpty()) {
            terms.add(family.email.lowercase())
        }

        // Family members
        family.familyMembers.forEach { member ->
            if (member.name.isNotEmpty()) {
                terms.add(member.name.lowercase())
                member.name.lowercase().split(" ").forEach { word ->
                    if (word.length > 1) terms.add(word)
                }
            }
            if (member.phone.isNotEmpty()) {
                val cleanPhone = member.phone.replace(Regex("[^0-9]"), "")
                terms.add(cleanPhone)
                if (cleanPhone.length >= 4) {
                    terms.add(cleanPhone.takeLast(4))
                }
            }
            if (member.email.isNotEmpty()) {
                terms.add(member.email.lowercase())
            }
        }

        return terms.toList()
    }

    fun resetState() {
        _uiState.value = AdminUploadUiState.Idle
    }
}

sealed class AdminUploadUiState {
    object Idle : AdminUploadUiState()
    object Uploading : AdminUploadUiState()
    data class Success(val familyId: String) : AdminUploadUiState()
    data class Error(val message: String) : AdminUploadUiState()
}