package vadimerenkov.biblioteka.core.data.database.movies

import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.biblioteka.core.domain.CompletionStatus

@Entity
data class MovieEntity(
	@PrimaryKey val id: String,
	val tmdbId: Long?,
	val imdbId: String?,
	val title: String,
	val backdropPath: String?,
	val posterPath: String?,
	val genres: String?,
	val originCountries: String?,
	val originalTitle: String?,
	val localPath: String?,
	val description: String?,
	val productionCompanies: String?,
	val releaseDate: Long?,
	val runtime: Int?,
	val revenue: Int?,
	val tagline: String?,
	val avgRating: Double?,
	val ratingsCount: Int?,
	val status: CompletionStatus,
	val finishedOn: Long?,
	val startedOn: Long?,
	val rating: Int?
)
