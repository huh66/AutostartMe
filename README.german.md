# AutostartMe

Android-App zum Verwalten von Apps, die beim Gerätestart automatisch gestartet werden.

## Features

- **Autostart-Liste** – Übersicht aller Apps, die beim Hochfahren gestartet werden
- **Löschen** – Jede App kann einzeln per Papierkorb-Button aus der Liste entfernt werden
- **App hinzufügen** – Alle installierten Apps durchsuchen und zur Autostart-Liste hinzufügen
- **Suchfunktion** – Schnelles Finden von Apps nach Name oder Paketname
- **Boot-Receiver** – Startet die konfigurierten Apps automatisch nach dem Gerätestart
- **Berechtigungs-Banner** – Zeigt fehlende Berechtigungen direkt in der App an mit Buttons zum Erteilen

## Screenshots

| Autostart-Liste | App hinzufügen |
|:---:|:---:|
| *FirstFragment* | *SecondFragment* |

## Voraussetzungen

- Android 13 (API 33) oder höher
- Android Studio mit AGP 9.0+

## Berechtigungen

| Berechtigung | Zweck |
|---|---|
| `RECEIVE_BOOT_COMPLETED` | Empfängt das Boot-Signal nach dem Gerätestart |
| `SYSTEM_ALERT_WINDOW` | Ermöglicht das Starten von Apps aus dem Hintergrund (Android 10+) |
| `POST_NOTIFICATIONS` | Zeigt Benachrichtigungen bei fehlgeschlagenen Starts (Android 13+) |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Stellt sicher, dass der Boot-Receiver nicht durch Akkuoptimierung unterdrückt wird |

> **Wichtig:** Nach der Installation müssen die Berechtigungen **"Über anderen Apps anzeigen"** und **"Akkuoptimierung deaktivieren"** manuell erteilt werden. Die App zeigt ein rotes Banner mit direkten Links zu den Einstellungen, solange Berechtigungen fehlen.

## Technologie

- **Sprache:** Kotlin
- **UI:** Material Design 3 (Material You)
- **Architektur:** Single-Activity mit Navigation Component
- **View Binding:** Aktiviert
- **Min SDK:** 33 (Android 13)
- **Target SDK:** 36

## Projektstruktur

```
app/src/main/java/com/example/autostartme/
├── MainActivity.kt            # Haupt-Activity mit Toolbar, FAB und Navigation
├── FirstFragment.kt           # Autostart-Liste mit Berechtigungs-Banner
├── SecondFragment.kt          # App-Auswahl mit Suchfunktion
├── AutostartPreferences.kt    # SharedPreferences-Helper für die App-Liste
├── AutostartAppAdapter.kt     # RecyclerView-Adapter für die Autostart-Liste
├── AppPickerAdapter.kt        # RecyclerView-Adapter für die App-Auswahl
└── BootReceiver.kt            # BroadcastReceiver für BOOT_COMPLETED
```

## Build & Installation

```bash
# Repository klonen
git clone https://github.com/huh66/AutostartMe.git

# In Android Studio öffnen und auf einem Gerät/Emulator mit API 33+ ausführen
```

Oder direkt in Android Studio: *File → Open → Projektordner auswählen → Run*

## Funktionsweise

1. **App öffnen** → Die Autostart-Liste wird angezeigt (anfangs leer)
2. **Berechtigungen erteilen** → Rotes Banner antippen, um Overlay- und Akku-Berechtigung zu aktivieren
3. **App hinzufügen** → Plus-Button (FAB) drücken → App aus der Liste auswählen
4. **App entfernen** → Papierkorb-Icon neben der App antippen
5. **Gerät neustarten** → Die konfigurierten Apps werden automatisch gestartet (mit 1,5s Verzögerung zwischen den Starts)

## Dokumentation

- [README in English](README.md)

## Lizenz

Dieses Projekt ist frei zur persönlichen Nutzung.
