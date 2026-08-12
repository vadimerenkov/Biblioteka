package vadimerenkov.biblioteka.shows.domain

import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

data class TvShow(
	val id: String,
	val tmdbId: Long?,
	val title: String,
	val description: String?,
	val seasons: List<TvSeason>,
	val localPaths: List<String>,
	val backdropPath: String?,
	val posterPath: String?,
	val episodeRuntime: Int?,
	val firstAirDate: LocalDate?,
	val genres: List<String>,
	val lastAirDate: LocalDate?,
	val isOngoing: Boolean?,
	val networks: List<String>,
	val originCountries: List<String>,
	val originalTitle: String?,
	val tagline: String?,
	val avgRating: Double?,
	val ratingsCount: Int?,
	val rating: Int?,
	val status: CompletionStatus = CompletionStatus.NOT_STARTED,
	val finishedOn: LocalDate? = null,
	val startedOn: LocalDate? = null
)
