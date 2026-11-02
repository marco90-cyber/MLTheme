package com.marco.mltheme

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils

/** Modalità del tema. */
val THEME_MODES = listOf(
    "auto" to "Automatico",
    "light" to "Chiaro",
    "dark" to "Scuro"
)

/**
 * Colori dell'app: "system" = la tavolozza Material You di Android (Sfondo e stile),
 * "auto" = dallo sfondo, altrimenti un colore fisso.
 */
val ACCENTS = listOf(
    "system" to "Dal sistema",
    "auto" to "Dallo sfondo",
    "blue" to "Blu",
    "green" to "Verde",
    "purple" to "Viola",
    "red" to "Rosso",
    "orange" to "Arancione",
    "gray" to "Grigio"
)

/** I colori che hanno senso su questo telefono: "Dal sistema" esiste solo da Android 12. */
val availableAccents: List<Pair<String, String>>
    get() = if (Build.VERSION.SDK_INT >= 31) ACCENTS else ACCENTS.filter { it.first != "system" }

/** Tonalità (gradi) e saturazione di ogni colore fisso. */
private fun accentHue(key: String): Pair<Float, Float>? = when (key) {
    "blue" -> 215f to 0.75f
    "green" -> 140f to 0.55f
    "purple" -> 270f to 0.55f
    "red" -> 355f to 0.70f
    "orange" -> 28f to 0.85f
    "gray" -> 220f to 0.06f
    else -> null
}

/** Colore campione per le anteprime. */
fun accentSwatch(key: String): Color? = accentHue(key)?.let { (h, s) -> hsl(h, s, 0.45f) }

private fun hsl(h: Float, s: Float, l: Float): Color {
    val c = ColorUtils.HSLToColor(floatArrayOf(((h % 360f) + 360f) % 360f, s.coerceIn(0f, 1f), l.coerceIn(0f, 1f)))
    return Color(c)
}

/** Schema completo ricavato da un colore, chiaro o scuro (tonalità alla Material 3). */
private fun accentScheme(base: ColorScheme, h: Float, s: Float, dark: Boolean): ColorScheme {
    val s2 = s * 0.35f
    val s3 = s * 0.7f
    val h3 = h + 60f
    val ns = minOf(s, 0.10f) // superfici appena tinte
    return if (!dark) base.copy(
        primary = hsl(h, s, 0.40f), onPrimary = Color.White,
        primaryContainer = hsl(h, s, 0.90f), onPrimaryContainer = hsl(h, s, 0.12f),
        secondary = hsl(h, s2, 0.40f), onSecondary = Color.White,
        secondaryContainer = hsl(h, s2, 0.90f), onSecondaryContainer = hsl(h, s2, 0.12f),
        tertiary = hsl(h3, s3, 0.40f), onTertiary = Color.White,
        tertiaryContainer = hsl(h3, s3, 0.90f), onTertiaryContainer = hsl(h3, s3, 0.12f),
        background = hsl(h, ns, 0.98f), onBackground = hsl(h, ns, 0.10f),
        surface = hsl(h, ns, 0.98f), onSurface = hsl(h, ns, 0.10f),
        surfaceVariant = hsl(h, ns * 2, 0.90f), onSurfaceVariant = hsl(h, ns * 2, 0.30f),
        surfaceContainerLowest = Color.White,
        surfaceContainerLow = hsl(h, ns, 0.96f),
        surfaceContainer = hsl(h, ns, 0.94f),
        surfaceContainerHigh = hsl(h, ns, 0.92f),
        surfaceContainerHighest = hsl(h, ns, 0.90f),
        outline = hsl(h, ns, 0.50f), outlineVariant = hsl(h, ns, 0.80f),
        inversePrimary = hsl(h, s, 0.80f)
    ) else base.copy(
        primary = hsl(h, s, 0.78f), onPrimary = hsl(h, s, 0.18f),
        primaryContainer = hsl(h, s, 0.30f), onPrimaryContainer = hsl(h, s, 0.90f),
        secondary = hsl(h, s2, 0.78f), onSecondary = hsl(h, s2, 0.18f),
        secondaryContainer = hsl(h, s2, 0.28f), onSecondaryContainer = hsl(h, s2, 0.90f),
        tertiary = hsl(h3, s3, 0.78f), onTertiary = hsl(h3, s3, 0.18f),
        tertiaryContainer = hsl(h3, s3, 0.30f), onTertiaryContainer = hsl(h3, s3, 0.90f),
        background = hsl(h, ns, 0.07f), onBackground = hsl(h, ns, 0.90f),
        surface = hsl(h, ns, 0.07f), onSurface = hsl(h, ns, 0.90f),
        surfaceVariant = hsl(h, ns * 2, 0.28f), onSurfaceVariant = hsl(h, ns * 2, 0.80f),
        surfaceContainerLowest = hsl(h, ns, 0.04f),
        surfaceContainerLow = hsl(h, ns, 0.10f),
        surfaceContainer = hsl(h, ns, 0.12f),
        surfaceContainerHigh = hsl(h, ns, 0.16f),
        surfaceContainerHighest = hsl(h, ns, 0.21f),
        outline = hsl(h, ns, 0.55f), outlineVariant = hsl(h, ns, 0.28f),
        inversePrimary = hsl(h, s, 0.40f)
    )
}

/** Colore principale dello sfondo del telefono (Android 8.1+), null se non disponibile. */
internal fun wallpaperSeed(ctx: Context): Int? {
    if (Build.VERSION.SDK_INT < 27) return null
    return try {
        WallpaperManager.getInstance(ctx).getWallpaperColors(WallpaperManager.FLAG_SYSTEM)?.primaryColor?.toArgb()
    } catch (e: Throwable) {
        null
    }
}

/**
 * Calcola lo schema colori come MLauncher.
 * [seed] = colore di partenza per "Dallo sfondo" (es. uno sfondo interno all'app);
 * se null si usa lo sfondo del telefono.
 */
@Composable
fun mlColorScheme(mode: String, accent: String, seed: Int? = null): ColorScheme {
    val dark = when (mode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }
    val ctx = LocalContext.current
    val hasDynamic = Build.VERSION.SDK_INT >= 31
    // tavolozza di sistema: se la cambi in Android, l'attività viene ricreata e i colori si aggiornano da soli
    val base = if (Build.VERSION.SDK_INT >= 31) {
        if (dark) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
    } else {
        if (dark) darkColorScheme() else lightColorScheme()
    }
    // senza tavolozza di sistema (prima di Android 12) "Dal sistema" diventa "Dallo sfondo"
    val key = if (accent == "system" && !hasDynamic) "auto" else accent
    if (key == "system") return base

    val wallSeed = remember(key, seed) { if (key == "auto" && seed == null && !hasDynamic) wallpaperSeed(ctx) else null }
    val fromSeed = if (key == "auto") (seed ?: wallSeed)?.let {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(it, hsl)
        hsl[0] to hsl[1].coerceIn(0.08f, 0.85f)
    } else null
    val hs = accentHue(key) ?: fromSeed
    return when {
        hs != null -> accentScheme(base, hs.first, hs.second, dark)
        hasDynamic -> base // "Dallo sfondo" senza sfondo proprio = tavolozza di sistema, che viene già dallo sfondo
        else -> accentScheme(base, 215f, 0.75f, dark) // nessun colore disponibile: blu
    }
}

/**
 * Tema completo delle MLapps: colori, stile degli interruttori.
 * Senza parametri legge le scelte salvate dell'app ([MLThemeSettings]).
 */
@Composable
fun MLTheme(
    mode: String = MLThemeSettings.get(LocalContext.current).theme,
    accent: String = MLThemeSettings.get(LocalContext.current).accent,
    switchStyle: String = MLThemeSettings.get(LocalContext.current).switchStyle,
    seed: Int? = null,
    typography: Typography = Typography(),
    content: @Composable () -> Unit
) {
    val scheme = mlColorScheme(mode, accent, seed)
    CompositionLocalProvider(LocalSwitchStyle provides switchStyle) {
        MaterialTheme(colorScheme = scheme, typography = typography, content = content)
    }
}
