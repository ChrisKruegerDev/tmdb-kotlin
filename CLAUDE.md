# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform library for The Movie Database (TMDB) API. It provides comprehensive access to TMDB's v3 and v4 APIs with a coroutine-based interface.

## Essential Commands

```bash
# Build the project
./gradlew assemble

# Run tests (JVM platform)
./gradlew jvmTest

# Run linting
./gradlew spotlessCheck

# Apply linting fixes
./gradlew spotlessApply

# Publish to Maven Central (requires credentials)
./gradlew publish

# Check for dependency updates
./gradlew dependencyUpdates
```

## Architecture Overview

The project uses Kotlin Multiplatform to support JVM, JS, and iOS targets:

- **Entry Points**: `Tmdb3` and `Tmdb4` classes in `tmdb-api/src/commonMain/kotlin/app/moviebase/tmdb/`
- **API Classes**: Each TMDB domain (movies, shows, people, etc.) has its own API class in the `api/` package
- **HTTP Client**: Uses Ktor with custom configuration in `core/TmdbHttpClient.kt`
- **Serialization**: Kotlinx Serialization with custom serializers for dates and enums
- **Models**: Data classes in the `model/` package matching TMDB's JSON responses

Key patterns:
- Factory pattern for client creation with DSL configuration
- Suspend functions for all API calls
- Comprehensive error handling with custom exceptions
- Image URL building utilities for constructing TMDB image URLs

## Testing Approach

- Mock HTTP responses stored in `tmdb-api/src/jvmTest/resources/`
- Unit tests use `MockHttpClient` to simulate API responses
- Integration tests can use real API with credentials from environment variables
- Run single test: `./gradlew jvmTest --tests "TestClassName.testMethodName"`

## CI/CD

GitHub Actions automatically:
- Runs tests on every PR
- Publishes to Maven Central when pushing to main branch
- Uses macOS runner for multiplatform compatibility