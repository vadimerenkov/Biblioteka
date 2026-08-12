package vadimerenkov.biblioteka.shows.data

import kotlinx.serialization.json.Json
import vadimerenkov.biblioteka.core.data.database.shows.SeasonWithEpisodes
import vadimerenkov.biblioteka.core.data.database.shows.ShowWithSeasons
import vadimerenkov.biblioteka.core.data.database.shows.TvEpisodeEntity
import vadimerenkov.biblioteka.core.data.database.shows.TvSeasonEntity
import vadimerenkov.biblioteka.core.data.database.shows.TvShowEntity
import vadimerenkov.biblioteka.core.domain.util.parseDate
import vadimerenkov.biblioteka.shows.data.tmdb.SearchedShowDto
import vadimerenkov.biblioteka.shows.data.tmdb.SeasonsObject
import vadimerenkov.biblioteka.shows.data.tmdb.ShowDetailsDto
import vadimerenkov.biblioteka.shows.data.tmdb.TvEpisodeDetailsDto
import vadimerenkov.biblioteka.shows.domain.TvEpisode
import vadimerenkov.biblioteka.shows.domain.TvSeason
import vadimerenkov.biblioteka.shows.domain.TvShow
import java.time.LocalDate
import java.util.UUID

fun TvShow.toEntity(): TvShowEntity {
	return TvShowEntity(
		id = id,
		tmdbId = tmdbId,
		title = title,
		description = description,
		localPaths = Json.encodeToString(localPaths),
		backdropPath = backdropPath,
		posterPath = posterPath,
		episodeRuntime = episodeRuntime,
		firstAirDate = firstAirDate?.toEpochDay(),
		genres = Json.encodeToString(genres),
		lastAirDate = lastAirDate?.toEpochDay(),
		isOngoing = isOngoing,
		networks = Json.encodeToString(networks),
		originCountries = Json.encodeToString(originCountries),
		originalTitle = originalTitle,
		tagline = tagline,
		avgRating = avgRating,
		ratingsCount = ratingsCount,
		rating = rating,
		status = status,
		finishedOn = finishedOn?.toEpochDay(),
		startedOn = startedOn?.toEpochDay()
	)
}

fun TvShowEntity.toShow(): TvShow {
	return TvShow(
		id = id,
		tmdbId = tmdbId,
		title = title,
		description = description,
		localPaths = Json.decodeFromString(localPaths),
		seasons = emptyList(),
		backdropPath = backdropPath,
		posterPath = posterPath,
		episodeRuntime = episodeRuntime,
		firstAirDate = if (firstAirDate != null) LocalDate.ofEpochDay(firstAirDate!!) else null,
		genres = Json.decodeFromString(genres),
		lastAirDate = if (lastAirDate != null) LocalDate.ofEpochDay(lastAirDate!!) else null,
		isOngoing = isOngoing,
		networks = Json.decodeFromString(networks),
		originCountries = Json.decodeFromString(originCountries),
		originalTitle = originalTitle,
		tagline = tagline,
		avgRating = avgRating,
		ratingsCount = ratingsCount,
		rating = rating,
		status = status,
		finishedOn = if (finishedOn != null) LocalDate.ofEpochDay(finishedOn!!) else null,
		startedOn = if (startedOn != null) LocalDate.ofEpochDay(startedOn!!) else null
	)
}

fun SearchedShowDto.toShow(): TvShow {
	return TvShow(
		id = UUID.randomUUID().toString(),
		tmdbId = tmdbId,
		title = title,
		description = null,
		seasons = emptyList(),
		localPaths = emptyList(),
		backdropPath = null,
		posterPath = "https://image.tmdb.org/t/p/original$posterPath",
		episodeRuntime = null,
		firstAirDate = null,
		genres = emptyList(),
		lastAirDate = null,
		isOngoing = null,
		networks = emptyList(),
		originCountries = emptyList(),
		originalTitle = null,
		tagline = null,
		avgRating = avgRating,
		ratingsCount = ratingsCount,
		rating = null
	)
}

fun ShowDetailsDto.toShow(show: TvShow): TvShow {
	return show.copy(
		tmdbId = tmdbId,
		title = title,
		description = description,
		seasons = seasons.map { it.toSeason(show.id) },
		backdropPath = "https://image.tmdb.org/t/p/original$backdropPath",
		posterPath = "https://image.tmdb.org/t/p/original$posterPath",
		episodeRuntime = episodeRuntime.firstOrNull(),
		firstAirDate = parseDate(firstAirDate),
		genres = genres.map { it.name },
		lastAirDate = parseDate(lastAirDate),
		isOngoing = isOngoing,
		networks = networks.map { it.name },
		originCountries = originCountries,
		originalTitle = originalTitle,
		tagline = tagline,
		avgRating = avgRating,
		ratingsCount = ratingsCount
	)
}

fun SeasonsObject.toSeason(showId: String): TvSeason {
	return TvSeason(
		id = UUID.randomUUID().toString(),
		showId = showId,
		tmdbId = tmdbId,
		episodes = emptyList(),
		seasonNumber = seasonNumber,
		description = description,
		episodeCount = episodeCount
	)
}

fun ShowWithSeasons.toShow(): TvShow {
	val seasons = seasons.map { it.toSeason() }
	return this.show.toShow().copy(
		seasons = seasons
	)
}

fun SeasonWithEpisodes.toSeason(): TvSeason {
	val episodes = episodes.map { it.toEpisode() }
	return with(season) {
		TvSeason(
			id = id,
			showId = showId,
			tmdbId = tmdbId,
			episodes = episodes,
			seasonNumber = seasonNumber,
			description = description,
			episodeCount = episodeCount
		)
	}
}

fun TvEpisodeEntity.toEpisode(): TvEpisode {
	return TvEpisode(
		id = id,
		seasonId = seasonId,
		tmdbId = tmdbId,
		title = title,
		episodeNumber = episodeNumber,
		description = description,
		localPath = localPath,
		posterPath = posterPath,
		status = status,
		runtime = runtime
	)
}

fun TvSeason.toEntity(): TvSeasonEntity {
	return TvSeasonEntity(
		id = id,
		showId = showId,
		tmdbId = tmdbId,
		seasonNumber = seasonNumber,
		description = description,
		episodeCount = episodeCount
	)
}

fun TvEpisode.toEntity(): TvEpisodeEntity {
	return TvEpisodeEntity(
		id = id,
		seasonId = seasonId,
		tmdbId = tmdbId,
		title = title,
		description = description,
		episodeNumber = episodeNumber,
		localPath = localPath,
		posterPath = posterPath,
		status = status,
		runtime = runtime
	)
}

fun TvEpisodeDetailsDto.toEpisode(seasonId: String): TvEpisode {
	return TvEpisode(
		id = UUID.randomUUID().toString(),
		seasonId = seasonId,
		tmdbId = tmdbId,
		title = title,
		description = description,
		episodeNumber = episodeNumber,
		localPath = null,
		posterPath = "https://image.tmdb.org/t/p/original$posterPath",
		runtime = runtime
	)
}