# 📱 TechFix — Android Repair Management System

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Room SQLite](https://img.shields.io/badge/Room_SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Academic Project](https://img.shields.io/badge/NIBM-Coursework-blue?style=for-the-badge)

**TechFix** is a native Android application designed for managing electronic device repairs, built specifically for a Sri Lankan repair-shop chain with branches in Colombo and Galle. 

This project ships as a **single application containing two distinct experiences**:
1. **🧑‍💻 Customer App:** Book and track repairs, view branch details, and pay invoices.
2. **🛡️ Admin App:** Manage appointments, technician assignments, and view business reports.

> **Note:** This is a fully offline system built for academic demonstration. All data is stored locally on the device using Room/SQLite. There is no external server or network backend.

---

## 🚀 Tech Stack & Architecture

The UI is built entirely with **Jetpack Compose** and **Material 3**, adhering strictly to a custom design system (TechFix blue palette, rounded cards, and custom canvas charts).

*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose + Material 3 (Single-Activity Architecture)
*   **Architecture:** MVVM with a lightweight, dependency-free manual DI container (`AppContainer`)
*   **Local Database:** Room (SQLite) with KSP-generated DAOs
*   **Navigation:** Navigation-Compose with one app-level `NavHost`
*   **Image Loading:** Coil (`AsyncImage`)
*   **Asynchrony:** Kotlin Coroutines + `StateFlow`
*   **Data Visualization:** Hand-drawn with Compose `Canvas` (No third-party chart libraries)

### ⚙️ Build Configuration

| Setting | Value |
| :--- | :--- |
| **minSdk** | 26 (Android 8.0) |
| **compileSdk / targetSdk** | 34 |
| **JDK** | 17 |
| **Gradle / AGP** | 8.7 / 8.5.x |
| **Kotlin** | 1.9.24 |
| **Compose BOM** | 2024.06.00 |
| **Room** | 2.6.1 |

---

## 🛠️ How to Build and Run

1. **Open in Android Studio:** Koala (2024.1.1) or newer is recommended. Choose *Open* and select the `TechFix-Repair-System` folder.
2. **Sync Gradle:** Let Android Studio download Gradle 8.7 and all dependencies automatically on the first sync. 
    * *CLI Alternative:* Run `gradle wrapper --gradle-version 8.7` then `./gradlew assembleDebug`.
3. **Run the App:** Launch on an emulator or physical device running Android 8.0+ (API 26+).
4. **Initial Data Seeding:** On the first launch, the database automatically seeds itself with demo branches, services, technicians, users, and sample appointments so the app is immediately testable.

*For troubleshooting, refer to [`docs/SETUP.md`](docs/SETUP.md).*

---

## 🔑 Demo Login Credentials

The database is pre-seeded with the following accounts for easy testing. *From the customer login screen, tap **Admin Portal** to reach the management side.*

| Role | Email | Password |
| :--- | :--- | :--- |
| **Admin** | `admin@techfix.lk` | `admin123` |
| **Customer (John Silva)** | `john.silva@email.com` | `password123` |

---

## ✨ Key Features

### 🧑‍💻 Customer Experience
*   **🔐 Onboarding & Auth:** Splash routing, animated onboarding carousel, login, registration, and forgot-password flows.
*   **🏠 Home Dashboard:** Personalized greeting, live active-repair tracking card, quick actions, and recent appointments.
*   **📅 Book a Repair:** Guided multi-step flow (Category → Brand/Model → Service → Problem Description w/ Photos → Branch Selection → Date & Time → Confirm).
*   **🔍 Track Repairs:** Vertical timeline of repair stages (from *Appointment Confirmed* to *Collected*) with real-time technician notes.
*   **💳 Payments:** Invoice summary with simulated payment processing (Cash, Card, Bank Transfer, eZ Cash, mCash). Amounts displayed in LKR (Rs.).
*   **📍 Branches:** Branch directory with details and a schematic UI map view.

### 🛡️ Admin / Management Experience
*   **📊 Dashboard:** High-level metrics including total revenue, appointment counts by status, custom donut charts, and quick links.
*   **📋 Appointments:** Global booking view across all branches, filterable by status and location.
*   **🔧 Appointment Detail & Workflow:** Assign technicians, advance repair stages (triggers timeline updates for the customer), view customer photos, and edit metadata.
*   **👷 Technicians:** Team roster with availability toggles and workload indicators.
*   **📈 Reports:** Revenue tracking, average ticket costs, completion rates, and custom bar charts (e.g., repairs by device type).

---

## 🏗️ Project Structure

```text
TechFix-Repair-System/
├── app/
│   ├── build.gradle.kts        
│   └── src/main/java/com/techfix/repairsystem/
│       ├── TechFixApplication.kt   # Application class; owns AppContainer
│       ├── MainActivity.kt         # Single Activity entry point
│       ├── data/                   # Data layer (Room DAOs, Entities, Repositories)
│       │   ├── local/              # SQLite database configurations
│       │   ├── repository/         # Aggregate repositories
│       │   └── DatabaseSeeder.kt   # Demo data initialization
│       └── ui/                     # Presentation layer
│           ├── theme/              # Compose Theme, Colors, Typography
│           ├── components/         # Reusable Material 3 widgets
│           ├── navigation/         # NavHost and routing configuration
│           ├── auth/               # Authentication screens
│           ├── customer/           # Customer portal screens
│           ├── admin/              # Admin dashboard and management screens
│           ├── booking/            # Complex booking flow
│           └── repair/             # Tracking and gallery screens
└── README.md
