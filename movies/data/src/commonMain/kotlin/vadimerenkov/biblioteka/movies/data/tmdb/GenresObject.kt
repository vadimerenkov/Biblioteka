package vadimerenkov.biblioteka.movies.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresObject(
	@SerialName("name") val name: String
)
