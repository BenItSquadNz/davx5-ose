/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package at.bitfire.davdroid.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

@Suppress("MemberVisibilityCanBePrivate")
object M3ColorScheme {

    // ── App palette ─────────────────────────────────────────────────────
    // Cyan  #27AAE1  →  primary   (buttons, active indicators)
    // Green #8CC641  →  secondary (accent, FABs)
    // Navy  #2F5577  →  tertiary  (top bars, containers, depth)

    // ── Light scheme ────────────────────────────────────────────────────
    val primaryLight = Color(0xFF27AAE1)            // cyan – prominent accents
    val onPrimaryLight = Color(0xFF00232E)           // very dark teal – AA on cyan
    val primaryContainerLight = Color(0xFFD4F0FC)    // pale cyan tint
    val onPrimaryContainerLight = Color(0xFF042830)  // near-black teal
    val secondaryLight = Color(0xFF8CC641)           // green – accent
    val onSecondaryLight = Color(0xFF0D2200)         // very dark green – AA on green
    val secondaryContainerLight = Color(0xFFDAEFB8)  // pale green tint
    val onSecondaryContainerLight = Color(0xFF142006)
    val tertiaryLight = Color(0xFF2F5577)            // navy – bars, depth
    val onTertiaryLight = Color(0xFFFFFFFF)          // white on navy – 6:1 ratio
    val tertiaryContainerLight = Color(0xFFC4DDEC)   // pale navy tint
    val onTertiaryContainerLight = Color(0xFF0C1E2E)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFF8FAFB)          // very faint cool grey
    val onBackgroundLight = Color(0xFF1A1C1E)        // near-black
    val surfaceLight = Color(0xFFF3F5F6)
    val onSurfaceLight = Color(0xFF1A1C1E)
    val surfaceVariantLight = Color(0xFFE0E3E6)
    val onSurfaceVariantLight = Color(0xFF3A3F44)
    val outlineLight = Color(0xFF74787D)
    val outlineVariantLight = Color(0xFFC4C8CC)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF2F3133)
    val inverseOnSurfaceLight = Color(0xFFF1F0F2)
    val inversePrimaryLight = Color(0xFF8CC641)
    val surfaceDimLight = Color(0xFFDADCDE)
    val surfaceBrightLight = Color(0xFFFAFAFB)
    val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    val surfaceContainerLowLight = Color(0xFFF5F6F7)
    val surfaceContainerLight = Color(0xFFEFF0F2)
    val surfaceContainerHighLight = Color(0xFFE9EBED)
    val surfaceContainerHighestLight = Color(0xFFE4E5E7)

    // ── Dark scheme ─────────────────────────────────────────────────────
    val primaryDark = Color(0xFF4DC4F0)              // brighter cyan for dark bg
    val onPrimaryDark = Color(0xFF00344A)
    val primaryContainerDark = Color(0xFF0F4A63)
    val onPrimaryContainerDark = Color(0xFFD4F0FC)
    val secondaryDark = Color(0xFFA4D65E)            // brighter green for dark bg
    val onSecondaryDark = Color(0xFF1A3800)
    val secondaryContainerDark = Color(0xFF244F00)
    val onSecondaryContainerDark = Color(0xFFDAEFB8)
    val tertiaryDark = Color(0xFF92C4E0)             // lightened navy-toned blue
    val onTertiaryDark = Color(0xFF0C2E46)
    val tertiaryContainerDark = Color(0xFF1F435E)
    val onTertiaryContainerDark = Color(0xFFC4DDEC)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF111416)           // very dark with navy tint
    val onBackgroundDark = Color(0xFFE2E2E4)
    val surfaceDark = Color(0xFF1A1D20)
    val onSurfaceDark = Color(0xFFE2E2E4)
    val surfaceVariantDark = Color(0xFF3A3F44)
    val onSurfaceVariantDark = Color(0xFFC4C8CC)
    val outlineDark = Color(0xFF8E9296)
    val outlineVariantDark = Color(0xFF3A3F44)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFE2E2E4)
    val inverseOnSurfaceDark = Color(0xFF2F3133)
    val inversePrimaryDark = Color(0xFF8CC641)
    val surfaceDimDark = Color(0xFF111416)
    val surfaceBrightDark = Color(0xFF373A3C)
    val surfaceContainerLowestDark = Color(0xFF0C0E10)
    val surfaceContainerLowDark = Color(0xFF1A1C1E)
    val surfaceContainerDark = Color(0xFF1E2022)
    val surfaceContainerHighDark = Color(0xFF282A2D)
    val surfaceContainerHighestDark = Color(0xFF333537)


    // Copied from Material Theme Builder: Theme.kt

    val lightScheme = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    )

    val darkScheme = darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
    )

}
