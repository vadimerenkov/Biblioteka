package vadimerenkov.biblioteka.books.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.biblioteka.books.data.KtorBooksRepository
import vadimerenkov.biblioteka.books.domain.BooksRepository

val booksDataModule = module {
	singleOf(::KtorBooksRepository) bind BooksRepository::class
}