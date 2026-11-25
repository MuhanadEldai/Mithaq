# Implementation Plan - Fattish Li (فتش لي) Missing Cars Search App

An Android application designed to search for missing cars in Sudan by Vehicle Identification Number (VIN). Built with Jetpack Compose, Material 3, Firebase Firestore (read-only), Firebase Cloud Messaging (FCM), and Play In-App Updates.

## User Review Required

> [!IMPORTANT]
> - **Firebase Integration**: Firestore is configured in read-only mode to fetch car records (`vin`, `carName`, `reporterPhone`, `status`, `timestamp`) from the `"cars"` collection. No write/upload operations are performed by the client app.
> - **In-App Update**: Uses Google Play In-App Update API (`app-update-ktx`) to prompt users for updates when available.
> - **Telegram-Style Bottom Bar**: A floating, compact, semi-transparent bottom navigation bar positioned cleanly above system navigation buttons.
> - **Consistent Theme**: Deep blue brand color (`#1E3A8A` / `#0047AB`) applied consistently to the status bar and action/top bar across all screens.

## Open Questions

- None. All specifications align with the provided UI mockups and feature requirements (Dark mode, in-app update, Firebase Firestore read-only, FCM notifications, action/status bar consistency, performance/size focus, Telegram-style bottom bar).

## Proposed Changes

### Build Configuration & Dependencies
#### [MODIFY] [libs.versions.toml](file:///home/muhanad/AndroidStudioProjects/fteshly/gradle/libs.versions.toml)
- Add Firebase BOM, Firestore KTX, Messaging KTX, and Play App Update KTX versions and libraries.

#### [MODIFY] [app/build.gradle.kts](file:///home/muhanad/AndroidStudioProjects/fteshly/app/build.gradle.kts)
- Enable R8 code shrinking and resource optimization in release build type.
- Add dependencies for Firebase BOM, Firestore, Messaging, and Play App Update.

### Core Data & Services
#### [NEW] [CarRepository.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/data/CarRepository.kt)
- Read-only Firestore repository querying the `"cars"` collection by VIN. Includes offline/mock fallback support for seamless testing.

#### [NEW] [FCMService.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/data/FCMService.kt)
- Firebase Messaging Service handling incoming push notifications.

#### [NEW] [AppUpdateManagerHelper.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/util/AppUpdateManagerHelper.kt)
- Helper class managing Google Play In-App Updates (immediate/flexible flow).

### Theme & Localization
#### [MODIFY] [Color.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/ui/theme/Color.kt)
- Define light and dark color schemes matching the deep blue brand aesthetic (`#1E3A8A`, `#0047AB`, surface colors, dark mode background `#121212`).

#### [MODIFY] [Theme.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/ui/theme/Theme.kt)
- Update `FteshlyTheme` to support dynamic dark/light mode toggle and consistent status bar coloring.

#### [NEW] [strings.xml (AR/EN)](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/res/values/strings.xml)
- Add Arabic and English string resources for app title ("فتش لي / Fattish Li"), search prompts, settings, and about info.

### UI Screens & Navigation
#### [NEW] [MainScreen.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/ui/main/MainScreen.kt)
- Root Composable implementing:
  - Telegram-style floating semi-transparent bottom bar.
  - Consistent Top Action Bar and Status Bar across all tabs.
  - Screens:
    1. **Home / Dashboard**: Quick stats (35,000+ cars), instructions card, quick search entry.
    2. **Search Screen**: VIN input field (`12345678`), search action, loading state.
    3. **Search Results Screen**: Match found view with reporter phone number (`0912345678`), WhatsApp contact button, privacy warning.
    4. **About Screen**: App description, support email (`support@fattishli.sd`), copyright 2026.
    5. **Settings Screen**: Language switch (Arabic/English), Dark/Light mode toggle, Notifications toggle, Privacy Policy, Help & Support, Share App, About.

#### [MODIFY] [MainActivity.kt](file:///home/muhanad/AndroidStudioProjects/fteshly/app/src/main/java/com/muhanad/fteshly/MainActivity.kt)
- Wire up `MainScreen`, edge-to-edge window insets, app update check, and theme state persistence.

## Verification Plan

### Automated Tests
- Run unit tests for repository and state management.
- Build project using `gradle_build` (`app:assembleDebug`) to verify zero compilation errors and clean R8 configuration.

### Manual Verification
- Deploy app to emulator/device (`deploy`).
- Verify dark/light mode switching, bottom navigation bar transparency, consistent top bar colors across screens, and VIN search functionality.
