package vadimerenkov.biblioteka.core.data.database.movies

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

	@Upsert
	suspend fun saveMovie(movie: MovieEntity)

	@Upsert
	suspend fun saveMovies(movies: List<MovieEntity>)

	@Query("SELECT * FROM movieentity")
	fun getAllMovies(): Flow<List<MovieEntity>>

	@Query("SELECT * FROM movieentity WHERE id = :id")
	suspend fun getMovie(id: String): MovieEntity

	@Query("SELECT * FROM movieentity WHERE tmdbId = :tmdbId")
	suspend fun getMovieByTmdbId(tmdbId: Long): MovieEntity?

	@Query("SELECT localPath FROM movieentity")
	suspend fun getSavedLocalPaths(): List<String>
}