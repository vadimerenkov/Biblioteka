package vadimerenkov.biblioteka.shows.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.biblioteka.shows.data.KtorTvRepository
import vadimerenkov.biblioteka.shows.data.TvFileParser
import vadimerenkov.biblioteka.shows.domain.TvRepository

val showsDataModule = module {
	singleOf(::TvFileParser)
	singleOf(::KtorTvRepository) bind TvRepository::class
}