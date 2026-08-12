package vadimerenkov.biblioteka.shows.data

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name

class TvFileParser {

	val formats = "(mp4|mkv|avi)$".toRegex()
	val resolution = "([0-9]{3,4}p)".toRegex()
	val quality = "/(?:PPV\\.)?[HP]DTV|(?:HD)?CAM|B[DR]Rip|TS|(?:PPV )?WEB-?DL(?: DVDRip)?|H[dD]Rip|DVDRip|DVDRiP|DVDRIP|CamRip|W[EB]B[rR]ip|[Bb]lu[Rr]ay|DvDScr|hdtv/".toRegex()
	val codec = "/xvid|x264|h\\.?264/i".toRegex()
	val year = "\\b(19|20)\\d{2}\\b".toRegex()
	val audio = "/MP3|DD5\\.?1|Dual[\\- ]Audio|LiNE|DTS|AAC(?:\\.?2\\.0)?|AC3(?:\\.5\\.1)?/".toRegex()
	val brackets = "\\[.*?\\]".toRegex()
	val season = "([Ss]?([0-9]{1,2}))".toRegex()
	val episode = "([Eex]([0-9]{2})(?:[^0-9]|\$))".toRegex()

	fun parseFolder(folder: PlatformFile): Map<String, List<String>> {
		val folders = folder.list()
		val namesWithPaths = mutableMapOf<String, List<String>>()

		folders.forEach { folder ->
			if (folder.isDirectory()) {
				val cleanedName = folder.name
					.replace(brackets, "")
					.replace(quality, "")
					.replace(resolution, "")
					.replace(formats, "")
					.replace(codec, "")
					.replace(year, "")
					.replace(audio, "")
					.replace(season, "")
					.replace(".", " ")
					.replace("_", " ")
					.replaceAfter("  ", "")
					.replace("(", "")
					.replace(")", "")
					.trim()

				namesWithPaths[cleanedName] = namesWithPaths[cleanedName]?.plus(folder.absolutePath()) ?: listOf(folder.absolutePath())
			}
		}

		return namesWithPaths.toMap()
	}

	fun parseTvSeason(path: String): List<TvEpisodeParsed> {
		val file = PlatformFile(path)
		val episodesParsed = mutableListOf<TvEpisodeParsed>()
		if (file.isDirectory()) {
			val episodes = file.list()
			episodes.forEach { file ->
				if (file.isDirectory()) {
					val t = parseTvSeason(file.absolutePath())
					episodesParsed.addAll(t)
				} else if (file.name.contains(formats)) {
					val cleanedName = file.name
						.replace(brackets, "")
						.replace(quality, "")
						.replace(resolution, "")
						.replace(formats, "")
						.replace(codec, "")
						.replace(year, "")
						.replace(audio, "")
						.replace(".", " ")
						.replace("_", " ")
						.replaceAfter("  ", "")
						.replace("(", "")
						.replace(")", "")
						.trim()

					val seasonNumber = season.find(file.name)?.groupValues?.lastOrNull()?.toIntOrNull() ?: 1
					val episodeNumber = episode.find(file.name)?.groupValues?.lastOrNull()?.toIntOrNull()
					val episode = TvEpisodeParsed(seasonNumber, episodeNumber, file.absolutePath(), cleanedName)
					episodesParsed.add(episode)
				}
			}
		}

		return episodesParsed
	}
}