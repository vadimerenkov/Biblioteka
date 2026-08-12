package vadimerenkov.biblioteka.movies.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.biblioteka.movies.presentation.details.MovieDetailsViewModel
import vadimerenkov.biblioteka.movies.presentation.edit.MovieEditViewModel
import vadimerenkov.biblioteka.movies.presentation.list.MovieListViewModel
import vadimerenkov.biblioteka.movies.presentation.search.MovieSearchViewModel

val moviesPresentationModule = module {
	viewModelOf(::MovieSearchViewModel)
	viewModelOf(::MovieListViewModel)
	viewModel { params ->
		MovieDetailsViewModel(
			id = params[0],
			tmdbId = params[1],
			repository = get()
		)
	}
	viewModelOf(::MovieEditViewModel)
}