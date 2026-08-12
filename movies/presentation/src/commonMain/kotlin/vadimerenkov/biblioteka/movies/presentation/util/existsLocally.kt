package vadimerenkov.biblioteka.movies.presentation.util

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import vadimerenkov.biblioteka.movies.domain.Movie

fun Movie.existsLocally(): Boolean {
	return this.localPath != null && PlatformFile(localPath!!).exists()
}

fun List<Movie>.filterLocal(showOnlyLocal: Boolean): List<Movie> {
	return if (showOnlyLocal) {
		filter { it.existsLocally() }
	} else this
}