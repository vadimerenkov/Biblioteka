package vadimerenkov.biblioteka.shows.data.tmdb

import kotlinx.serialization.Serializable

@Serializable
data class SeasonEpisodesDto(
	val episodes: List<TvEpisodeDetailsDto>
)
