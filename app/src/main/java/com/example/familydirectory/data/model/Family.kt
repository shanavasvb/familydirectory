package com.example.familydirectory.data.model

// Family.kt - Data models for family directory


import com.google.firebase.Timestamp
import java.util.Date

data class Family(
    val id: String = "",
    val familyHead: String = "",
    val place: String = "",
    val postOffice: String = "",
    val region: String = "",
    val parish: String = "",
    val phone: String = "",
    val email: String = "",
    val job: String = "",
    val education: String = "",
    val bloodGroup: String = "",
    val dob: String = "",
    val gender: String = "",
    val otherInfo: String = "",
    val familyMembers: List<FamilyMember> = emptyList(),
    val searchTerms: List<String> = emptyList(), // For efficient searching
    val createdAt: Date = Date()
)

data class FamilyMember(
    val id: String = "",
    val name: String = "",
    val relation: String = "",
    val fatherName: String = "",
    val motherName: String = "",
    val spouseName: String = "",
    val dob: String = "",
    val education: String = "",
    val job: String = "",
    val institution: String = "",
    val phone: String = "",
    val email: String = "",
    val bloodGroup: String = "",
    val gender: String = ""
)

// Search Filter Options
data class SearchFilters(
    val parish: String? = null,
    val region: String? = null,
    val bloodGroup: String? = null,
    val gender: String? = null
)

// Search Result with highlighted terms
data class SearchResult(
    val family: Family,
    val matchedIn: String, // "Family Head", "Member", "Place", etc.
    val matchedMember: FamilyMember? = null,
    val relevanceScore: Int = 0
)

// Common filter values
object FilterOptions {
    val parishes = listOf(
        "St.Mary's Jacobite Cheengeri",
        "St.George Orthodox Church",
        "Mar Thoma Church",
        // Add more parishes
    )

    val regions = listOf(
        "Kumbleri",
        "Meenangadi",
        "Meppadi",
        // Add more regions
    )

    val bloodGroups = listOf(
        "A positive (A+)",
        "A negative (A-)",
        "B positive (B+)",
        "B negative (B-)",
        "O positive (O+)",
        "O negative (O-)",
        "AB positive (AB+)",
        "AB negative (AB-)"
    )

    val genders = listOf("Male", "Female")

    val relations = listOf(
        "Wife",
        "Husband",
        "Son",
        "Daughter",
        "Father",
        "Mother",
        "Brother",
        "Sister",
        "Grandfather",
        "Grandmother",
        "Grandson",
        "Granddaughter"
    )
}