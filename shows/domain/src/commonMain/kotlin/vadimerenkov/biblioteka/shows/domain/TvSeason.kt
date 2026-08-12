package vadimerenkov.biblioteka.shows.domain

data class TvSeason(
	val id: String,
	val showId: String,
	val tmdbId: Long?,
	val episodes: List<TvEpisode>,
	val seasonNumber: Int,
	val description: String?,
	val episodeCount: Int
)
