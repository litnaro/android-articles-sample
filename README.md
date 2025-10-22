# android-articles-sample
A sample Android application that displays the **most popular articles** from the [New York Times API](https://developer.nytimes.com/get-started]) .  
Built using **Jetpack Compose**, **Clean Architecture**, **MVVM**, and **Hilt**.

## Tech Stack

- Kotlin
- Jetpack Compose
- Hilt (DI)
- Retrofit
- Coroutines / Flow
- Navigation Compose
- Unit & Instrumented Tests (JUnit, Espresso)

## Architecture Overview
- **data** — DTOs, API services, repositories
- **domain** — entities, use cases, exceptions
- **presentation** — ViewModels, composables, navigation
- **di** — Hilt dependency injection modules

## Build & Run

### From Android Studio
1. Open the project in **Android Studio Ladybug** or newer.
2. Select the `app` configuration.
3. Click **Run** to build and launch the app.

### API Key Setup
The app uses the New York Times Most Popular API.
Add your API key to local.properties (in the project root):
```bash
NYTIMES_API_KEY=your_real_api_key
```

### From Command Line
```bash
./gradlew clean assembleDebug
```
The APK will be generated in:
app/build/outputs/apk/debug/

## Running Tests

### Unit Tests
```bash
./gradlew test
```
Report: app/build/reports/tests/testDebugUnitTest/index.html

### UI Tests
```bash
./gradlew connectedDebugAndroidTest
```
Report: app/build/reports/androidTests/connected/debug/index.html

## Code Coverage

### Unit Test Coverage
```bash
./gradlew jacocoTestReport
```
Report: app/build/reports/jacoco/jacocoTestReport/html/index.html

### Instrumented Coverage
```bash
./gradlew createDebugCoverageReport
```
Report: app/build/reports/coverage/androidTest/debug/connected/index.html



