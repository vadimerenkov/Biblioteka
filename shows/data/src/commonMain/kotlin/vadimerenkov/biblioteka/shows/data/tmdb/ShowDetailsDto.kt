package vadimerenkov.biblioteka.shows.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDetailsDto(
	@SerialName("id") val tmdbId: Long,
	@SerialName("name") val title: String,
	@SerialName("overview") val description: String,
	@SerialName("backdrop_path") val backdropPath: String,
	val seasons: List<SeasonsObject>,
	@SerialName("poster_path") val posterPath: String,
	@SerialName("episode_run_time") val episodeRuntime: List<Int>,
	@SerialName("first_air_date") val firstAirDate: String,
	val genres: List<GenresObject>,
	@SerialName("last_air_date") val lastAirDate: String?,
	@SerialName("in_production") val isOngoing: Boolean,
	val networks: List<NetworkObject>,
	@SerialName("origin_country") val originCountries: List<String>,
	@SerialName("original_name") val originalTitle: String,
	val tagline: String,
	@SerialName("vote_average") val avgRating: Double,
	@SerialName("vote_count") val ratingsCount: Int?,
)
