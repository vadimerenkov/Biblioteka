package vadimerenkov.biblioteka.shows.presentation.util

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import vadimerenkov.biblioteka.shows.domain.TvEpisode

fun TvEpisode.existsLocally(): Boolean {
	return this.localPath != null && PlatformFile(this.localPath!!).exists()
}