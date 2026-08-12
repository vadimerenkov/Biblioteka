package vadimerenkov.biblioteka.shows.data

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import vadimerenkov.biblioteka.core.data.database.shows.TvDao
import vadimerenkov.biblioteka.core.data.networking.safeCall
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result
import vadimerenkov.biblioteka.core.domain.util.map
import vadimerenkov.biblioteka.core.domain.util.onFailure
import vadimerenkov.biblioteka.core.domain.util.onSuccess
import vadimerenkov.biblioteka.shows.data.tmdb.SearchedShowResponse
import vadimerenkov.biblioteka.shows.data.tmdb.SeasonEpisodesDto
import vadimerenkov.biblioteka.shows.data.tmdb.ShowDetailsDto
import vadimerenkov.biblioteka.shows.data.tmdb.TvEpisodeDetailsDto
import vadimerenkov.biblioteka.shows.domain.TvEpisode
import vadimerenkov.biblioteka.shows.domain.TvRepository
import vadimerenkov.biblioteka.shows.domain.TvSeason
import vadimerenkov.biblioteka.shows.domain.TvShow
import java.util.UUID

class KtorTvRepository(
	private val httpClient: HttpClient,
	private val tvParser: TvFileParser,
	private val dao: TvDao
): TvRepository {

	val apiKey = BuildKonfig.TMDB_API_KEY

	override suspend fun searchTvShow(query: String): Result<List<TvShow>, DataError.Remote> {
		val response = safeCall<SearchedShowResponse> {
			httpClient.get(
				urlString = "https://api.themoviedb.org/3/search/tv"
			) {
				header("Authorization", "Bearer $apiKey")
				parameter("query", query)
			}
		}
		return response.map { response ->
			response.results.map { it.toShow() }
		}
	}

	override fun getAllShows(): Flow<List<TvShow>> {
		return dao.getAllShows().map { entities ->
			entities.map { it.toShow() }
		}
	}

	override suspend fun getShow(id: String): TvShow {
		return dao.getShow(id).toShow()
	}

	override suspend fun getShowFromTmdb(tmdbId: Long): Result<TvShow, DataError.Remote> {
		val show = newShow("", emptyList())

		return safeCall<ShowDetailsDto> {
			httpClient.get(
				urlString = "https://api.themoviedb.org/3/tv/$tmdbId"
			) {
				header("Authorization", "Bearer $apiKey")
			}
		}.map { it.toShow(show) }
	}

	override suspend fun parseFolderAndSave(folder: PlatformFile) {
		val localPaths = dao.getAllLocalPaths().flatMap { paths ->
			Json.decodeFromString<List<String>>(paths)
		}
		val namesWithPaths = tvParser.parseFolder(folder)
		val tvShows = namesWithPaths.mapNotNull { (name, paths) ->
			if (paths.any { it in localPaths } ) null else {
				newShow(
					title = name,
					localPaths = paths
				)
			}
		}
		dao.saveTvShows(tvShows.map { it.toEntity() })
		tvShows.forEach { show ->
			val detailedShow = parseShow(show)
			if (detailedShow != null) {
				dao.saveTvShow(detailedShow.toEntity())
				dao.saveSeasons(detailedShow.seasons.map { it.toEntity() })
				val episodes = detailedShow.seasons.mapNotNull { season ->
						val dtos = fetchEpisodes(detailedShow.tmdbId!!, season.seasonNumber)
						dtos?.map { dto ->
							dto.toEpisode(season.id)
						}
					}.flatten()

				dao.saveEpisodes(episodes.map { it.toEntity() })
				detailedShow.localPaths.forEach { path ->
					val parsedEpisodes = tvParser.parseTvSeason(path)
					parsedEpisodes.forEach { episode ->
						val seasonId = detailedShow.seasons.find { it.seasonNumber == episode.seasonNumber }?.id ?: detailedShow.seasons.first().id
						val fetchedEpisode = episodes.find { it.seasonId == seasonId && it.episodeNumber == episode.episodeNumber }
						val episodeWithLocalPath = fetchedEpisode?.copy(localPath = episode.localPath)
						if (episodeWithLocalPath != null) {
							dao.saveEpisode(episodeWithLocalPath.toEntity())
						} else {
							/*
							val newEpisode = TvEpisode(
								id = UUID.randomUUID().toString(),
								seasonId = seasonId,
								tmdbId = null,
								title = episode.cleanedName,
								description = null,
								episodeNumber = episode.episodeNumber,
								localPath = episode.localPath,
								posterPath = null
							)
							dao.saveEpisode(newEpisode.toEntity())
							
							 */
						}
					}
				}
			} else {
				val defaultSeason = TvSeason(
					id = UUID.randomUUID().toString(),
					showId = show.id,
					tmdbId = null,
					episodes = emptyList(),
					seasonNumber = 1,
					description = null,
					episodeCount = 0
				)
				dao.saveSeasons(listOf(defaultSeason).map { it.toEntity() })
				show.localPaths.forEach { path ->
					val episodes = tvParser.parseTvSeason(path)
					episodes.forEach { episode ->
						val tvEpisode = TvEpisode(
							id = UUID.randomUUID().toString(),
							seasonId = defaultSeason.id,
							title = episode.cleanedName,
							tmdbId = null,
							description = null,
							episodeNumber = episode.episodeNumber,
							localPath = episode.localPath,
							posterPath = null,
							runtime = null
						)
						dao.saveEpisode(tvEpisode.toEntity())
					}
				}
			}
		}
	}

	override suspend fun checkIfAlreadyExists(show: TvShow): Boolean {
		if (show.tmdbId == null) return false
		val dbShow = dao.getShowByTmdbId(show.tmdbId!!) ?: return false
		return dbShow.id != show.id
	}

	override suspend fun updateLocalPaths(paths: List<String>, tmdbId: Long, showId: String) {
		println("paths is $paths")
		val show = dao.getShowByTmdbId(tmdbId)
		if (show != null) {
			val localPaths = Json.decodeFromString<List<String>>(show.localPaths) + paths
			println("Local paths is $localPaths")
			val newShow = show.copy(localPaths = Json.encodeToString(localPaths))
			dao.saveTvShow(newShow)
			dao.deleteShowById(showId)

			val showWithSeasons = dao.getShow(show.id)
			val seasons = showWithSeasons.seasons.map { it.toSeason() }
			val seasonEpisodes = seasons.flatMap { it.episodes }

			paths.forEach { path ->
				val episodes = tvParser.parseTvSeason(path)
				episodes.forEach { episode ->
					val seasonId = seasons.find { it.seasonNumber == episode.seasonNumber }?.id ?: seasons.first().id
					val fetchedEpisode = seasonEpisodes.find { it.seasonId == seasonId && it.episodeNumber == episode.episodeNumber }
					val episodeWithLocalPath = fetchedEpisode?.copy(localPath = episode.localPath)
					if (episodeWithLocalPath != null) {
						dao.saveEpisode(episodeWithLocalPath.toEntity())
					} else {
						val newEpisode = TvEpisode(
							id = UUID.randomUUID().toString(),
							seasonId = seasonId,
							tmdbId = null,
							title = episode.cleanedName,
							description = null,
							episodeNumber = episode.episodeNumber,
							localPath = episode.localPath,
							posterPath = null,
							runtime = null
						)
						dao.saveEpisode(newEpisode.toEntity())
					}
				}
			}
		}
	}

	override suspend fun saveShow(show: TvShow) {
		dao.saveTvShow(show.toEntity())
	}

	override suspend fun fetchSeasonFromTmdb(
		seasonId: String,
		seasonNumber: Int,
		tmdbId: Long
	): List<TvEpisode> {
		val episodes = fetchEpisodes(tmdbId, seasonNumber) ?: return emptyList()
		return episodes.map { it.toEpisode(seasonId) }
	}

	override suspend fun saveSeasons(seasons: List<TvSeason>) {
		val episodes = seasons.flatMap { it.episodes }
		dao.saveSeasons(seasons.map { it.toEntity() })
		dao.saveEpisodes(episodes.map { it.toEntity() })
	}

	override suspend fun deleteSeasons(seasons: List<TvSeason>) {
		dao.deleteSeasons(seasons.map { it.toEntity() })
	}

	override fun assignFolderToSeason(
		folder: PlatformFile,
		season: TvSeason
	): TvSeason {
		val formats = "(mp4|mkv|avi)$".toRegex()
		val episodeFiles = folder
			.list()
			.filter { it.name.contains(formats) }
			.zip(season.episodes)
			.map { (file, episode) ->
				episode.copy(localPath = file.absolutePath())
			}

		return season.copy(episodes = episodeFiles)
	}

	override suspend fun getEpisode(id: String): TvEpisode {
		return dao.getEpisode(id).toEpisode()
	}

	override suspend fun saveEpisode(episode: TvEpisode) {
		dao.saveEpisode(episode.toEntity())
	}

	override suspend fun getShowBySeasonId(seasonId: String): TvShow {
		val season = dao.getSeason(seasonId)
		val show = dao.getShow(season.showId)
		return show.toShow()
	}

	override suspend fun saveEpisodes(episodes: List<TvEpisode>) {
		dao.saveEpisodes(episodes.map { it.toEntity() })
	}

	private suspend fun fetchEpisodes(
		showTmdbId: Long,
		seasonNumber: Int,
	): List<TvEpisodeDetailsDto>? {
		safeCall<SeasonEpisodesDto> {
			httpClient.get(
				urlString = "https://api.themoviedb.org/3/tv/$showTmdbId/season/$seasonNumber"
			) {
				header("Authorization", "Bearer $apiKey")
			}

		}
			.onSuccess { dto ->
				return dto.episodes
			}
			.onFailure {
				return null
			}

		return null
	}

	private suspend fun parseShow(show: TvShow): TvShow? {
		searchTvShow(show.title)
			.onSuccess { shows ->
				val tmdbId = shows.firstOrNull()?.tmdbId ?: return null
				return getDetailsForShow(show, tmdbId)
			}
			.onFailure {
				return null
			}
		return null
	}

	private suspend fun getDetailsForShow(
		show: TvShow,
		tmdbId: Long
	): TvShow? {
		safeCall<ShowDetailsDto> {
			httpClient.get(
				urlString = "https://api.themoviedb.org/3/tv/$tmdbId"
			) {
				header("Authorization", "Bearer $apiKey")
			}
		}
			.onSuccess {
				return it.toShow(show)
			}
			.onFailure {
				return null
			}

		return null
	}

	private fun newShow(title: String, localPaths: List<String>): TvShow {
		return TvShow(
			id = UUID.randomUUID().toString(),
			tmdbId = null,
			title = title,
			seasons = emptyList(),
			localPaths = localPaths,
			backdropPath = null,
			posterPath = null,
			episodeRuntime = null,
			firstAirDate = null,
			genres = emptyList(),
			lastAirDate = null,
			isOngoing = null,
			networks = emptyList(),
			originCountries = emptyList(),
			originalTitle = null,
			tagline = null,
			avgRating = null,
			ratingsCount = null,
			rating = null,
			description = null
		)
	}
}