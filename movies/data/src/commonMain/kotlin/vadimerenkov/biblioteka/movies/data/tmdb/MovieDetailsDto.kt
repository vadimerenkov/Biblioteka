package vadimerenkov.biblioteka.movies.data.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
	val id: Long,
	@SerialName("backdrop_path") val backdropPath: String?,
	val budget: Int,
	val genres: List<GenresObject>,
	@SerialName("imdb_id") val imdbId: String?,
	@SerialName("origin_country") val originCountries: List<String>,
	@SerialName("original_title") val originalTitle: String,
	@SerialName("overview") val description: String,
	@SerialName("poster_path") val posterPath: String,
	val title: String,
	@SerialName("production_companies") val productionCompanies: List<CompaniesObject>,
	@SerialName("release_date") val releaseDate: String,
	val revenue: Int,
	val runtime: Int,
	val tagline: String,
	@SerialName("vote_average") val avgRating: Double,
	@SerialName("vote_count") val ratingsCount: Int
)
