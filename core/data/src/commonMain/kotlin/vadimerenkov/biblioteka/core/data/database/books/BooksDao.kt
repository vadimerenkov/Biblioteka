package vadimerenkov.biblioteka.core.data.database.books

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

	@Upsert
	suspend fun saveBook(book: BookEntity)

	@Query("SELECT * FROM bookentity")
	fun getAllBooks(): Flow<List<BookEntity>>

	@Query("SELECT * FROM bookentity WHERE id = :id")
	suspend fun getBook(id: String): BookEntity?
}