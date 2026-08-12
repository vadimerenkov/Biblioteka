package vadimerenkov.biblioteka.movies.data

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vadimerenkov.biblioteka.core.data.database.movies.MoviesDao
import vadimerenkov.biblioteka.core.data.networking.safeCall
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result
import vadimerenkov.biblioteka.core.domain.util.map
import vadimerenkov.biblioteka.core.domain.util.onFailure
import vadimerenkov.biblioteka.core.domain.util.onSuccess
import vadimerenkov.biblioteka.movies.data.tmdb.MovieDetailsDto
import vadimerenkov.biblioteka.movies.data.tmdb.SearchedMoviesResponse
import vadimerenkov.biblioteka.movies.domain.Movie
import vadimerenkov.biblioteka.movies.domain.MovieParser
import vadimerenkov.biblioteka.movies.domain.MoviesRepository
import java.util.UUID

class KtorMoviesRepository(
	private val httpClient: HttpClient,
	private val movieParser: MovieParser,
	private val dao: MoviesDao
): MoviesRepository {
	val apiKey = BuildKonfig.TMDB_API_KEY

	override suspend fun saveMovie(movie: Movie) {
		dao.saveMovie(movie.toEntity())
	}

	override suspend fun searchMovie(query: String): Result<List<Movie>, DataError.Remote> {

		val t = safeCall<SearchedMoviesResponse> {
			httpClient.get(
				urlString = "https://api.themoviedb.org/3/search/movie"
			) {
				header("Authorization", "Bearer $apiKey")
				parameter("query", query)
			}
		}
		return t.map { response ->
			response.results.map { it.toMovie() }
		}
	}

	override suspend fun parseFolderAndSave(folder: PlatformFile) {
		val localPaths = dao.getSavedLocalPaths()
		val movies = movieParser
			.parse(folder)
			.mapNotNull {
				if (it.key.absolutePath() in localPaths) {
					null
				} else {
					Movie(
						id = UUID.randomUUID().toString(),
						title = it.value,
						localPath = it.key.absolutePath()
					)
				}
		}
		dao.saveMovies(movies.map { it.toEntity() })
		movies.forEach { movie ->
			val detailedMovie = parseMovie(movie)
			if (detailedMovie != null) {
				dao.saveMovie(detailedMovie.toEntity())
			}
		}
	}

	private suspend fun parseMovie(movie: Movie): Movie? {
		searchMovie(movie.title)
			.onSuccess { movies ->
				val tmdbId = movies.firstOrNull()?.tmdbId ?: return null
				return getDetailsForMovie(movie, tmdbId)
			}
			.onFailure {
				return null
			}

		return null
	}

	override suspend fun getDetailsForMovie(movie: Movie, tmdbId: Long): Movie? {
		safeCall<MovieDetailsDto> {
			httpClient.get(
				urlString = "https://api.themoviedb.org/3/movie/${tmdbId}",
			) {
				header("Authorization", "Bearer $apiKey")
			}
		}
			.onSuccess {
				return it.toMovie(movie)
			}
			.onFailure {
				return null
			}
		return null
	}

	override fun getAllMovies(): Flow<List<Movie>> {
		return dao.getAllMovies().map { movies ->
			movies.map { it.toMovie() }
		}
	}

	override suspend fun getMovie(id: String): Movie {
		return dao.getMovie(id).toMovie()
	}
}