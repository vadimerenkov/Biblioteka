package vadimerenkov.biblioteka.movies.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.biblioteka.movies.data.KtorMoviesRepository
import vadimerenkov.biblioteka.movies.data.MovieFilesParser
import vadimerenkov.biblioteka.movies.domain.MovieParser
import vadimerenkov.biblioteka.movies.domain.MoviesRepository

val moviesDataModule = module {
	singleOf(::MovieFilesParser) bind MovieParser::class
	singleOf(::KtorMoviesRepository) bind MoviesRepository::class
}