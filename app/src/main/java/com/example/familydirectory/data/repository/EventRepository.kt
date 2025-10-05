package com.example.familydirectory.data.repository

// EventRepository.kt

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.familydirectory.data.cloudinary.CloudinaryManager
import com.example.familydirectory.data.cloudinary.UploadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class EventRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")

    /**
     * Upload event with images
     */
    suspend fun uploadEvent(
        title: String,
        description: String,
        imageUris: List<Uri>,
        uploadedBy: String
    ): Result<String> {
        return try {
            // Upload all images to Cloudinary
            val uploadedImages = mutableListOf<CloudinaryImage>()

            for (uri in imageUris) {
                when (val result = CloudinaryManager.uploadImage(uri, folder = "events")) {
                    is UploadResult.Success -> {
                        uploadedImages.add(
                            CloudinaryImage(
                                url = result.url,
                                publicId = result.publicId,
                                thumbnailUrl = result.thumbnailUrl
                            )
                        )
                    }
                    is UploadResult.Error -> {
                        return Result.failure(Exception("Image upload failed: ${result.message}"))
                    }
                }
            }

            // Save to Firestore
            val event = hashMapOf(
                "title" to title,
                "description" to description,
                "images" to uploadedImages.map { mapOf(
                    "url" to it.url,
                    "publicId" to it.publicId,
                    "thumbnailUrl" to it.thumbnailUrl
                )},
                "uploadedBy" to uploadedBy,
                "createdAt" to com.google.firebase.Timestamp.now(),
                "likesCount" to 0
            )

            val docRef = eventsCollection.add(event).await()
            Result.success(docRef.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all events with pagination
     */
    fun getEvents(limit: Int = 20): Flow<List<Event>> = flow {
        try {
            val snapshot = eventsCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val events = snapshot.documents.mapNotNull { doc ->
                try {
                    Event(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        images = (doc.get("images") as? List<Map<String, Any>>)?.map { img ->
                            CloudinaryImage(
                                url = img["url"] as? String ?: "",
                                publicId = img["publicId"] as? String ?: "",
                                thumbnailUrl = img["thumbnailUrl"] as? String
                            )
                        } ?: emptyList(),
                        uploadedBy = doc.getString("uploadedBy") ?: "",
                        createdAt = doc.getTimestamp("createdAt")?.toDate() ?: Date(),
                        likesCount = doc.getLong("likesCount")?.toInt() ?: 0
                    )
                } catch (e: Exception) {
                    null
                }
            }

            emit(events)

        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    /**
     * Get single event by ID
     */
    suspend fun getEventById(eventId: String): Event? {
        return try {
            val doc = eventsCollection.document(eventId).get().await()

            if (doc.exists()) {
                Event(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    images = (doc.get("images") as? List<Map<String, Any>>)?.map { img ->
                        CloudinaryImage(
                            url = img["url"] as? String ?: "",
                            publicId = img["publicId"] as? String ?: "",
                            thumbnailUrl = img["thumbnailUrl"] as? String
                        )
                    } ?: emptyList(),
                    uploadedBy = doc.getString("uploadedBy") ?: "",
                    createdAt = doc.getTimestamp("createdAt")?.toDate() ?: Date(),
                    likesCount = doc.getLong("likesCount")?.toInt() ?: 0
                )
            } else null

        } catch (e: Exception) {
            null
        }
    }

    /**
     * Delete event
     */
    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            eventsCollection.document(eventId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Data Models
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val images: List<CloudinaryImage> = emptyList(),
    val uploadedBy: String = "",
    val createdAt: Date = Date(),
    val likesCount: Int = 0
)

data class CloudinaryImage(
    val url: String,
    val publicId: String,
    val thumbnailUrl: String? = null
)