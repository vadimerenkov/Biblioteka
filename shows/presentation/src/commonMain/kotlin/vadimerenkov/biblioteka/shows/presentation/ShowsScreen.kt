package vadimerenkov.biblioteka.shows.presentation

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
import vadimerenkov.biblioteka.shows.presentation.details.ShowDetailsScreen
import vadimerenkov.biblioteka.shows.presentation.details.ShowDetailsViewModel
import vadimerenkov.biblioteka.shows.presentation.edit.ShowEditScreen
import vadimerenkov.biblioteka.shows.presentation.edit.ShowEditViewModel
import vadimerenkov.biblioteka.shows.presentation.episode_details.EpisodeDetailsScreen
import vadimerenkov.biblioteka.shows.presentation.episode_details.EpisodeDetailsViewModel
import vadimerenkov.biblioteka.shows.presentation.list.ShowListScreen
import vadimerenkov.biblioteka.shows.presentation.search.ShowSearchScreen
import vadimerenkov.biblioteka.shows.presentation.search.ShowSearchViewModel

@Composable
fun ShowsScreen() {
	val backStack = remember { mutableStateListOf<NavKey>(ShowListRoute) }

	NavDisplay(
		backStack = backStack,
		sceneStrategies = listOf(DialogSceneStrategy()),
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry<ShowListRoute> {
				ShowListScreen(
					onShowClick = { id ->
						backStack.add(ShowDetailsRoute(id, null))
					},
					onEditShowClick = { id ->
						backStack.add(ShowEditRoute(id))
					},
					onAddShowClick = {
						backStack.add(ShowSearchRoute(""))
					}
				)
			}
			entry<ShowDetailsRoute> {
				val viewModel: ShowDetailsViewModel = koinViewModel { parametersOf(it.id, it.tmdb) }
				ShowDetailsScreen(
					viewModel = viewModel,
					onBackClick = {
						backStack.removeLastOrNull()
					},
					onEpisodeClick = { id ->
						backStack.add(EpisodeDetailsRoute(id))
					}
				)
			}
			entry<ShowEditRoute> {
				val viewModel: ShowEditViewModel = koinViewModel { parametersOf(it.id) }
				ShowEditScreen(
					viewModel = viewModel,
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
			entry<EpisodeDetailsRoute> {
				val viewModel: EpisodeDetailsViewModel = koinViewModel { parametersOf(it.id) }
				EpisodeDetailsScreen(
					viewModel = viewModel,
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
			entry<ShowSearchRoute> {
				val viewModel: ShowSearchViewModel = koinViewModel { parametersOf(it.initialQuery) }
				ShowSearchScreen(
					viewModel = viewModel,
					onShowSelected = { tmdbId ->
						backStack.add(ShowDetailsRoute(null, tmdbId))
					},
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
		}
	)
}