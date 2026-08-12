package vadimerenkov.biblioteka

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import biblioteka.desktopapp.generated.resources.Res
import biblioteka.desktopapp.generated.resources.app_icon
import io.github.vinceglb.filekit.FileKit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import vadimerenkov.biblioteka.books.data.di.booksDataModule
import vadimerenkov.biblioteka.books.presentation.di.booksPresentationModule
import vadimerenkov.biblioteka.core.data.di.coreDataModule
import vadimerenkov.biblioteka.core.domain.di.coreDomainModule
import vadimerenkov.biblioteka.core.domain.settings.Settings
import vadimerenkov.biblioteka.core.presentation.theme.BibliotekaTheme
import vadimerenkov.biblioteka.movies.data.di.moviesDataModule
import vadimerenkov.biblioteka.movies.presentation.di.moviesPresentationModule
import vadimerenkov.biblioteka.settings.restoreWindowState
import vadimerenkov.biblioteka.settings.saveWindowState
import vadimerenkov.biblioteka.shows.data.di.showsDataModule
import vadimerenkov.biblioteka.shows.presentation.di.showsPresentationModule

fun main() {

	startKoin {
		modules(
			booksPresentationModule,
			booksDataModule,
			coreDataModule,
			coreDomainModule,
			moviesDataModule,
			moviesPresentationModule,
			showsDataModule,
			showsPresentationModule
		)
	}

	val settings: Settings = get().get()
	val applicationScope: CoroutineScope = get().get()

	val prefsWindowState = runBlocking { settings.restoreWindowState() }

	FileKit.init("Biblioteka")

	application {
		val windowState = remember { prefsWindowState } ?: rememberWindowState()

		BibliotekaTheme(
			darkTheme = true
		) {
			Window(
				icon = painterResource(Res.drawable.app_icon),
				state = windowState,
				onCloseRequest = {
					applicationScope.launch {
						settings.saveWindowState(windowState)
						exitApplication()
					}
				},
				title = "Biblioteka",
			) {
				BibliotekaApp()
			}
		}
	}
}