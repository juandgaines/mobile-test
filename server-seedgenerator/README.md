# Seed Generator Service

A Spring Boot application written in Kotlin that provides a secure seed generation and validation service.

## 🚀 Overview

This service provides endpoints for generating unique seeds, validating them, and managing their lifecycle. It's built using Spring Boot and Kotlin, offering a robust and type-safe implementation.

## 📁 Project Structure

```
src/main/kotlin/com/juadgaines/seedgenerator/
├── SeedgeneratorApplication.kt
├── controller
│   └── SeedController.kt
├── domain
│   └── SeedDto.kt
└── services
    └── SeedService.kt
```


## 🔍 API Endpoints

### SeedController (`/v1/seed`)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/v1/seed` | GET | Generates a new seed |
| `/v1/seed/validate/{seed}` | GET | Validates a given seed |
| `/v1/seed/clear` | GET | Clears all stored seeds |

## 💻 Core Components

### SeedService

The `SeedService` class manages seed generation and validation with the following features:

- Uses `ConcurrentHashMap` for thread-safe seed storage
- Generates UUID-based seeds
- Sets 5-minute expiration for seeds
- Validates seeds against storage and expiration time

```kotlin
fun generateSeed(): SeedDto {
    val seedValue = UUID.randomUUID().toString().replace("-", "")
    val expiration = ZonedDateTime.now().plusMinutes(5)
    seedStorage[seedValue] = expiration
    return SeedDto(seed = seedValue, expires_at = expiration)
}
```

## 🚀 Heroku Deployment Guide

### Prerequisites

- [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)
- Heroku account
- Git installed

### Required Files

1. **Procfile**
```
web: java -Dserver.port=$PORT -jar build/libs/seedgenerator-0.0.1-SNAPSHOT.jar
```

2. **system.properties**
```
java.runtime.version=17
```

### Deployment Steps

1. **Login to Heroku**
```bash
heroku login
```

2. **Create Heroku App**
```bash
heroku create seed-server-1
```

3. **Set Buildpack**
```bash
heroku buildpacks:clear --app seed-server-1
heroku buildpacks:set heroku/gradle --app seed-server-1
```

4. **Build the Application**
```bash
./gradlew clean build
```

5. **Deploy using Git Subtree**
```bash
git subtree push --prefix server heroku main
```

6. **Monitor Deployment**
```bash
heroku logs --tail --app seed-server-1
```

7. **Open the Application**
```bash
heroku open --app seed-server-1
```

## 🔄 Redeployment

After making changes:

```bash
git add .
git commit -m "Update backend"
git subtree push --prefix server heroku main
```

## 🔒 Security Note

Seeds are stored in memory and expire after 5 minutes for security purposes. The service uses thread-safe storage to handle concurrent requests. 