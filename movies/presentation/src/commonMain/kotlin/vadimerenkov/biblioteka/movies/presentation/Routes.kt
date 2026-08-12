package vadimerenkov.biblioteka.movies.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MovieListRoute: NavKey

@Serializable
data class MovieDetailsRoute(
	val id: String? = null,
	val tmdbId: Long? = null
): NavKey

@Serializable
data class MovieEditRoute(
	val id: String
): NavKey

data class MovieSearchRoute(
	val initialSearch: String
): NavKey