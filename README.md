# PlanA - Daily Expenditure Monitoring

PlanA is an offline-first personal finance tracker built with Kotlin, Jetpack Compose (Material 3), and Room. It focuses on fast daily logging, clear daily summaries, and strong budgeting foundations.

## Features
- Daily dashboard: today/yesterday totals, 7-day trend, top categories.
- Quick add transactions (2–3 taps).
- Transactions list with sticky date headers.
- Categories, accounts, budgets, and recurring rules modeled for growth.
- Settings: currency + hide amounts toggle.
- Offline-first: Room is the single source of truth.

## Tech Stack
- Kotlin, Jetpack Compose, Navigation Compose
- Room + Coroutines + Flow
- MVVM + Clean Architecture (data/domain/presentation)
- Hilt for dependency injection
- DataStore Preferences for settings

## Project Structure
```
app/src/main/java/com/example/plana
├── data
│   ├── local (Room entities, DAOs, DB, converters)
│   ├── preferences (DataStore)
│   └── repository (implementations + mappers)
├── domain
│   ├── model
│   ├── repository (interfaces)
│   └── usecase
├── presentation
│   ├── navigation
│   ├── screens
│   ├── state
│   └── ViewModels
└── di (Hilt modules)
```

## Setup
1. Open the project in Android Studio (Hedgehog+).
2. Sync Gradle.
3. Run on an emulator or device (minSdk 24).

## Debug Seed Data
When `BuildConfig.DEBUG` is enabled, the database seeds a few starter categories and accounts on first launch.

## Room Migrations
The database is currently at version 1. Future schema changes should include:
- A new migration object in `data/local/Migrations.kt` (planned).
- Updated `PlanADatabase` version.
- Automated tests for migration behavior.

## Testing
- Unit tests: use cases (budget progress).
- Instrumented tests: DAO queries (in-memory Room).

## Next Improvements
- Full budgets UI and alerts
- Analytics charts (Vico)
- Recurring transaction engine
- CSV export + backup/restore via SAF
- App lock (PIN/biometric)
