package vadimerenkov.biblioteka.shows.domain

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result

interface TvRepository {

	suspend fun searchTvShow(query: String): Result<List<TvShow>, DataError.Remote>
	fun getAllShows(): Flow<List<TvShow>>
	suspend fun getShow(id: String): TvShow
	suspend fun getShowFromTmdb(tmdbId: Long): Result<TvShow, DataError.Remote>
	suspend fun parseFolderAndSave(folder: PlatformFile)
	suspend fun checkIfAlreadyExists(show: TvShow): Boolean
	suspend fun updateLocalPaths(paths: List<String>, tmdbId: Long, showId: String)
	suspend fun saveShow(show: TvShow)
	suspend fun fetchSeasonFromTmdb(seasonId: String, seasonNumber: Int, tmdbId: Long): List<TvEpisode>
	suspend fun saveSeasons(seasons: List<TvSeason>)
	suspend fun deleteSeasons(seasons: List<TvSeason>)
	fun assignFolderToSeason(
		folder: PlatformFile,
		season: TvSeason
	): TvSeason
	suspend fun getEpisode(id: String): TvEpisode
	suspend fun saveEpisode(episode: TvEpisode)
	suspend fun getShowBySeasonId(seasonId: String): TvShow
	suspend fun saveEpisodes(episodes: List<TvEpisode>)
}