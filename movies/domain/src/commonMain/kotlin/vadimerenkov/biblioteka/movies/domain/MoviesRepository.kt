package vadimerenkov.biblioteka.movies.domain

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result

interface MoviesRepository {

	suspend fun saveMovie(movie: Movie)
	suspend fun searchMovie(query: String): Result<List<Movie>, DataError.Remote>
	suspend fun parseFolderAndSave(folder: PlatformFile)
	fun getAllMovies(): Flow<List<Movie>>
	suspend fun getMovie(id: String): Movie
	suspend fun getDetailsForMovie(movie: Movie, tmdbId: Long): Movie?
}