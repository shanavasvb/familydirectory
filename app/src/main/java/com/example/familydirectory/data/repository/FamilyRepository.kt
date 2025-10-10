package com.example.familydirectory.data.repository

import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.SearchFilters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FamilyRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val familiesCollection = firestore.collection("families")

    /**
     * Add new family to Firestore
     */
    suspend fun addFamily(family: Family): Result<String> {
        return try {
            val docRef = familiesCollection.add(family).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get family by ID
     */
    suspend fun getFamilyById(familyId: String): Family? {
        return try {
            val snapshot = familiesCollection.document(familyId).get().await()
            snapshot.toObject(Family::class.java)?.copy(id = snapshot.id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Search families by query with filters
     */
    suspend fun searchFamilies(
        query: String,
        filters: SearchFilters
    ): Result<List<com.example.familydirectory.data.model.SearchResult>> {
        return try {
            val searchTerms = query.lowercase().split(" ").filter { it.isNotBlank() }

            var firestoreQuery: Query = familiesCollection

            // Apply filters
            filters.parish?.let { firestoreQuery = firestoreQuery.whereEqualTo("parish", it) }
            filters.region?.let { firestoreQuery = firestoreQuery.whereEqualTo("region", it) }
            filters.bloodGroup?.let { firestoreQuery = firestoreQuery.whereEqualTo("bloodGroup", it) }
            filters.gender?.let { firestoreQuery = firestoreQuery.whereEqualTo("gender", it) }

            val snapshot = firestoreQuery.get().await()

            val results = snapshot.documents.mapNotNull { doc ->
                val family = doc.toObject(Family::class.java)?.copy(id = doc.id)
                family?.let {
                    val matchScore = calculateMatchScore(it, searchTerms)
                    if (matchScore > 0) {
                        com.example.familydirectory.data.model.SearchResult(
                            family = it,
                            matchedIn = determineMatchLocation(it, searchTerms),
                            relevanceScore = matchScore
                        )
                    } else null
                }
            }.sortedByDescending { it.relevanceScore }

            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Search by filters only
     */
    suspend fun searchByFilters(filters: SearchFilters): Result<List<Family>> {
        return try {
            var query: Query = familiesCollection

            filters.parish?.let { query = query.whereEqualTo("parish", it) }
            filters.region?.let { query = query.whereEqualTo("region", it) }
            filters.bloodGroup?.let { query = query.whereEqualTo("bloodGroup", it) }
            filters.gender?.let { query = query.whereEqualTo("gender", it) }

            val snapshot = query.get().await()
            val families = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Family::class.java)?.copy(id = doc.id)
            }

            Result.success(families)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all parishes
     */
    suspend fun getAllParishes(): List<String> {
        return try {
            val snapshot = familiesCollection.get().await()
            snapshot.documents.mapNotNull { it.getString("parish") }.distinct().sorted()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get all regions
     */
    suspend fun getAllRegions(): List<String> {
        return try {
            val snapshot = familiesCollection.get().await()
            snapshot.documents.mapNotNull { it.getString("region") }.distinct().sorted()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun calculateMatchScore(family: Family, searchTerms: List<String>): Int {
        var score = 0

        searchTerms.forEach { term ->
            if (family.searchTerms.contains(term)) score += 10
            if (family.familyHead.contains(term, ignoreCase = true)) score += 20
            if (family.phone.contains(term)) score += 15
            if (family.place.contains(term, ignoreCase = true)) score += 10
        }

        return score
    }

    private fun determineMatchLocation(family: Family, searchTerms: List<String>): String {
        searchTerms.forEach { term ->
            when {
                family.familyHead.contains(term, ignoreCase = true) -> return "Name"
                family.phone.contains(term) -> return "Phone"
                family.place.contains(term, ignoreCase = true) -> return "Location"
                family.parish.contains(term, ignoreCase = true) -> return "Parish"
                family.familyMembers.any { it.name.contains(term, ignoreCase = true) } -> return "Family Member"
            }
        }
        return "General Match"
    }
}