package vadimerenkov.biblioteka.shows.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.biblioteka.shows.presentation.details.ShowDetailsViewModel
import vadimerenkov.biblioteka.shows.presentation.edit.ShowEditViewModel
import vadimerenkov.biblioteka.shows.presentation.episode_details.EpisodeDetailsViewModel
import vadimerenkov.biblioteka.shows.presentation.list.ShowListViewModel
import vadimerenkov.biblioteka.shows.presentation.search.ShowSearchViewModel

val showsPresentationModule = module {
	viewModelOf(::ShowListViewModel)
	viewModelOf(::ShowEditViewModel)
	viewModelOf(::ShowSearchViewModel)
	viewModelOf(::EpisodeDetailsViewModel)
	viewModel { params ->
		ShowDetailsViewModel(
			id = params[0],
			tmdbId = params[1],
			repository = get()
		)
	}
}