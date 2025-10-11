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
    primary = DeepRoyalBlue,
    onPrimary = PureWhite,
    primaryContainer = SoftGray,
    onPrimaryContainer = DeepRoyalBlue,

    // Secondary Colors
    secondary = RoyalBlueLight,
    onSecondary = PureWhite,
    secondaryContainer = SoftGray,
    onSecondaryContainer = DeepRoyalBlue,

    // Tertiary Colors (Teal/Terracotta)
    tertiary = WarmTerracotta,
    onTertiary = PureWhite,
    tertiaryContainer = WarmTerracotta.copy(alpha = 0.2f),
    onTertiaryContainer = WarmTerracotta,

    // Background (White)
    background = PureWhite,
    onBackground = TextDark,

    // Surface (Off-White)
    surface = PureWhite,
    onSurface = TextDark,
    surfaceVariant = SoftGray,
    onSurfaceVariant = TextSecondary,

    // Error Colors
    error = ErrorRed,
    onError = PureWhite,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,

    // Outline
    outline = LightBorder,
    outlineVariant = LightBorder.copy(alpha = 0.5f),

    // Surface Tint
    surfaceTint = DeepRoyalBlue,

    // Inverse Colors
    inverseSurface = DeepRoyalBlue,
    inverseOnSurface = PureWhite,
    inversePrimary = HeritageGold
)

private val DarkColorScheme = darkColorScheme(
    // Primary Colors
    primary = RoyalBlueLight,
    onPrimary = DeepRoyalBlue,
    primaryContainer = DeepRoyalBlue,
    onPrimaryContainer = RoyalBlueLight,

    // Secondary Colors
    secondary = RoyalBlueLight,
    onSecondary = DeepRoyalBlue,
    secondaryContainer = RoyalBlueDark,
    onSecondaryContainer = RoyalBlueLight,

    // Tertiary Colors
    tertiary = WarmTerracotta,
    onTertiary = Color(0xFF3D2517),
    tertiaryContainer = Color(0xFF5B3A27),
    onTertiaryContainer = WarmTerracotta.copy(alpha = 0.7f),

    // Background (Deep Blue, not black)
    background = RoyalBlueDark,
    onBackground = SoftGray,

    // Surface (Dark Blue)
    surface = Color(0xFF112240),
    onSurface = SoftGray,
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = TextHint,

    // Error Colors
    error = ErrorRed,
    onError = PureWhite,
    errorContainer = ErrorRed.copy(alpha = 0.2f),
    onErrorContainer = ErrorRed.copy(alpha = 0.9f),

    // Outline
    outline = RoyalBlueLight,
    outlineVariant = Color(0xFF1E3A5F),

    // Surface Tint
    surfaceTint = RoyalBlueLight,

    // Inverse Colors
    inverseSurface = SoftGray,
    inverseOnSurface = RoyalBlueDark,
    inversePrimary = DeepRoyalBlue
)

@Composable
fun FamilydirectoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
        shapes = Shapes,
        content = content
    )
}