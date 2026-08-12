package vadimerenkov.biblioteka.core.data.database.shows

import androidx.room.Embedded
import androidx.room.Relation

data class SeasonWithEpisodes(
	@Embedded val season: TvSeasonEntity,
	@Relation(
		parentColumn = "id",
		entityColumn = "seasonId"
	)
	val episodes: List<TvEpisodeEntity>
)
