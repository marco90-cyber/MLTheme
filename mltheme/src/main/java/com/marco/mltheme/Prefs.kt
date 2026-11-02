package com.marco.mltheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------
//  Componenti delle impostazioni, presi da MLauncher
// ---------------------------------------------------------------------

/** Trasparenza delle voci disattivate. */
const val DISABLED_ALPHA = 0.38f

/** Colori delle righe: sfondo a "scheda", testi in grigio se la voce è disattivata. */
@Composable
fun mlPrefColors(): ListItemColors {
    val cs = MaterialTheme.colorScheme
    return if (LocalPrefEnabled.current) ListItemDefaults.colors(containerColor = cs.surfaceContainer)
    else ListItemDefaults.colors(
        containerColor = cs.surfaceContainer,
        headlineColor = cs.onSurface.copy(alpha = DISABLED_ALPHA),
        supportingColor = cs.onSurfaceVariant.copy(alpha = DISABLED_ALPHA),
        leadingIconColor = cs.onSurfaceVariant.copy(alpha = DISABLED_ALPHA),
        trailingIconColor = cs.onSurfaceVariant.copy(alpha = DISABLED_ALPHA)
    )
}

/**
 * Pagina delle impostazioni: barra grande che si richiude scorrendo, freccia indietro,
 * contenuto che scorre con i gruppi distanziati.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MLSettingsScaffold(
    title: String,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val scroll = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        containerColor = cs.surface,
        modifier = modifier.nestedScroll(scroll.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    if (onBack != null) IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = actions,
                scrollBehavior = scroll,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = cs.surface,
                    scrolledContainerColor = cs.surface
                )
            )
        }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
            Spacer(Modifier.height(16.dp))
        }
    }
}

/** Campo "Cerca nelle impostazioni" arrotondato. */
@Composable
fun MLSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Cerca nelle impostazioni"
) {
    val cs = MaterialTheme.colorScheme
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (value.isNotEmpty()) IconButton(onClick = { onValueChange("") }) {
                Icon(Icons.Filled.Close, contentDescription = "Cancella")
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = cs.surfaceContainerHigh,
            unfocusedContainerColor = cs.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

/** Gruppo a elementi separati da un filo di spazio, con gli angoli esterni molto arrotondati. */
@Composable
fun MLGroup(title: String? = null, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier) {
        if (title != null) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 32.dp, top = 6.dp, bottom = 8.dp)
            )
        }
        Column(
            Modifier.padding(horizontal = 16.dp).clip(RoundedCornerShape(28.dp)),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) { content() }
    }
}

/**
 * Voci che dipendono da un'altra impostazione: con [enabled] false restano visibili ma in grigio
 * e non si possono toccare (il loro valore resta quello di prima).
 */
@Composable
fun MLDependent(enabled: Boolean, content: @Composable () -> Unit) {
    Box {
        CompositionLocalProvider(LocalPrefEnabled provides enabled) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) { content() }
        }
        if (!enabled) {
            Box(
                Modifier.matchParentSize().pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
                    }
                }
            )
        }
    }
}

/** Freccia che indica un sottomenu (stile Android 16). */
@Composable
fun MLChevron() {
    Icon(
        Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (LocalPrefEnabled.current) 1f else DISABLED_ALPHA)
    )
}

/** Separé verticale tra la freccia e l'interruttore. */
@Composable
fun MLSeparator(modifier: Modifier = Modifier) {
    Box(
        modifier.padding(horizontal = 12.dp).width(1.dp).height(32.dp)
            .background(MaterialTheme.colorScheme.outline)
    )
}

/** Linea orizzontale sottile, per separare voci dentro una scheda. */
@Composable
fun MLDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

/** Freccia | separé | interruttore: per una voce che apre un sottomenu e si accende anche da qui. */
@Composable
fun MLNavSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MLChevron()
        MLSeparator()
        MLSwitch(checked = checked, onCheckedChange = onChange)
    }
}

/** Icona tonda colorata a sinistra delle voci principali. */
@Composable
fun MLRowIcon(icon: ImageVector) {
    val cs = MaterialTheme.colorScheme
    Box(
        Modifier.alpha(if (LocalPrefEnabled.current) 1f else DISABLED_ALPHA)
            .size(44.dp).clip(CircleShape).background(cs.primaryContainer),
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = null, tint = cs.onPrimaryContainer) }
}

/**
 * Voce che apre un sottomenu o fa un'azione.
 * [trailing] di serie è la freccetta; passa null per nessun elemento a destra,
 * oppure { MLNavSwitch(...) } per freccia + separé + interruttore.
 */
@Composable
fun MLNavRow(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    trailing: (@Composable () -> Unit)? = { MLChevron() },
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = if (subtitle != null) ({ Text(subtitle) }) else null,
        leadingContent = if (icon != null) ({ MLRowIcon(icon) }) else null,
        trailingContent = trailing,
        colors = mlPrefColors(),
        modifier = modifier.clickable(onClick = onClick)
    )
}

/** Riga con interruttore: si tocca tutta la riga. */
@Composable
fun MLSwitchRow(
    title: String,
    subtitle: String?,
    checked: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = if (subtitle != null) ({ Text(subtitle) }) else null,
        leadingContent = if (icon != null) ({ MLRowIcon(icon) }) else null,
        trailingContent = { MLSwitch(checked = checked, onCheckedChange = onChange) },
        colors = mlPrefColors(),
        modifier = modifier.clickable { onChange(!checked) }
    )
}

/** Riga con cursore e valore scritto a destra (più un'anteprima facoltativa). */
@Composable
fun MLSliderRow(
    title: String,
    valueLabel: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    modifier: Modifier = Modifier,
    preview: (@Composable () -> Unit)? = null,
    onChange: (Float) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier
            .fillMaxWidth()
            .background(cs.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        val en = LocalPrefEnabled.current
        val dim = if (en) 1f else DISABLED_ALPHA
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(title, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = cs.onSurface.copy(alpha = dim))
            Text(valueLabel, color = cs.primary.copy(alpha = dim), fontWeight = FontWeight.SemiBold)
            if (preview != null) {
                Spacer(Modifier.width(12.dp))
                Box(Modifier.alpha(dim)) { preview() }
            }
        }
        Slider(value = value, onValueChange = onChange, valueRange = range, steps = steps, enabled = en)
    }
}

/** Riga di scelta: mostra il valore attuale e apre un elenco a pallini, con anteprime facoltative. */
@Composable
fun MLChoiceRow(
    title: String,
    options: List<Pair<String, String>>,
    current: String,
    modifier: Modifier = Modifier,
    preview: (@Composable (String) -> Unit)? = null,
    onSelect: (String) -> Unit
) {
    var open by remember { mutableStateOf(false) }
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(options.firstOrNull { it.first == current }?.second ?: "") },
        trailingContent = if (preview != null) ({
            Box(Modifier.alpha(if (LocalPrefEnabled.current) 1f else DISABLED_ALPHA)) { preview(current) }
        }) else null,
        colors = mlPrefColors(),
        modifier = modifier.clickable { open = true }
    )
    if (open) {
        AlertDialog(
            onDismissRequest = { open = false },
            title = { Text(title) },
            text = {
                Column {
                    options.forEach { (k, l) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(k)
                                    open = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = k == current, onClick = null)
                            Spacer(Modifier.width(12.dp))
                            if (preview != null) {
                                preview(k)
                                Spacer(Modifier.width(12.dp))
                            }
                            Text(l)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { open = false }) { Text("Annulla") } }
        )
    }
}
