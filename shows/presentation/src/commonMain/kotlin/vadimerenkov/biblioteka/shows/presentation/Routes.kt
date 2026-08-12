package vadimerenkov.biblioteka.shows.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ShowListRoute: NavKey

@Serializable
data class ShowEditRoute(val id: String?): NavKey

@Serializable
data class ShowDetailsRoute(val id: String?, val tmdb: Long?): NavKey

@Serializable
data class ShowSearchRoute(val initialQuery: String): NavKey

@Serializable
data class EpisodeDetailsRoute(val id: String): NavKey