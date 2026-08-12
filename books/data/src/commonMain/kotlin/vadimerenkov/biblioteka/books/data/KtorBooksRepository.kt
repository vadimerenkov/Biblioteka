package vadimerenkov.biblioteka.books.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vadimerenkov.biblioteka.books.data.google.GoogleBookResponseDto
import vadimerenkov.biblioteka.books.data.open_library.OpenLibraryBooksResponseDto
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.domain.BooksRepository
import vadimerenkov.biblioteka.core.data.database.books.BooksDao
import vadimerenkov.biblioteka.core.data.networking.safeCall
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result
import vadimerenkov.biblioteka.core.domain.util.map

private const val OL_URL = "https://openlibrary.org"
private const val GOOGLE_URL = "https://www.googleapis.com/books"


class KtorBooksRepository(
	private val httpClient: HttpClient,
	private val booksDao: BooksDao
): BooksRepository {

	override suspend fun searchBooksOpenLibrary(query: String): Result<List<Book>, DataError.Remote> {
		val t = safeCall<OpenLibraryBooksResponseDto> {
			httpClient.get(
				urlString = "$OL_URL/search.json"
			) {
				parameter("q", query)
				parameter("page", 1)
				parameter("limit", 20)
				parameter("fields", "key,title,description,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count")
			}
		}
		return t.map { response ->
			response.books.map { it.toBook() }
		}
	}

	override suspend fun searchBooksGoogle(query: String): Result<List<Book>, DataError.Remote> {
		val apiKey = BuildKonfig.GOOGLE_API_KEY

		val t = safeCall<GoogleBookResponseDto> {
			httpClient.get(
				urlString = "$GOOGLE_URL/v1/volumes"
			) {
				parameter("q", query)
				parameter("key", apiKey)
			}
		}.map { response ->
			response.items.map { it.book.toBook(it.id) }
		}
		return t
	}

	override suspend fun saveBook(book: Book) {
		booksDao.saveBook(book.toEntity())
	}

	override fun getAllBooks(): Flow<List<Book>> {
		return booksDao.getAllBooks().map { books ->
			books.map { it.toBook() }
		}
	}

	override suspend fun getBook(id: String): Book? {
		return booksDao.getBook(id)?.toBook()
	}
}