// CloudinaryManager.kt

package com.example.familydirectory.data.cloudinary

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.familydirectory.BuildConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryManager {

    private var isInitialized = false

    private const val CLOUD_NAME = BuildConfig.CLOUDINARY_CLOUD_NAME
    private const val UPLOAD_PRESET = BuildConfig.CLOUDINARY_UPLOAD_PRESET

    fun initialize(context: Context) {
        if (!isInitialized) {
            val config = mapOf(
                "cloud_name" to CLOUD_NAME,
                "secure" to true
            )
            MediaManager.init(context.applicationContext, config)
            isInitialized = true
        }
    }

    /**
     * Upload image with progress updates
     */
    fun uploadImageWithProgress(
        imageUri: Uri,
        folder: String = "events"
    ): Flow<UploadProgress> = callbackFlow {

        val requestId = MediaManager.get().upload(imageUri)
            .unsigned(UPLOAD_PRESET)
            .option("folder", folder)
            .option("resource_type", "image")
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {
                    trySend(UploadProgress.Started)
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = (bytes.toFloat() / totalBytes.toFloat() * 100).toInt()
                    trySend(UploadProgress.Progress(progress, bytes, totalBytes))
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    val publicId = resultData["public_id"] as? String

                    if (url != null && publicId != null) {
                        trySend(UploadProgress.Success(url, publicId))
                        close()
                    } else {
                        trySend(UploadProgress.Error("Upload succeeded but no URL returned"))
                        close()
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    trySend(UploadProgress.Error(error.description))
                    close()
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    trySend(UploadProgress.Rescheduled)
                }
            })
            .dispatch()

        awaitClose {
            // Cleanup if needed
        }
    }

    /**
     * Simple upload without progress (suspend function)
     */
    suspend fun uploadImage(
        imageUri: Uri,
        folder: String = "events"
    ): UploadResult = suspendCancellableCoroutine { continuation ->

        MediaManager.get().upload(imageUri)
            .unsigned(UPLOAD_PRESET)
            .option("folder", folder)
            .option("resource_type", "image")
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    val publicId = resultData["public_id"] as? String
                    val thumbnailUrl = resultData["thumbnail_url"] as? String

                    if (url != null && publicId != null) {
                        continuation.resume(
                            UploadResult.Success(
                                url = url,
                                publicId = publicId,
                                thumbnailUrl = thumbnailUrl
                            )
                        )
                    } else {
                        continuation.resumeWithException(
                            Exception("Upload succeeded but no URL returned")
                        )
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resume(
                        UploadResult.Error(error.description)
                    )
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }

    /**
     * Generate optimized image URL
     * Examples:
     * - Thumbnail: getOptimizedUrl(publicId, width = 300, height = 300)
     * - Quality: getOptimizedUrl(publicId, quality = 80)
     */
    fun getOptimizedUrl(
        publicId: String,
        width: Int? = null,
        height: Int? = null,
        quality: Int? = null,
        format: String = "auto"
    ): String {
        val baseUrl = "https://res.cloudinary.com/$CLOUD_NAME/image/upload"

        val transformations = buildList {
            if (width != null) add("w_$width")
            if (height != null) add("h_$height")
            if (quality != null) add("q_$quality")
            add("f_$format")
        }.joinToString(",")

        return "$baseUrl/$transformations/$publicId"
    }

    /**
     * Delete image from Cloudinary (requires API secret - use Cloud Functions instead)
     */
    suspend fun deleteImage(publicId: String): Boolean {
        // Note: Deletion requires API secret which shouldn't be in client app
        // Implement this via Cloud Functions or Admin SDK
        return false
    }
}

// Upload Progress States
sealed class UploadProgress {
    object Started : UploadProgress()
    data class Progress(val percentage: Int, val bytes: Long, val totalBytes: Long) : UploadProgress()
    data class Success(val url: String, val publicId: String) : UploadProgress()
    data class Error(val message: String) : UploadProgress()
    object Rescheduled : UploadProgress()
}

// Upload Result
sealed class UploadResult {
    data class Success(
        val url: String,
        val publicId: String,
        val thumbnailUrl: String?
    ) : UploadResult()

    data class Error(val message: String) : UploadResult()
}

// Extension function for easier URL generation
fun String.toCloudinaryUrl(
    width: Int? = null,
    height: Int? = null,
    quality: Int = 80
): String {
    return CloudinaryManager.getOptimizedUrl(
        publicId = this,
        width = width,
        height = height,
        quality = quality
    )
}