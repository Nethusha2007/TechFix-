# TechFix — Android Repair Management System

TechFix is a native Android application for managing electronic device repairs, built for a Sri Lankan
repair-shop chain with branches in Colombo, Galle and Kandy. It ships as a single app containing two
experiences: a Customer app (book and track repairs, pay invoices) and an Admin app (manage
appointments, technicians and view business reports). All data is stored locally on the device using
Room/SQLite — there is no server or network back end, so the app runs fully offline.

The UI is built entirely with Jetpack Compose and Material 3, following the supplied design system
(TechFix blue palette, rounded cards, custom charts).

---

## Tech stack

- Language: Kotlin
- UI: Jetpack Compose + Material 3 (single-Activity architecture)
- Architecture: MVVM with a lightweight, dependency-free manual DI container (`AppContainer`)
- Local database: Room (SQLite) with KSP-generated DAOs
- Navigation: Navigation-Compose with one app-level `NavHost`
- Images: Coil (`AsyncImage`)
- Async: Kotlin Coroutines + `StateFlow`
- Charts: hand-drawn with Compose `Canvas` and layout primitives (no third-party chart library)

### Build configuration

| Setting | Value |
|---|---|
| `minSdk` | 26 (Android 8.0) |
| `compileSdk` / `targetSdk` | 34 |
| JDK | 17 |
| Gradle | 8.7 |
| Android Gradle Plugin | 8.5.x |
| Kotlin | 1.9.24 |
| Compose BOM | 2024.06.00 |
| Compose compiler | 1.5.14 |
| Room | 2.6.1 |

---

## How to build and run

1. Open in Android Studio — Koala (2024.1.1) or newer is recommended because the project uses
   Android Gradle Plugin 8.5.x. Any 2024+ Android Studio release (Ladybug, Meerkat, etc.) should work.
   Choose *Open* and select the `TechFix-Repair-System` folder (the one containing `settings.gradle.kts`).
2. Let Gradle sync. Android Studio downloads Gradle 8.7 and all dependencies automatically on first
   sync. (This project is shipped as source without the committed Gradle wrapper JAR; Android Studio
   regenerates it for you. If you build from the command line instead, run `gradle wrapper --gradle-version 8.7`
   once to create `gradlew`, then use `./gradlew assembleDebug`.)
3. Run on an emulator or device running Android 8.0+ (API 26+). Press Run ▶ in Android Studio or
   use `./gradlew installDebug` from the command line.
4. On first launch the database seeds itself with demo branches, services, technicians, users and a few
   sample appointments, so every screen has realistic content immediately.

A short quick-start with troubleshooting notes is in [`docs/SETUP.md`](docs/SETUP.md).

---

## Demo login credentials

The database is pre-seeded with these accounts:

| Role | Email | Password |
|---|---|---|
| Admin | `admin@techfix.lk` | `admin123` |
| Customer (John Silva) | `john.silva@email.com` | `password123` |

From the customer login screen, tap **Admin Portal** (or the admin link) to reach the admin login.
You can also register a brand-new customer account from the Sign-Up screen.

---

## Features

### Customer app
- Onboarding & auth — splash routing, onboarding carousel, login, registration and a forgot-password flow.
- Home dashboard — greeting, active-repair card with live status, quick actions, service categories and recent appointments.
- Book a repair — a guided multi-step flow: choose device category → brand/model → service → describe the problem (with optional photos) → pick a branch → pick date & time → review and confirm booking.
- Track repairs — a vertical timeline of the repair stages (Appointment Confirmed → Collected) with per-stage updates and technician notes.
- Appointments & history — filterable list of current appointments and a history of completed/cancelled repairs.
- Payments — invoice summary, choice of payment method (Cash, Card, Bank Transfer, eZ Cash, mCash), a simulated payment flow, and a success screen with a generated transaction ID. Amounts are shown in LKR (Sri Lankan Rupees).
- Branches — branch directory with details and a schematic map view.
- Profile & settings — view/edit profile, notifications, help & support, and app settings.

### Admin app
- Dashboard — total revenue, appointment counts by status, a custom donut chart status overview, quick links, and recent appointments.
- Appointments — every booking across all branches, filterable by status and branch.
- Appointment detail — assign/reassign a technician, advance the repair stage (which notifies the customer and writes a timeline entry), view the customer's problem description and photos, and change appointment metadata.
- Technicians — team roster with per-technician availability toggles and basic workload indicators.
- Reports — revenue, average ticket, completion rate, and custom bar charts for appointments-by-status and repairs-by-device-type.

---

## Project structure

```
TechFix-Repair-System/
├── build.gradle.kts            # Root Gradle config
├── settings.gradle.kts
├── gradle.properties
├── app/
│   ├── build.gradle.kts        # App module config & dependencies
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/                # Themes, colors, strings, launcher icon, backup rules
│       └── java/com/techfix/repairsystem/
│           ├── TechFixApplication.kt   # Application class; owns the AppContainer
│           ├── MainActivity.kt         # Single Activity → TechFixTheme → TechFixApp()
│           ├── data/
│           │   ├── model/              # Domain enums (DeviceCategory, RepairStage, …)
│           │   ├── local/
│           │   │   ├── entity/         # Room @Entity data classes
│           │   │   ├── dao/            # Room DAOs
│           │   │   ├── Converters.kt   # Room type converters
│           │   │   └── TechFixDatabase.kt
│           │   ├── repository/         # One repository per aggregate
│           │   ├── SessionManager.kt   # Tracks the logged-in user/role
│           │   ├── AppContainer.kt     # Manual DI graph
│           │   └── DatabaseSeeder.kt   # Seeds demo data on first launch
│           └── ui/
│               ├── theme/              # Color, Type, Theme
│               ├── components/         # Shared Compose components & icon mapping
│               ├── navigation/         # Routes.kt + TechFixApp.kt (the single NavHost)
│               ├── AppViewModelProvider.kt
│               ├── auth/               # Splash, onboarding, login, register, forgot-password
│               ├── customer/           # Home, appointments, history, profile, branches, …
│               ├── booking/            # Book-repair flow + ViewModel
│               ├── repair/             # Tracking, details, gallery + ViewModel
│               ├── payment/            # Summary, method, success, payments list + ViewModels
│               └── admin/              # Dashboard, appointments, detail, technicians, reports
└── README.md
```

---

## Architecture notes
- There is exactly one `NavHost` (in `ui/navigation/TechFixApp.kt`). It owns every route and switches the bottom navigation bar between the customer tabs and the admin tabs based on the current destination and user role.
- Each screen gets its ViewModel via a decentralized factory: `viewModel(factory = SomeViewModel.Factory)`. The factory reaches the repositories through the `AppContainer` held by `TechFixApplication`.
- Screens observe immutable `UiState` data classes exposed as `StateFlow`, collected with `collectAsStateWithLifecycle()`.

---

## Notes & limitations

- Offline / academic scope: This is a self-contained demo. There is no server, no real payment gateway (payments are simulated), and the branch map is a styled schematic rather than a live Google Maps view.
- Passwords are stored in plain text in the local database purely because this is an offline academic project with no back end. Do not reuse this approach in production — always hash and salt credentials and follow standard security practices.
- Seed data is inserted only when the database is empty. To re-seed, clear the app's storage (or uninstall/reinstall).
- All monetary values use Sri Lankan Rupees (LKR).

---

If you'd like any additional changes (for example: add badges, contribution guidelines, a license, or CI status), tell me what you'd like and I will update the README accordingly.
