package vadimerenkov.biblioteka.core.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ca.gosyer.appdirs.AppDirs
import kotlinx.coroutines.Dispatchers
import vadimerenkov.biblioteka.core.data.BuildKonfig
import vadimerenkov.biblioteka.core.data.database.books.BookEntity
import vadimerenkov.biblioteka.core.data.database.books.BooksDao
import vadimerenkov.biblioteka.core.data.database.movies.MovieEntity
import vadimerenkov.biblioteka.core.data.database.movies.MoviesDao
import vadimerenkov.biblioteka.core.data.database.shows.TvDao
import vadimerenkov.biblioteka.core.data.database.shows.TvEpisodeEntity
import vadimerenkov.biblioteka.core.data.database.shows.TvSeasonEntity
import vadimerenkov.biblioteka.core.data.database.shows.TvShowEntity
import java.io.File

@Database(
	entities = [
		BookEntity::class,
		MovieEntity::class,
		TvShowEntity::class,
		TvEpisodeEntity::class,
		TvSeasonEntity::class
	],
	version = 3,
	exportSchema = true,
	autoMigrations = [
		AutoMigration(1, 2),
		AutoMigration(2, 3)
	]
)
abstract class MediaDatabase: RoomDatabase() {

	abstract val booksDao: BooksDao
	abstract val moviesDao: MoviesDao
	abstract val tvDao: TvDao

	companion object {
		fun initialize(): MediaDatabase {
			val appdirs = AppDirs {
				appName = "Biblioteka"
			}
			val isDebug = BuildKonfig.isDebug

			val path = if (isDebug) appdirs.getUserCacheDir() else appdirs.getUserDataDir()
			val file = File(path, "database/media.db")
			return Room.databaseBuilder<MediaDatabase>(file.absolutePath)
				.setDriver(BundledSQLiteDriver())
				.setQueryCoroutineContext(Dispatchers.IO)
				.fallbackToDestructiveMigrationOnDowngrade(true)
				.build()
		}
	}
}