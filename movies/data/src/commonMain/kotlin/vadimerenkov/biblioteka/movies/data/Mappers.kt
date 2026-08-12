package vadimerenkov.biblioteka.movies.data

import kotlinx.serialization.json.Json
import vadimerenkov.biblioteka.core.data.database.movies.MovieEntity
import vadimerenkov.biblioteka.core.domain.util.parseDate
import vadimerenkov.biblioteka.movies.data.tmdb.MovieDetailsDto
import vadimerenkov.biblioteka.movies.data.tmdb.SearchedMovieDto
import vadimerenkov.biblioteka.movies.domain.Movie
import java.time.LocalDate
import java.util.UUID

fun SearchedMovieDto.toMovie(): Movie {
	return Movie(
		id = UUID.randomUUID().toString(),
		tmdbId = id,
		title = title,
		posterPath = "https://image.tmdb.org/t/p/original$posterPath",
		releaseDate = parseDate(releaseDate),
		avgRating = avgRating,
		ratingsCount = ratingsCount
	)
}

fun Movie.toEntity(): MovieEntity {
	return MovieEntity(
		id = id,
		tmdbId = tmdbId,
		imdbId = imdbId,
		title = title,
		backdropPath = backdropPath,
		posterPath = posterPath,
		genres = Json.encodeToString(genres),
		originCountries = Json.encodeToString(originCountries),
		originalTitle = originalTitle,
		localPath = localPath,
		description = description,
		productionCompanies = Json.encodeToString(productionCompanies),
		releaseDate = releaseDate?.toEpochDay(),
		runtime = runtime,
		rating = rating,
		revenue = revenue,
		tagline = tagline,
		avgRating = avgRating,
		ratingsCount = ratingsCount,
		status = status,
		finishedOn = finishedOn?.toEpochDay(),
		startedOn = startedOn?.toEpochDay()
	)
}

fun MovieEntity.toMovie(): Movie {
	return Movie(
		id = id,
		tmdbId = tmdbId,
		imdbId = imdbId,
		title = title,
		backdropPath = backdropPath,
		posterPath = posterPath,
		genres = if (genres != null) Json.decodeFromString(genres!!) else emptyList(),
		originCountries = if (originCountries != null) Json.decodeFromString(originCountries!!) else emptyList(),
		originalTitle = originalTitle,
		localPath = localPath,
		description = description,
		productionCompanies = if (productionCompanies != null) Json.decodeFromString(productionCompanies!!) else emptyList(),
		releaseDate = if (releaseDate != null) LocalDate.ofEpochDay(releaseDate!!) else null,
		runtime = runtime,
		rating = rating,
		revenue = revenue,
		tagline = tagline,
		avgRating = avgRating,
		ratingsCount = ratingsCount,
		status = status,
		finishedOn = if (finishedOn != null) LocalDate.ofEpochDay(finishedOn!!) else null,
		startedOn = if (startedOn != null) LocalDate.ofEpochDay(startedOn!!) else null
	)
}

fun MovieDetailsDto.toMovie(movie: Movie): Movie {
	return movie.copy(
		title = title,
		posterPath = "https://image.tmdb.org/t/p/original$posterPath",
		backdropPath = "https://image.tmdb.org/t/p/original$backdropPath",
		budget = budget,
		genres = genres.map { it.name },
		imdbId = imdbId,
		tmdbId = id,
		originCountries = originCountries,
		originalTitle = originalTitle,
		description = description,
		revenue = revenue,
		productionCompanies = productionCompanies.map { it.name },
		releaseDate = parseDate(releaseDate),
		runtime = runtime,
		tagline = tagline,
		avgRating = avgRating,
		ratingsCount = ratingsCount,
	)
}
