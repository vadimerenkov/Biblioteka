package vadimerenkov.biblioteka.core.data.database.shows

import androidx.room.Embedded
import androidx.room.Relation

data class ShowWithSeasons(
	@Embedded val show: TvShowEntity,
	@Relation(
		parentColumn = "id",
		entityColumn = "showId",
		entity = TvSeasonEntity::class
	)
	val seasons: List<SeasonWithEpisodes>
)
