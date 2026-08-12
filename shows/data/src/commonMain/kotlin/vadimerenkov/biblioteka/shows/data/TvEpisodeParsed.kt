package vadimerenkov.biblioteka.shows.data

data class TvEpisodeParsed(
	val seasonNumber: Int?,
	val episodeNumber: Int?,
	val localPath: String,
	val cleanedName: String
)