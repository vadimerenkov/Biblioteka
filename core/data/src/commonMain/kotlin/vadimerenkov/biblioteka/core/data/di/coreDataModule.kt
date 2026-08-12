package vadimerenkov.biblioteka.core.data.di

import org.koin.dsl.module
import vadimerenkov.biblioteka.core.data.database.MediaDatabase
import vadimerenkov.biblioteka.core.data.networking.HttpClientFactory

val coreDataModule = module {
	single { HttpClientFactory.create() }
	single { MediaDatabase.initialize() }
	single { get<MediaDatabase>().booksDao }
	single { get<MediaDatabase>().moviesDao }
	single { get<MediaDatabase>().tvDao }
}