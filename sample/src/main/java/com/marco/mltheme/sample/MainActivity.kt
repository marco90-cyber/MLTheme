package com.marco.mltheme.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.marco.mltheme.MLChoiceRow
import com.marco.mltheme.MLDependent
import com.marco.mltheme.MLGroup
import com.marco.mltheme.MLNavRow
import com.marco.mltheme.MLNavSwitch
import com.marco.mltheme.MLSettingsScaffold
import com.marco.mltheme.MLSliderRow
import com.marco.mltheme.MLSwitchRow
import com.marco.mltheme.MLTheme
import com.marco.mltheme.MLThemeSettingsGroup

/** App di prova: mostra tutti i pezzi di MLTheme. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MLTheme {
                var notif by remember { mutableStateOf(true) }
                var sound by remember { mutableStateOf(true) }
                var vibra by remember { mutableStateOf(false) }
                var size by remember { mutableFloatStateOf(3f) }
                var mode by remember { mutableStateOf("a") }
                MLSettingsScaffold("MLTheme", onBack = null) {
                    MLThemeSettingsGroup()
                    MLGroup("Esempi") {
                        MLNavRow("Voce con sottomenu", "Freccetta a destra", icon = Icons.Filled.Settings) {}
                        MLNavRow(
                            "Notifiche", if (notif) "Attive" else "Spente", icon = Icons.Filled.Notifications,
                            trailing = { MLNavSwitch(notif) { notif = it } }
                        ) {}
                        MLSwitchRow("Notifiche", "Interruttore principale", notif) { notif = it }
                        MLDependent(notif) {
                            MLSwitchRow("Suono", "Grigia se le notifiche sono spente", sound) { sound = it }
                            MLSwitchRow("Vibrazione", null, vibra) { vibra = it }
                        }
                    }
                    MLGroup("Altri controlli") {
                        MLSliderRow("Dimensione", "${size.toInt()}", size, 1f..5f, steps = 3) { size = it }
                        MLChoiceRow("Modalità", listOf("a" to "Prima", "b" to "Seconda", "c" to "Terza"), mode) { mode = it }
                        MLNavRow("Azione senza freccia", null, icon = Icons.Filled.Star, trailing = null) {}
                    }
                }
            }
        }
    }
}
