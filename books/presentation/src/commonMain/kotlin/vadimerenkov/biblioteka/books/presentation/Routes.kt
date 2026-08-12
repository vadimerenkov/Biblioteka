package vadimerenkov.biblioteka.books.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import vadimerenkov.biblioteka.books.domain.Book

@Serializable
data object BookSearchRoute: NavKey

@Serializable
data object BookListRoute: NavKey

@Serializable
data class BookEditRoute(val book: Book): NavKey

@Serializable
data class BookDetailsRoute(val book: Book): NavKey