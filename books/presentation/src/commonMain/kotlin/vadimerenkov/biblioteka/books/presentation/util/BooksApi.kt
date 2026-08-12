package vadimerenkov.biblioteka.books.presentation.util

import androidx.compose.runtime.Composable
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.google_books
import biblioteka.core.presentation.generated.resources.open_library
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.books.presentation.search_books.BooksApi

@Composable
fun BooksApi.toText(): String {
	return when (this) {
		BooksApi.OPEN_LIBRARY -> stringResource(Res.string.open_library)
		BooksApi.GOOGLE -> stringResource(Res.string.google_books)
	}
}