package com.example.familydirectory.data.repository

import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.SearchFilters
import com.example.familydirectory.data.model.SearchResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FamilyRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val familiesCollection = firestore.collection("families")

    /**
     * Normalize Firebase data - Run this once to clean up inconsistent data
     */
    suspend fun normalizeFirebaseData(): Result<Int> {
        return try {
            val families = getAllFamilies()
            var updatedCount = 0

            families.forEach { family ->
                val updates = mutableMapOf<String, Any>()

                // Normalize parish names
                val normalizedParish = when {
                    family.parish.contains("Kuruppampady", ignoreCase = true) &&
                            family.parish.contains("Mary", ignoreCase = true) ->
                        "St. Mary's Orthodox Church, Kuruppampady"

                    family.parish.contains("Cheengeri", ignoreCase = true) &&
                            family.parish.contains("Jacobite", ignoreCase = true) ->
                        "St. Mary's Jacobite Church, Cheengeri"

                    family.parish.contains("Kolagappara", ignoreCase = true) ->
                        "St. Thomas Malankara Catholic Church, Kolagappara"

                    family.parish.contains("Kallichal", ignoreCase = true) ->
                        "St. Mary's Malankara Catholic Church, Kallichal"

                    family.parish.contains("Pulluvazhy", ignoreCase = true) ->
                        "Saron Fellowship Church, Pulluvazhy"

                    family.parish.contains("Mar Gregorious", ignoreCase = true) ->
                        "Mar Gregorious Church, Iringole"

                    else -> family.parish
                }

                if (normalizedParish != family.parish) {
                    updates["parish"] = normalizedParish
                }

                // Normalize region names (if empty, infer from place)
                if (family.region.isBlank() || family.region.isEmpty()) {
                    val inferredRegion = when {
                        family.place.contains("Kumbleri", ignoreCase = true) -> "Kumbleri"
                        family.place.contains("Iringole", ignoreCase = true) -> "Valiyaveedu"
                        family.place.contains("Kallichal", ignoreCase = true) -> "Nilgiri"
                        else -> ""
                    }
                    if (inferredRegion.isNotBlank()) {
                        updates["region"] = inferredRegion
                    }
                }

                // Apply updates if any
                if (updates.isNotEmpty()) {
                    familiesCollection
                        .document(family.id)
                        .update(updates)
                        .await()
                    updatedCount++
                    println("✅ Updated family: ${family.familyHead} - $updates")
                }
            }

            println("✅ Normalization complete: $updatedCount families updated")
            Result.success(updatedCount)

        } catch (e: Exception) {
            println("❌ Normalization error: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Search families with filters and query - IMPROVED VERSION
     */
    suspend fun searchFamilies(
        query: String,
        filters: SearchFilters
    ): Result<List<SearchResult>> {
        return try {
            println("DEBUG: Starting search with query='$query', filters=$filters")

            val allFamilies = getAllFamilies()
            println("DEBUG: Total families loaded: ${allFamilies.size}")

            // First apply filters
            var filteredFamilies = allFamilies

            // Parish filter - case insensitive and flexible matching
            if (filters.parish != null) {
                println("DEBUG: Applying parish filter: ${filters.parish}")
                filteredFamilies = filteredFamilies.filter { family ->
                    val matches = family.parish.contains(filters.parish, ignoreCase = true) ||
                            filters.parish.contains(family.parish, ignoreCase = true)
                    if (matches) {
                        println("DEBUG: Parish match: ${family.familyHead} - ${family.parish}")
                    }
                    matches
                }
                println("DEBUG: After parish filter: ${filteredFamilies.size} families")
            }

            // Region filter - case insensitive
            if (filters.region != null) {
                println("DEBUG: Applying region filter: ${filters.region}")
                filteredFamilies = filteredFamilies.filter { family ->
                    family.region.equals(filters.region, ignoreCase = true)
                }
                println("DEBUG: After region filter: ${filteredFamilies.size} families")
            }

            // Blood group filter - exact match
            if (filters.bloodGroup != null) {
                println("DEBUG: Applying blood group filter: ${filters.bloodGroup}")
                filteredFamilies = filteredFamilies.filter { family ->
                    // Check family head's blood group
                    family.bloodGroup.equals(filters.bloodGroup, ignoreCase = true) ||
                            // Check family members' blood groups
                            family.familyMembers.any { member ->
                                member.bloodGroup.equals(filters.bloodGroup, ignoreCase = true)
                            }
                }
                println("DEBUG: After blood group filter: ${filteredFamilies.size} families")
            }

            // Gender filter - exact match
            if (filters.gender != null) {
                println("DEBUG: Applying gender filter: ${filters.gender}")
                filteredFamilies = filteredFamilies.filter { family ->
                    // Check family head's gender
                    family.gender.equals(filters.gender, ignoreCase = true) ||
                            // Check family members' genders
                            family.familyMembers.any { member ->
                                member.gender.equals(filters.gender, ignoreCase = true)
                            }
                }
                println("DEBUG: After gender filter: ${filteredFamilies.size} families")
            }

            // Then apply search query if present
            val searchResults = if (query.isBlank()) {
                // No query, just return filtered families
                filteredFamilies.map { family ->
                    SearchResult(
                        family = family,
                        matchedIn = when {
                            filters.parish != null -> "Parish: ${family.parish}"
                            filters.region != null -> "Region: ${family.region}"
                            filters.bloodGroup != null -> "Blood Group: ${filters.bloodGroup}"
                            filters.gender != null -> "Gender: ${filters.gender}"
                            else -> "All Families"
                        }
                    )
                }
            } else {
                // Apply search query on filtered families
                val searchTerm = query.lowercase()
                println("DEBUG: Applying search term: $searchTerm")

                filteredFamilies.mapNotNull { family ->
                    val matchedIn = when {
                        family.familyHead.lowercase().contains(searchTerm) -> "Family Head"
                        family.place.lowercase().contains(searchTerm) -> "Place"
                        family.phone.contains(searchTerm) -> "Phone"
                        family.parish.lowercase().contains(searchTerm) -> "Parish"
                        family.region.lowercase().contains(searchTerm) -> "Region"
                        family.email.lowercase().contains(searchTerm) -> "Email"
                        family.postOffice.lowercase().contains(searchTerm) -> "Post Office"
                        family.familyMembers.any { member ->
                            member.name.lowercase().contains(searchTerm) ||
                                    member.phone.contains(searchTerm) ||
                                    member.email.lowercase().contains(searchTerm)
                        } -> "Family Member"
                        else -> null
                    }

                    matchedIn?.let {
                        SearchResult(
                            family = family,
                            matchedIn = it
                        )
                    }
                }
            }

            println("DEBUG: Final results: ${searchResults.size}")
            Result.success(searchResults)

        } catch (e: Exception) {
            println("DEBUG: Search error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Get all families (for loading filter options)
     */
    suspend fun getAllFamilies(): List<Family> {
        return try {
            val snapshot = familiesCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Family::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            println("ERROR: Failed to get all families: ${e.message}")
            emptyList()
        }
    }

    /**
     * Get available parishes from Firebase - DYNAMIC
     */
    suspend fun getAvailableParishes(): List<String> {
        return try {
            val families = getAllFamilies()
            families
                .map { it.parish.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .sortedBy { it.lowercase() }
        } catch (e: Exception) {
            println("ERROR: Failed to get available parishes: ${e.message}")
            emptyList()
        }
    }

    /**
     * Get available regions from Firebase - DYNAMIC
     */
    suspend fun getAvailableRegions(): List<String> {
        return try {
            val families = getAllFamilies()
            families
                .map { it.region.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .sortedBy { it.lowercase() }
        } catch (e: Exception) {
            println("ERROR: Failed to get available regions: ${e.message}")
            emptyList()
        }
    }

    /**
     * Get family by ID
     */
    suspend fun getFamilyById(familyId: String): Family? {
        return try {
            val document = familiesCollection.document(familyId).get().await()
            document.toObject(Family::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            println("ERROR: Failed to get family by ID: ${e.message}")
            null
        }
    }

    /**
     * Add new family
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
     * Update family
     */
    suspend fun updateFamily(familyId: String, family: Family): Result<Unit> {
        return try {
            familiesCollection.document(familyId).set(family).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete family
     */
    suspend fun deleteFamily(familyId: String): Result<Unit> {
        return try {
            familiesCollection.document(familyId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}