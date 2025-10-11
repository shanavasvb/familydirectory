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


object FilterOptions {
    val parishes = listOf(
        "St. Mary's Orthodox Church, Kuruppampady",
        "St. Mary's Jacobite Church, Cheengeri",
        "St. Thomas Malankara Catholic Church, Kolagappara",
        "St. Mary's Malankara Catholic Church, Kallichal",
        "Saron Fellowship Church, Pulluvazhy",
        "Mar Gregorious Church, Iringole"
    ).sorted()

    val regions = listOf(
        "മാത്താംപാട്ട",
        "Kumbleri",
        "Nilgiri",
        "Valiyaveedu",
        "Elanthuruthy"
    ).sorted()

    val bloodGroups = listOf(
        "A positive (A+)",
        "A negative (A-)",
        "B positive (B+)",
        "B negative (B-)",
        "AB positive (AB+)",
        "AB negative (AB-)",
        "O positive (O+)",
        "O negative (O-)"
    )

    val genders = listOf(
        "Male",
        "Female"
    )

}