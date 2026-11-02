package com.marco.mltheme

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

/** Feedback tattile delle MLapps, disattivabile dalle impostazioni dell'app. */
object MLHaptics {
    @Volatile var enabled: Boolean = true

    /** Leggero: selezione, passo di un cursore, cambio pagina. */
    fun tick(v: View) {
        if (enabled) v.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    /** Pressione lunga, inizio trascinamento. */
    fun press(v: View) {
        if (enabled) v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    /** Azione riuscita. */
    fun confirm(v: View) {
        if (!enabled) return
        v.performHapticFeedback(if (Build.VERSION.SDK_INT >= 30) HapticFeedbackConstants.CONFIRM else HapticFeedbackConstants.VIRTUAL_KEY)
    }

    /** Azione non possibile. */
    fun reject(v: View) {
        if (!enabled) return
        v.performHapticFeedback(if (Build.VERSION.SDK_INT >= 30) HapticFeedbackConstants.REJECT else HapticFeedbackConstants.LONG_PRESS)
    }
}
