# AutostartMe

Android app for managing apps that automatically launch on device boot.

## Features

- **Autostart list** – Overview of all apps configured to launch on boot
- **Remove** – Each app can be removed from the list via a trash icon button
- **Add app** – Browse all installed apps and add them to the autostart list
- **Search** – Quickly find apps by name or package name
- **Boot receiver** – Automatically launches configured apps after device boot
- **Permission banner** – Displays missing permissions directly in the app with buttons to grant them

## Screenshots

| Autostart List | Add App |
|:---:|:---:|
| *FirstFragment* | *SecondFragment* |

## Requirements

- Android 13 (API 33) or higher
- Android Studio with AGP 9.0+

## Permissions

| Permission | Purpose |
|---|---|
| `RECEIVE_BOOT_COMPLETED` | Receives the boot signal after device startup |
| `SYSTEM_ALERT_WINDOW` | Allows launching apps from the background (Android 10+) |
| `POST_NOTIFICATIONS` | Shows notifications for failed launches (Android 13+) |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Ensures the boot receiver is not suppressed by battery optimization |

> **Important:** After installation, the permissions **"Display over other apps"** and **"Disable battery optimization"** must be granted manually. The app displays a red banner with direct links to the respective settings as long as permissions are missing.

## Technology

- **Language:** Kotlin
- **UI:** Material Design 3 (Material You)
- **Architecture:** Single-Activity with Navigation Component
- **View Binding:** Enabled
- **Min SDK:** 33 (Android 13)
- **Target SDK:** 36

## Project Structure

```
app/src/main/java/com/example/autostartme/
├── MainActivity.kt            # Main activity with toolbar, FAB and navigation
├── FirstFragment.kt           # Autostart list with permission banner
├── SecondFragment.kt          # App picker with search functionality
├── AutostartPreferences.kt    # SharedPreferences helper for the app list
├── AutostartAppAdapter.kt     # RecyclerView adapter for the autostart list
├── AppPickerAdapter.kt        # RecyclerView adapter for the app picker
└── BootReceiver.kt            # BroadcastReceiver for BOOT_COMPLETED
```

## Build & Installation

```bash
# Clone the repository
git clone https://github.com/huh66/AutostartMe.git

# Open in Android Studio and run on a device/emulator with API 33+
```

Or directly in Android Studio: *File → Open → Select project folder → Run*

## How It Works

1. **Open the app** → The autostart list is displayed (initially empty)
2. **Grant permissions** → Tap the red banner to enable overlay and battery permissions
3. **Add an app** → Press the plus button (FAB) → Select an app from the list
4. **Remove an app** → Tap the trash icon next to the app
5. **Reboot the device** → Configured apps are launched automatically (with a 1.5s delay between launches)

## Documentation

- [README auf Deutsch](README.german.md)

## License

This project is free for personal use.
