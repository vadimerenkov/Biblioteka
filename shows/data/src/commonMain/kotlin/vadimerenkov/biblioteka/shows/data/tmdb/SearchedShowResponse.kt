package vadimerenkov.biblioteka.shows.data.tmdb

import kotlinx.serialization.Serializable

@Serializable
data class SearchedShowResponse(
	val results: List<SearchedShowDto>
)
