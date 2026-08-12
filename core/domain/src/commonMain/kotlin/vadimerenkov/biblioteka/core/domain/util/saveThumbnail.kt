package vadimerenkov.biblioteka.core.domain.util

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.write

suspend fun saveThumbnail(
	path: String,
	id: String
): String {
	val file = PlatformFile(path)
	if (file.exists()) {
		val compressedBytes = FileKit.compressImage(file)
		val directory = PlatformFile(FileKit.cacheDir, "thumbnails")
		if (!directory.exists()) {
			directory.createDirectories()
		}
		val compressedFile = PlatformFile(FileKit.cacheDir, "/thumbnails/$id.jpg")
		compressedFile.write(compressedBytes)
		println(compressedFile.absolutePath())
		return compressedFile.absolutePath()
	}
	return path
}