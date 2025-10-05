package com.example.familydirectory.ui.components

// CloudinaryImage.kt - Optimized image loading component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage

/**
 * CloudinaryImage - Optimized image loading with automatic transformations
 *
 * Cloudinary URL format:
 * https://res.cloudinary.com/CLOUD_NAME/image/upload/TRANSFORMATIONS/PUBLIC_ID
 *
 * Common transformations:
 * - w_300 = width 300px
 * - h_200 = height 200px
 * - c_fill = crop to fill
 * - q_auto = auto quality
 * - f_auto = auto format (WebP for supported browsers)
 */

@Composable
fun CloudinaryImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    thumbnail: Boolean = false,
    customWidth: Int? = null
) {
    val optimizedUrl = if (thumbnail) {
        // Thumbnail: 400px width, auto quality, auto format
        url.replace("/upload/", "/upload/w_400,q_auto,f_auto/")
    } else if (customWidth != null) {
        url.replace("/upload/", "/upload/w_$customWidth,q_auto,f_auto/")
    } else {
        // Full size with auto quality and format
        url.replace("/upload/", "/upload/q_auto:good,f_auto/")
    }

    SubcomposeAsyncImage(
        model = optimizedUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    )
}

// Example Usage:

/*
// In EventCard:
CloudinaryImage(
    url = event.images.first().url,
    contentDescription = event.title,
    modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
    thumbnail = false // Load full quality
)

// In Grid/List thumbnails:
CloudinaryImage(
    url = imageUrl,
    contentDescription = null,
    modifier = Modifier.size(100.dp),
    thumbnail = true // Load optimized thumbnail
)

// Custom width:
CloudinaryImage(
    url = imageUrl,
    contentDescription = null,
    modifier = Modifier.fillMaxWidth(),
    customWidth = 800 // Load 800px width version
)
*/

// Cloudinary Transformation Examples:
object CloudinaryTransforms {

    /**
     * Generate thumbnail URL
     * Example: w_400,h_400,c_fill,q_auto,f_auto
     */
    fun getThumbnailUrl(originalUrl: String, size: Int = 400): String {
        return originalUrl.replace(
            "/upload/",
            "/upload/w_$size,h_$size,c_fill,q_auto,f_auto/"
        )
    }

    /**
     * Generate profile picture URL (circular crop)
     * Example: w_200,h_200,c_fill,g_face,r_max,q_auto,f_auto
     */
    fun getProfilePictureUrl(originalUrl: String, size: Int = 200): String {
        return originalUrl.replace(
            "/upload/",
            "/upload/w_$size,h_$size,c_fill,g_face,r_max,q_auto,f_auto/"
        )
    }

    /**
     * Generate blurred placeholder
     * Example: w_50,q_auto,e_blur:1000
     */
    fun getBlurredPlaceholder(originalUrl: String): String {
        return originalUrl.replace(
            "/upload/",
            "/upload/w_50,q_auto,e_blur:1000/"
        )
    }

    /**
     * Generate responsive image for different screen sizes
     */
    fun getResponsiveUrl(
        originalUrl: String,
        screenWidth: Int
    ): String {
        val width = when {
            screenWidth <= 640 -> 640
            screenWidth <= 1024 -> 1024
            screenWidth <= 1920 -> 1920
            else -> 2560
        }

        return originalUrl.replace(
            "/upload/",
            "/upload/w_$width,q_auto,f_auto/"
        )
    }
}

// Usage Examples:

/*
// Thumbnail in list:
val thumbnailUrl = CloudinaryTransforms.getThumbnailUrl(event.images.first().url, 300)
AsyncImage(model = thumbnailUrl, ...)

// Profile picture:
val profileUrl = CloudinaryTransforms.getProfilePictureUrl(user.profileImageUrl)
AsyncImage(model = profileUrl, ...)

// Blurred placeholder:
val placeholderUrl = CloudinaryTransforms.getBlurredPlaceholder(imageUrl)
AsyncImage(
    model = imageUrl,
    placeholder = rememberAsyncImagePainter(placeholderUrl),
    ...
)
*/