package vadimerenkov.biblioteka.movies.data.tmdb

import kotlinx.serialization.Serializable

@Serializable
data class SearchedMoviesResponse(
	val results: List<SearchedMovieDto>
)
