package vadimerenkov.biblioteka.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object BooksRoute: NavKey

@Serializable
data object MoviesRoute: NavKey

@Serializable
data object ShowsRoute: NavKey

@Serializable
data object AboutRoute: NavKey