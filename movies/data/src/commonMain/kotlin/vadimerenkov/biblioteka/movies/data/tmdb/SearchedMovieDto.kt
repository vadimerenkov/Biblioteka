package vadimerenkov.biblioteka.movies.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedMovieDto(
	val id: Long,
	val title: String,
	@SerialName("poster_path") val posterPath: String? = null,
	@SerialName("vote_average") val avgRating: Double? = null,
	@SerialName("vote_count") val ratingsCount: Int? = null,
	@SerialName("release_date") val releaseDate: String? = null
)
