package vadimerenkov.biblioteka.movies.data

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import vadimerenkov.biblioteka.movies.domain.MovieParser

class MovieFilesParser(

): MovieParser {

	val formats = "(mp4|mkv|avi)$".toRegex()
	val resolution = "([0-9]{3,4}p)".toRegex()
	val quality = "/(?:PPV\\.)?[HP]DTV|(?:HD)?CAM|B[DR]Rip|TS|(?:PPV )?WEB-?DL(?: DVDRip)?|H[dD]Rip|DVDRip|DVDRiP|DVDRIP|CamRip|W[EB]B[rR]ip|[Bb]lu[Rr]ay|DvDScr|hdtv/".toRegex()
	val codec = "/xvid|x264|h\\.?264/i".toRegex()
	val year = "\\b(19|20)\\d{2}\\b".toRegex()
	val audio = "/MP3|DD5\\.?1|Dual[\\- ]Audio|LiNE|DTS|AAC(?:\\.?2\\.0)?|AC3(?:\\.5\\.1)?/".toRegex()
	val brackets = "\\[.*?\\]".toRegex()

	override fun parse(directory: PlatformFile): Map<PlatformFile, String> {
		val fileNames = mutableMapOf<PlatformFile, String>()
		val files = directory.list()

		files.forEach { file ->
			if (file.isDirectory()) {
				val names = parse(file)
				fileNames.putAll(names)
			}
			else {
				if (file.name.contains(formats)) {
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

					fileNames[file] = cleanedName
					println(cleanedName)
				}
			}
		}

		return fileNames
	}
}