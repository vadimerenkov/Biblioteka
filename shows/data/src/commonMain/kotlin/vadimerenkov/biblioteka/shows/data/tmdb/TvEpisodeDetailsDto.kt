package vadimerenkov.biblioteka.shows.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvEpisodeDetailsDto(
	@SerialName("id") val tmdbId: Long,
	@SerialName("name") val title: String,
	@SerialName("overview") val description: String?,
	@SerialName("air_date") val airDate: String?,
	val runtime: Int?,
	@SerialName("episode_number") val episodeNumber: Int,
	@SerialName("season_number") val seasonNumber: Int,
	@SerialName("still_path") val posterPath: String?,
	@SerialName("vote_average") val avgRating: Double?,
	@SerialName("vote_count") val ratingsCount: Int?,
)
