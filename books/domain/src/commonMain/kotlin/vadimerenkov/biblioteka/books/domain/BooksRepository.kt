package vadimerenkov.biblioteka.books.domain

import kotlinx.coroutines.flow.Flow
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result

interface BooksRepository {

	suspend fun searchBooksOpenLibrary(query: String): Result<List<Book>, DataError.Remote>
	suspend fun searchBooksGoogle(query: String): Result<List<Book>, DataError.Remote>
	suspend fun saveBook(book: Book)
	fun getAllBooks(): Flow<List<Book>>
	suspend fun getBook(id: String): Book?
}