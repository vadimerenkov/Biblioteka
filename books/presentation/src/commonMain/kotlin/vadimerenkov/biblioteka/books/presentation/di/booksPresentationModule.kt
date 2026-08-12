package vadimerenkov.biblioteka.books.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.biblioteka.books.presentation.book_details.BookDetailsViewModel
import vadimerenkov.biblioteka.books.presentation.book_edit.BookEditViewModel
import vadimerenkov.biblioteka.books.presentation.book_list.BookListViewModel
import vadimerenkov.biblioteka.books.presentation.search_books.SearchBooksViewModel

val booksPresentationModule = module {
	viewModelOf(::SearchBooksViewModel)
	viewModelOf(::BookDetailsViewModel)
	viewModelOf(::BookListViewModel)
	viewModelOf(::BookEditViewModel)
}