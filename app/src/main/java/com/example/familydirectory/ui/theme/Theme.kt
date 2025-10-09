package com.example.familydirectory.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    // Primary Colors (Deep Blue)
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = SurfaceBlueLight,
    onPrimaryContainer = PrimaryBlueDark,

    // Secondary Colors (Sky Blue)
    secondary = SecondaryBlue,
    onSecondary = Color.White,
    secondaryContainer = SecondaryBlueLight.copy(alpha = 0.3f),
    onSecondaryContainer = SecondaryBlueDark,

    // Tertiary Colors (Teal)
    tertiary = AccentTeal,
    onTertiary = Color.White,
    tertiaryContainer = AccentTeal.copy(alpha = 0.2f),
    onTertiaryContainer = Color(0xFF004D40),

    // Background (White)
    background = BackgroundWhite,
    onBackground = TextPrimary,

    // Surface (Off-White with blue hint)
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceBlueLight,
    onSurfaceVariant = TextSecondary,

    // Error Colors
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,

    // Outline
    outline = BorderBlue,
    outlineVariant = DividerLight,

    // Surface Tint
    surfaceTint = PrimaryBlue,

    // Inverse Colors
    inverseSurface = PrimaryBlueDark,
    inverseOnSurface = Color.White,
    inversePrimary = PrimaryBlueLight
)

private val DarkColorScheme = darkColorScheme(
    // Primary Colors
    primary = PrimaryBlueLight,
    onPrimary = PrimaryBlueDark,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueLight,

    // Secondary Colors
    secondary = SecondaryBlueLight,
    onSecondary = SecondaryBlueDark,
    secondaryContainer = SecondaryBlueDark,
    onSecondaryContainer = SecondaryBlueLight,

    // Tertiary Colors
    tertiary = AccentTeal,
    onTertiary = Color(0xFF003D33),
    tertiaryContainer = Color(0xFF005B4F),
    onTertiaryContainer = AccentTeal.copy(alpha = 0.7f),

    // Background (Deep Blue, not black)
    background = Color(0xFF0A1929), // Deep Blue background
    onBackground = Color(0xFFE3F2FD),

    // Surface (Dark Blue)
    surface = Color(0xFF112240), // Dark Blue surface
    onSurface = Color(0xFFE3F2FD),
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = Color(0xFFB0BEC5),

    // Error Colors
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.2f),
    onErrorContainer = ErrorRed.copy(alpha = 0.9f),

    // Outline
    outline = Color(0xFF5472D3),
    outlineVariant = Color(0xFF1E3A5F),

    // Surface Tint
    surfaceTint = PrimaryBlueLight,

    // Inverse Colors
    inverseSurface = Color(0xFFE3F2FD),
    inverseOnSurface = Color(0xFF0A1929),
    inversePrimary = PrimaryBlue
)

@Composable
fun FamilydirectoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to always use custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}