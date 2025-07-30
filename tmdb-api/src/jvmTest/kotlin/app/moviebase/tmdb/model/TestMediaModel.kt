package app.moviebase.tmdb.model

import kotlinx.datetime.LocalDate

fun TestTmdbMovie() = TmdbMovie(
    id = 12,
    title = "Jack Reacher: Never Go Back",
    posterPath = "/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg",
    backdropPath = "/3FHrAeYMogXd6K1e5tUzQAiS7GE.jpg",
    genresIds = listOf(TmdbGenreId.Movie.ACTION, TmdbGenreId.Movie.ADVENTURE),
    voteAverage = 4.5f,
    popularity = 0.8943f,
    releaseDate = LocalDate.parse("2021-03-21"),
    overview = "overview",
    originalTitle = "originalTitle",
    originalLanguage = "EN",
    voteCount = 23,
    video = false
)
