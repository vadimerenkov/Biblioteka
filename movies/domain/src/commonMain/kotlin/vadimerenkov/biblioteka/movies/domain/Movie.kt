package vadimerenkov.biblioteka.movies.domain

import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

data class Movie(
	val id: String,
	val tmdbId: Long? = null,
	val imdbId: String? = null,
	val title: String = "",
	val backdropPath: String? = null,
	val posterPath: String? = null,
	val genres: List<String> = emptyList(),
	val originCountries: List<String> = emptyList(),
	val originalTitle: String? = null,
	val localPath: String? = null,
	val description: String? = null,
	val productionCompanies: List<String> = emptyList(),
	val releaseDate: LocalDate? = null,
	val runtime: Int? = null,
	val revenue: Int? = null,
	val budget: Int? = null,
	val tagline: String? = null,
	val avgRating: Double? = null,
	val ratingsCount: Int? = null,
	val status: CompletionStatus = CompletionStatus.NOT_STARTED,
	val rating: Int? = null,
	val finishedOn: LocalDate? = null,
	val startedOn: LocalDate? = null
)
