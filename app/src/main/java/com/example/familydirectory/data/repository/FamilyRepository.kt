package com.example.familydirectory.data.repository

// FamilyRepository.kt


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.familydirectory.data.model.*
import kotlinx.coroutines.tasks.await

class FamilyRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val familiesCollection = firestore.collection("families")

    /**
     * Search families by query string
     * Searches in: familyHead, place, parish, phone, family members
     */
    suspend fun searchFamilies(
        query: String,
        filters: SearchFilters? = null
    ): Result<List<SearchResult>> {
        return try {
            if (query.isBlank()) {
                return Result.success(emptyList())
            }

            val searchTerm = query.lowercase().trim()

            // Query Firestore using searchTerms array
            var firestoreQuery = familiesCollection
                .whereArrayContains("searchTerms", searchTerm)
                .limit(50)

            // Apply filters
            filters?.let {
                if (it.parish != null) {
                    firestoreQuery = firestoreQuery.whereEqualTo("parish", it.parish)
                }
                if (it.region != null) {
                    firestoreQuery = firestoreQuery.whereEqualTo("region", it.region)
                }
                if (it.bloodGroup != null) {
                    firestoreQuery = firestoreQuery.whereEqualTo("bloodGroup", it.bloodGroup)
                }
                if (it.gender != null) {
                    firestoreQuery = firestoreQuery.whereEqualTo("gender", it.gender)
                }
            }

            val snapshot = firestoreQuery.get().await()

            val searchResults = mutableListOf<SearchResult>()

            for (doc in snapshot.documents) {
                val family = documentToFamily(doc.id, doc.data ?: continue)

                // Check match in family head
                if (family.familyHead.lowercase().contains(searchTerm)) {
                    searchResults.add(
                        SearchResult(
                            family = family,
                            matchedIn = "Family Head",
                            relevanceScore = calculateRelevance(family.familyHead, searchTerm)
                        )
                    )
                    continue
                }

                // Check match in place
                if (family.place.lowercase().contains(searchTerm)) {
                    searchResults.add(
                        SearchResult(
                            family = family,
                            matchedIn = "Place: ${family.place}",
                            relevanceScore = calculateRelevance(family.place, searchTerm)
                        )
                    )
                    continue
                }

                // Check match in phone
                if (family.phone.contains(searchTerm)) {
                    searchResults.add(
                        SearchResult(
                            family = family,
                            matchedIn = "Phone",
                            relevanceScore = 100
                        )
                    )
                    continue
                }

                // Check match in family members
                val matchedMember = family.familyMembers.find { member ->
                    member.name.lowercase().contains(searchTerm) ||
                            member.phone.contains(searchTerm)
                }

                if (matchedMember != null) {
                    searchResults.add(
                        SearchResult(
                            family = family,
                            matchedIn = "Member: ${matchedMember.name}",
                            matchedMember = matchedMember,
                            relevanceScore = calculateRelevance(matchedMember.name, searchTerm)
                        )
                    )
                }
            }

            // Sort by relevance
            val sortedResults = searchResults.sortedByDescending { it.relevanceScore }

            Result.success(sortedResults)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get family by ID
     */
    suspend fun getFamilyById(familyId: String): Family? {
        return try {
            val doc = familiesCollection.document(familyId).get().await()
            if (doc.exists()) {
                documentToFamily(doc.id, doc.data ?: return null)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get all families with pagination
     */
    suspend fun getAllFamilies(limit: Int = 50): Result<List<Family>> {
        return try {
            val snapshot = familiesCollection
                .orderBy("familyHead")
                .limit(limit.toLong())
                .get()
                .await()

            val families = snapshot.documents.mapNotNull { doc ->
                try {
                    documentToFamily(doc.id, doc.data ?: return@mapNotNull null)
                } catch (e: Exception) {
                    null
                }
            }

            Result.success(families)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Search by filters only (no text query)
     */
    suspend fun searchByFilters(filters: SearchFilters): Result<List<Family>> {
        return try {
            var query: Query = familiesCollection

            filters.parish?.let { query = query.whereEqualTo("parish", it) }
            filters.region?.let { query = query.whereEqualTo("region", it) }
            filters.bloodGroup?.let { query = query.whereEqualTo("bloodGroup", it) }
            filters.gender?.let { query = query.whereEqualTo("gender", it) }

            val snapshot = query.limit(100).get().await()

            val families = snapshot.documents.mapNotNull { doc ->
                try {
                    documentToFamily(doc.id, doc.data ?: return@mapNotNull null)
                } catch (e: Exception) {
                    null
                }
            }

            Result.success(families)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get unique parishes for filter dropdown
     */
    suspend fun getAllParishes(): List<String> {
        return try {
            val snapshot = familiesCollection.get().await()
            snapshot.documents
                .mapNotNull { it.getString("parish") }
                .distinct()
                .sorted()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get unique regions for filter dropdown
     */
    suspend fun getAllRegions(): List<String> {
        return try {
            val snapshot = familiesCollection.get().await()
            snapshot.documents
                .mapNotNull { it.getString("region") }
                .distinct()
                .sorted()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Helper: Convert Firestore document to Family object
    private fun documentToFamily(docId: String, data: Map<String, Any>): Family {
        val membersData = data["familyMembers"] as? List<Map<String, Any>> ?: emptyList()

        return Family(
            id = docId,
            familyHead = data["familyHead"] as? String ?: "",
            place = data["place"] as? String ?: "",
            postOffice = data["postOffice"] as? String ?: "",
            region = data["region"] as? String ?: "",
            parish = data["parish"] as? String ?: "",
            phone = data["phone"] as? String ?: "",
            email = data["email"] as? String ?: "",
            job = data["job"] as? String ?: "",
            education = data["education"] as? String ?: "",
            bloodGroup = data["bloodGroup"] as? String ?: "",
            dob = data["dob"] as? String ?: "",
            gender = data["gender"] as? String ?: "",
            otherInfo = data["otherInfo"] as? String ?: "",
            familyMembers = membersData.map { memberData ->
                FamilyMember(
                    name = memberData["name"] as? String ?: "",
                    relation = memberData["relation"] as? String ?: "",
                    fatherName = memberData["fatherName"] as? String ?: "",
                    motherName = memberData["motherName"] as? String ?: "",
                    spouseName = memberData["spouseName"] as? String ?: "",
                    dob = memberData["dob"] as? String ?: "",
                    education = memberData["education"] as? String ?: "",
                    job = memberData["job"] as? String ?: "",
                    institution = memberData["institution"] as? String ?: "",
                    phone = memberData["phone"] as? String ?: "",
                    email = memberData["email"] as? String ?: "",
                    bloodGroup = memberData["bloodGroup"] as? String ?: "",
                    gender = memberData["gender"] as? String ?: ""
                )
            },
            searchTerms = (data["searchTerms"] as? List<String>) ?: emptyList()
        )
    }

    // Helper: Calculate relevance score for search results
    private fun calculateRelevance(text: String, searchTerm: String): Int {
        val lowerText = text.lowercase()
        val lowerSearch = searchTerm.lowercase()

        return when {
            lowerText == lowerSearch -> 100 // Exact match
            lowerText.startsWith(lowerSearch) -> 90 // Starts with
            lowerText.contains(" $lowerSearch") -> 80 // Word boundary
            lowerText.contains(lowerSearch) -> 70 // Contains
            else -> 50
        }
    }
}