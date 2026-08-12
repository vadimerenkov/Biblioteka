package vadimerenkov.biblioteka.core.data.database.shows

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
	foreignKeys = [
		ForeignKey(
			entity = TvShowEntity::class,
			parentColumns = ["id"],
			childColumns = ["showId"],
			onDelete = CASCADE
		)
	]
)
data class TvSeasonEntity(
	@PrimaryKey val id: String,
	val showId: String,
	val tmdbId: Long?,
	val seasonNumber: Int,
	val description: String?,
	val episodeCount: Int
)
