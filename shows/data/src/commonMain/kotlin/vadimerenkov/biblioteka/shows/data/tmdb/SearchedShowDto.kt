package vadimerenkov.biblioteka.shows.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedShowDto(
	@SerialName("id") val tmdbId: Long,
	@SerialName("name") val title: String,
	@SerialName("poster_path") val posterPath: String?,
	@SerialName("vote_average") val avgRating: Double?,
	@SerialName("vote_count") val ratingsCount: Int?
)
