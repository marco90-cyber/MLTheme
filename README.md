# MLTheme

Il tema Material You di **MLauncher**, condiviso da tutte le MLapps: colori, interruttori nei 4 stili, gruppi arrotondati, separé, freccette e righe delle impostazioni.

Ogni app ha **le sue scelte** (tema, colori, stile degli interruttori), salvate in automatico.

## Aggiungerlo a un'app

**1. `settings.gradle.kts`**: aggiungi JitPack ai repository:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**2. `app/build.gradle.kts`**:

```kotlin
dependencies {
    implementation("com.github.marco90-cyber:MLTheme:1.0.0")
}
```

**3. Avvolgi l'app nel tema** (al posto di `MaterialTheme` o del tema dell'app):

```kotlin
setContent {
    MLTheme {
        // tutta l'app
    }
}
```

**4. Nelle impostazioni** aggiungi il gruppo pronto:

```kotlin
MLThemeSettingsGroup()   // Tema · Colori · Stile degli interruttori
```

## I pezzi

| Componente | Cosa fa |
|---|---|
| `MLTheme { }` | Colori Material You + stile interruttori scelto |
| `MLSettingsScaffold(title, onBack) { }` | Pagina impostazioni con barra grande che si richiude |
| `MLGroup("Titolo") { }` | Gruppo con angoli da 28 dp e voci separate da un filo |
| `MLNavRow(title, subtitle, icon) { }` | Voce con freccetta (o `trailing = null` senza) |
| `MLNavSwitch(checked) { }` | Freccetta · separé · interruttore (stile Android 16) |
| `MLSwitchRow(title, subtitle, checked) { }` | Riga con interruttore |
| `MLSliderRow(...)` | Riga con cursore e valore |
| `MLChoiceRow(title, opzioni, attuale) { }` | Scelta a pallini con anteprime |
| `MLDependent(enabled) { }` | Voci in grigio quando quella principale è spenta |
| `MLSwitch`, `MLChevron`, `MLSeparator`, `MLDivider` | I pezzi singoli |
| `MLSearchField` | "Cerca nelle impostazioni" |
| `MLHaptics`, `Motion` | Vibrazione e curve di animazione di MLauncher |

Il valore delle scelte si legge e si cambia da codice con `MLThemeSettings.get(context)`.

## Note

- Da Android 8 in su. Sotto Android 12 "Dal sistema" non c'è: i colori vengono dallo sfondo del telefono.
- Funziona anche con le app su Kotlin 1.9.
- Per aggiornare tutte le app: modifica qui, crea un nuovo tag (es. `1.0.1`), poi cambia il numero di versione nelle app.
- `sample/` è un'app di prova con tutti i componenti: l'APK si scarica dagli artifact di GitHub Actions.
