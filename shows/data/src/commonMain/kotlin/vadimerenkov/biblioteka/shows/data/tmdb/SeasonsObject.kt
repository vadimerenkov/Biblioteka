package vadimerenkov.biblioteka.shows.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonsObject(
	@SerialName("id") val tmdbId: Long,
	@SerialName("episode_count") val episodeCount: Int,
	@SerialName("name") val title: String,
	@SerialName("season_number") val seasonNumber: Int,
	@SerialName("overview") val description: String
)
