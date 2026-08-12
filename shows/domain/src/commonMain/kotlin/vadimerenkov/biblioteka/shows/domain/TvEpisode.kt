package vadimerenkov.biblioteka.shows.domain

import vadimerenkov.biblioteka.core.domain.CompletionStatus

data class TvEpisode(
	val id: String,
	val seasonId: String,
	val tmdbId: Long?,
	val title: String,
	val description: String?,
	val episodeNumber: Int?,
	val localPath: String?,
	val posterPath: String?,
	val runtime: Int?,
	val status: CompletionStatus = CompletionStatus.NOT_STARTED
)
