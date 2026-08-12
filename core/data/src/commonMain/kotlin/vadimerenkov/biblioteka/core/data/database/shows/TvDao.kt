package vadimerenkov.biblioteka.core.data.database.shows

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TvDao {

	@Upsert
	suspend fun saveTvShow(show: TvShowEntity)

	@Upsert
	suspend fun saveTvShows(shows: List<TvShowEntity>)

	@Upsert
	suspend fun saveSeasons(seasons: List<TvSeasonEntity>)

	@Upsert
	suspend fun saveEpisode(episode: TvEpisodeEntity)

	@Upsert
	suspend fun saveEpisodes(episodes: List<TvEpisodeEntity>)

	@Query("SELECT * FROM tvshowentity")
	fun getAllShows(): Flow<List<TvShowEntity>>

	@Transaction
	@Query("SELECT * FROM tvshowentity WHERE id = :id")
	suspend fun getShow(id: String): ShowWithSeasons

	@Query("SELECT * FROM tvshowentity WHERE tmdbId = :tmdbId")
	suspend fun getShowByTmdbId(tmdbId: Long): TvShowEntity?

	@Query("DELETE FROM tvshowentity WHERE id = :id")
	suspend fun deleteShowById(id: String)

	@Delete
	suspend fun deleteSeasons(seasons: List<TvSeasonEntity>)

	@Query("SELECT localPaths FROM tvshowentity")
	suspend fun getAllLocalPaths(): List<String>

	@Query("SELECT * FROM tvepisodeentity WHERE id = :id")
	suspend fun getEpisode(id: String): TvEpisodeEntity

	@Query("SELECT * FROM tvseasonentity WHERE id = :seasonId")
	suspend fun getSeason(seasonId: String): TvSeasonEntity
}