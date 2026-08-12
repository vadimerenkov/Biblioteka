package vadimerenkov.biblioteka.core.data.database.shows

import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.biblioteka.core.domain.CompletionStatus

@Entity
data class TvShowEntity(
	@PrimaryKey val id: String,
	val tmdbId: Long?,
	val title: String,
	val description: String?,
	val localPaths: String,
	val backdropPath: String?,
	val posterPath: String?,
	val episodeRuntime: Int?,
	val firstAirDate: Long?,
	val genres: String,
	val lastAirDate: Long?,
	val isOngoing: Boolean?,
	val networks: String,
	val originCountries: String,
	val originalTitle: String?,
	val tagline: String?,
	val avgRating: Double?,
	val ratingsCount: Int?,
	val rating: Int?,
	val status: CompletionStatus,
	val finishedOn: Long?,
	val startedOn: Long?
)
