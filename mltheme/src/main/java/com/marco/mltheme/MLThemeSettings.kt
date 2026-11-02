package com.marco.mltheme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Scelte del tema di questa app (ogni MLapp ha le sue), salvate in automatico.
 * Basta cambiare un valore e tutta l'app si aggiorna:
 *
 *     MLThemeSettings.get(context).switchStyle = "ios"
 */
class MLThemeSettings private constructor(ctx: Context) {
    private val sp = ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    private var _theme by mutableStateOf(sp.getString(K_THEME, "auto") ?: "auto")
    private var _accent by mutableStateOf(sp.getString(K_ACCENT, "system") ?: "system")
    private var _switch by mutableStateOf(sp.getString(K_SWITCH, "android16") ?: "android16")

    /** "auto", "light" o "dark" (vedi [THEME_MODES]). */
    var theme: String
        get() = _theme
        set(v) { _theme = v; sp.edit().putString(K_THEME, v).apply() }

    /** Chiave di [ACCENTS]. */
    var accent: String
        get() = _accent
        set(v) { _accent = v; sp.edit().putString(K_ACCENT, v).apply() }

    /** Chiave di [SWITCH_STYLES]. */
    var switchStyle: String
        get() = _switch
        set(v) { _switch = v; sp.edit().putString(K_SWITCH, v).apply() }

    companion object {
        private const val FILE = "mltheme"
        private const val K_THEME = "theme"
        private const val K_ACCENT = "accent"
        private const val K_SWITCH = "switchStyle"

        @Volatile private var instance: MLThemeSettings? = null

        fun get(ctx: Context): MLThemeSettings =
            instance ?: synchronized(this) { instance ?: MLThemeSettings(ctx).also { instance = it } }
    }
}
