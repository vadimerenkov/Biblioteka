package vadimerenkov.biblioteka.movies.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vadimerenkov.biblioteka.movies.presentation.details.MovieDetailsScreen
import vadimerenkov.biblioteka.movies.presentation.details.MovieDetailsViewModel
import vadimerenkov.biblioteka.movies.presentation.edit.MovieEditScreen
import vadimerenkov.biblioteka.movies.presentation.edit.MovieEditViewModel
import vadimerenkov.biblioteka.movies.presentation.list.MovieListScreen
import vadimerenkov.biblioteka.movies.presentation.search.MovieSearchScreen
import vadimerenkov.biblioteka.movies.presentation.search.MovieSearchViewModel

@Composable
fun MoviesScreen() {
	val backStack = remember { mutableStateListOf<NavKey>(MovieListRoute) }

	NavDisplay(
		backStack = backStack,
		sceneStrategies = listOf(DialogSceneStrategy()),
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry<MovieListRoute> {
				MovieListScreen(
					onMovieClick = { id, tmdbId ->
						backStack.add(MovieDetailsRoute(id = id, tmdbId = tmdbId))
					},
					onMovieEditClick = { id ->
						backStack.add(MovieEditRoute(id))
					},
					onAddMovieClick = {
						backStack.add(MovieSearchRoute(""))
					}
				)
			}
			entry<MovieDetailsRoute> { route ->
				val viewModel: MovieDetailsViewModel = koinViewModel { parametersOf(route.id, route.tmdbId) }
				MovieDetailsScreen(
					viewModel = viewModel,
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
			entry<MovieEditRoute> {
				val viewModel: MovieEditViewModel = koinViewModel { parametersOf(it.id) }
				MovieEditScreen(
					viewModel = viewModel,
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
			entry<MovieSearchRoute> {
				val viewModel: MovieSearchViewModel = koinViewModel { parametersOf(it.initialSearch) }
				MovieSearchScreen(
					viewModel = viewModel,
					showConfirmButton = false,
					onConfirmClick = {},
					onMovieClick = { tmdbId ->
						backStack.add(MovieDetailsRoute(null, tmdbId))
					},
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
		}
	)
}