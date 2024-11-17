[Website](https://www.themoviedb.org) |
[Forum](https://www.themoviedb.org/talk/category/5047958519c29526b50017d6) |
[Documentation](https://www.themoviedb.org/documentation/api) |
[TMDB API 3](https://developers.themoviedb.org/3)

<a href="https://www.themoviedb.org"><img alt="TMDb" src="doc/images/blue_short.svg" width="600"></a>

***Get movies and TV shows from the largest community database.***

[![Maven Central](https://img.shields.io/maven-central/v/app.moviebase/tmdb-api?label=Maven%20Central)](https://search.maven.org/artifact/app.moviebase/tmdb-api)
![Github Actions](https://github.com/ChrisKruegerDev/tmdb-kotlin/actions/workflows/build.yml/badge.svg)
[![Issues](https://img.shields.io/github/issues/ChrisKruegerDev/tmdb-kotlin)](https://github.com/ChrisKruegerDev/tmdb-api/issues)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-8-green?style=flat)](https://gradle.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![GitHub Account](https://img.shields.io/static/v1?label=GitHub&message=ChrisKruegerDev&color=C51162)](https://github.com/ChrisKruegerDev)

<hr>

# TMDB Kotlin
TMDB Kotlin is a **Kotlin Multiplatform** library for fetching movies, TV shows, episodes, and all relevant information.

* Written in Kotlin native with ktor from the ground up.
* Support for Android, iOS, desktop, and web applications.
* High customizable HttpClient configuration


Sample projects:

* [Moviebase](https://play.google.com/store/apps/details?id=com.moviebase)
* [StreamGuide](https://play.google.com/store/apps/details?id=app.streamguide)
* [tivi](https://github.com/chrisbanes/tivi)
*
## Adding to your project

The library is available on Maven Central.

### Gradle

Add the Maven Central repository if it is not already there.

```kotlin
repositories {
    mavenCentral()
}
```

To use the library in a single-platform project, add a dependency.

```kotlin
dependencies {
    implementation("app.moviebase:tmdb-api:1.4.0")
}
```

In Kotlin Multiplatform projects, add the dependency to your commonMain source-set dependencies.

```kotlin
commonMain {
    dependencies {
        implementation("app.moviebase:tmdb-api:1.4.0")
    }
}
```

## Usage
Most of the library follows the possibilities and naming at the [official documentation](https://www.themoviedb.org/documentation/api). The documentation of the API endpoints is on [version 3](https://developers.themoviedb.org/3) and [version 4](https://developers.themoviedb.org/4).

### Configuration
Build up your TMDB instance to access the APIs. You can configure the entire HttpClient of ktor.

```kotlin
val tmdb = Tmdb3 {
    tmdbApiKey = "yourApiKey"

    userAuthentication {
        authenticationToken = "auth token for version 4"
        val storage = TmdbAccountStorage() // use own class here
        loadSessionId { storage.sessionId  }
        loadGuestSessionId { storage.guestSessionId }
        loadAccessToken { storage.accessToken }
    }

    expectSuccess = false // if you want to disable exceptions
    useCache = true
    useTimeout = true
    maxRetriesOnException = 3 // retries when network calls throw an exception

    // add your own client
    httpClient(OkHttp) {
        engine {

        }
    }

    httpClient {
        // for custom HttpClient configuration
    }
}
```

You need to add your own TMDB API key for using the library. The instruction for the creation is in the [TMDB developer guide](https://developers.themoviedb.org).


### Get information
For getting basic information about a movie or other media content.

```kotlin
val tmdb = Tmdb3("apiKey")
val movieDetail = tmdb.movies.getDetails(
    movieId = 12,
    language = "DE",
    appendResponses = listOf(AppendResponse.MOVIE_CREDITS)
)
```

### Search
Search for TV shows by a query.

```kotlin
val tmdb = Tmdb3("apiKey")
val showPageResult = tmdb.search.findShows(
    query = "The Expanse",
    page = 1,
    language = "DE",
    region = "US",
    includeAdult = false
)
```


### Discover
Discover a movie or TV show by the discover parameter class.

```kotlin
val discover = TmdbDiscover.Movie(
    sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
    sortOrder = TmdbSortOrder.DESC,
    voteAverageGte = 5,
    voteCountGte = 200,
    releaseDate = TmdbDiscoverTimeRange.BetweenYears(from = 2020, to = 2021)
)

val discoverPageResult = tmdb.discover.discoverMovie(
    page = 1,
    region = "DE",
    language = "de",
    discover = discover
)
```

Alternatively, use predefined discover categories like upcoming, networks or on streaming.

```kotlin
val discoverCategory = DiscoverCategory.OnStreaming.Netflix(
    mediaType = TmdbMediaType.MOVIE,
    watchRegion = "DE"
)

val result = tmdb.discover.discoverByCategory(
    page = 1,
    region = "DE",
    language = "de",
    category = discoverCategory
)
```

### Authentication
Build up your authentication URL, which you can open in the web view.

```kotlin
// Returns "https://www.themoviedb.org/authenticate/[request_token]?redirect_to=auth://yourApp"
val url = tmdb.authentication.requestAuthorizationUrl("auth://yourApp")
```
After opening the URL, TMDB version 3 returns the following URL:
```kotlin
yourApp://auth/login?request_token=[request_token]&approved=true
```


### Build image URL

You can build an image URL via the poster file path and size key. More information on the [TMDB images site](https://developers.themoviedb.org/3/getting-started/images).

```kotlin
val url = TmdbImageUrlBuilder.build("nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "w154")
```

Or create the URL by an image class with the best matched width/height.

```kotlin
val url = TmdbImageUrlBuilder.build(image = movie.posterImage, width = 200, height = 300)
```

For creating the poster URL by the movie item.

```kotlin
val url = TmdbImageUrlBuilder.buildPoster(item = movie, width = 200)
```

### Build video URL

You can build a video URL by the key value into `TmdbVideo`. TMDb API currently support videos from YouTube and Vimeo

```kotlin
val youtubeTmdbVideo = TmdbVideo(id = "123", key = "qwerasdf", site = TmdbVideoSite.YOUTUBE)
val url = TmdbImageUrlBuilder.build(youtubeTmdbVideo) // It will return `https://www.youtube.com/watch?v=qwerasdf`

val vimeoTmdbVideo = TmdbVideo(id = "123", key = "qwerasdf", site = TmdbVideoSite.VIMEO)
val url = TmdbImageUrlBuilder.build(vimeoTmdbVideo) // It will return `https://vimeo.com/qwerasdf`
```
## Contributing 🤝
Please feel free to [open an issue](https://github.com/ChrisKruegerDev/tmdb-kotlin/issues/new/choose) if you have any questions or suggestions. Or participate in the [discussion](https://github.com/ChrisKruegerDev/tmdb-kotlin/discussions). If you want to contribute, please read the [contribution guidelines](https://github.com/ChrisKruegerDev/tmdb-kotlin/blob/main/CONTRIBUTING.md) for more information.

<br>

<hr>

*This library uses the TMDB but is not endorsed or certified by TMDB. These services are licensed under [CC BY-NC 4.0](https://creativecommons.org/licenses/by-nc/4.0).*
