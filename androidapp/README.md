# Seed QR Validator

A modern Android application for generating and validating QR codes using random seeds from a backend service. Built with Kotlin and following clean architecture principles.

## Features

- 🎯 Generate QR codes using random seeds from API
- 📷 Scan QR codes using device camera
- ✅ Validate scanned QR codes against backend
- 💾 Offline support with local caching
- 🎨 Material Design 3 UI

## Architecture

The app follows Clean Architecture principles with a modular approach:

```
com.juandgaines.seedqrvalidator/
├── core/                          # Core functionality
│   ├── data/                      # Data layer (Repository implementations, API, Database)
│   ├── domain/                    # Domain layer (Models, Repository interfaces)
│   └── presentation/              # Common UI components and utilities
├── generator/                     # QR Code Generation feature
│   ├── data/
│   ├── domain/
│   └── presentation/
├── scanner/                       # QR Code Scanning feature
│   ├── data/
│   ├── domain/
│   └── presentation/
└── home/                         # Home screen feature
    ├── data/
    ├── domain/
    └── presentation/
```

### Tech Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVI + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit + Kotlinx Serialization
- **Camera**: CameraX
- **QR Scanning**: ML Kit Vision
- **Testing**: JUnit, Truth, Turbine, MockWebServer
- **Concurrency**: Kotlin Coroutines + Flow

## Setup

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 17
- Android SDK 34
- Kotlin 2.0.0+

### Building the Project

1. Open the project in Android Studio
2. Replace your server address if neccessary on the build.gradle.kts from app level
3. Sync project with Gradle files
4. Run the app on an emulator or physical device

## API Integration

The app integrates with the following endpoints:

### Get Seed
```http
GET /v1/seed
Response:
{
    "seed": "5440b58a257f4f759df40eb4dbbec666",
    "expires_at": "2025-05-15T23:00:15.471590097Z"
}
```

### Validate Seed
```http
GET /v1/seed/validate/{seed}
Response: 204 No Content (Success)
```

## Testing

The project includes comprehensive unit tests for the repository layer:

- `QRGeneratorRepositoryImplTest`: Tests QR code generation
- `ScannerRepositoryImplTest`: Tests QR code validation
- `HomeRepositoryImplTest`: Tests seed management
- `SeedDaoFake`: Mock implementation for database testing

Run tests using:
```bash
./gradlew test
```

## Project Structure

### Key Components

- `MainActivity`: Entry point and navigation setup
- `SeedValidatorApp`: Application class with Hilt setup
- `core/`: Shared components and utilities
- `generator/`: QR code generation feature
- `scanner/`: QR code scanning feature
- `home/`: Main screen and seed management

### Dependencies

Dependencies are managed through Version Catalogs in `gradle/libs.versions.toml`:

- AndroidX components
- Jetpack Compose
- CameraX
- ML Kit Vision
- Room Database
- Retrofit + OkHttp
- Hilt
- Testing libraries


## Acknowledgments

- Material Design 3 for UI guidelines
- ML Kit for QR code scanning
- CameraX for camera implementation 