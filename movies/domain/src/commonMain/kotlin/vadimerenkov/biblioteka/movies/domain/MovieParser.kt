package vadimerenkov.biblioteka.movies.domain

import io.github.vinceglb.filekit.PlatformFile

interface MovieParser {

	fun parse(directory: PlatformFile): Map<PlatformFile, String>
}