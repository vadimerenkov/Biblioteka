package vadimerenkov.biblioteka.core.data.database.shows

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import vadimerenkov.biblioteka.core.domain.CompletionStatus

@Entity(
	foreignKeys = [
		ForeignKey(
			entity = TvSeasonEntity::class,
			parentColumns = ["id"],
			childColumns = ["seasonId"],
			onDelete = CASCADE
		)
	]
)
data class TvEpisodeEntity(
	@PrimaryKey val id: String,
	val seasonId: String,
	val tmdbId: Long?,
	val title: String,
	val description: String?,
	val episodeNumber: Int?,
	val localPath: String?,
	val posterPath: String?,
	val status: CompletionStatus,
	val runtime: Int?
)
