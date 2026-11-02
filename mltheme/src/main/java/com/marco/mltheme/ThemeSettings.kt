package com.marco.mltheme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

/**
 * Gruppo "Tema" pronto da mettere nelle impostazioni di ogni app:
 * tema chiaro/scuro, colori, stile degli interruttori. Le scelte si salvano da sole.
 */
@Composable
fun MLThemeSettingsGroup(
    title: String? = "Tema",
    settings: MLThemeSettings = MLThemeSettings.get(LocalContext.current)
) {
    MLGroup(title) {
        MLChoiceRow("Tema", THEME_MODES, settings.theme, preview = { ThemePreview(it) }) { settings.theme = it }
        MLChoiceRow("Colori", availableAccents, settings.accent, preview = { AccentPreview(it) }) { settings.accent = it }
        MLChoiceRow("Stile degli interruttori", SWITCH_STYLES, settings.switchStyle, preview = { SwitchStylePreview(it) }) {
            settings.switchStyle = it
        }
    }
}

/** Cerchio metà chiaro e metà scuro. */
@Composable
fun ThemePreview(key: String) {
    Row(Modifier.size(34.dp).clip(CircleShape)) {
        val light = Color(0xFFF5F5F5)
        val dark = Color(0xFF222222)
        Box(Modifier.weight(1f).fillMaxSize().background(if (key == "dark") dark else light))
        Box(Modifier.weight(1f).fillMaxSize().background(if (key == "light") light else dark))
    }
}

/** Pallino del colore: per "Dal sistema" i veri colori della tavolozza di Android. */
@Composable
fun AccentPreview(key: String) {
    if (key == "system" && Build.VERSION.SDK_INT >= 31) {
        val a1 = colorResource(android.R.color.system_accent1_500)
        val a2 = colorResource(android.R.color.system_accent2_500)
        val a3 = colorResource(android.R.color.system_accent3_500)
        Box(Modifier.size(30.dp).clip(CircleShape).background(Brush.sweepGradient(listOf(a1, a1, a2, a2, a3, a3, a1))))
        return
    }
    val c = accentSwatch(key)
    Box(
        Modifier.size(30.dp).clip(CircleShape).background(
            if (c != null) Brush.linearGradient(listOf(c, c)) else Brush.sweepGradient(
                listOf(Color(0xFFEA4335), Color(0xFFFBBC05), Color(0xFF34A853), Color(0xFF4285F4), Color(0xFFEA4335))
            )
        )
    )
}
