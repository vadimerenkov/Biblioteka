package vadimerenkov.biblioteka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import androidx.navigation3.ui.NavDisplay
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.about
import biblioteka.core.presentation.generated.resources.book_icon
import biblioteka.core.presentation.generated.resources.books
import biblioteka.core.presentation.generated.resources.movie
import biblioteka.core.presentation.generated.resources.movies
import biblioteka.core.presentation.generated.resources.new_version_available
import biblioteka.core.presentation.generated.resources.tv_show
import biblioteka.core.presentation.generated.resources.tv_shows
import biblioteka.desktopapp.generated.resources.app_icon
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.update_checker.UpdateChecker
import vadimerenkov.biblioteka.books.presentation.BooksScreen
import vadimerenkov.biblioteka.core.presentation.about.AboutScreen
import vadimerenkov.biblioteka.movies.presentation.MoviesScreen
import vadimerenkov.biblioteka.navigation.AboutRoute
import vadimerenkov.biblioteka.navigation.BooksRoute
import vadimerenkov.biblioteka.navigation.MoviesRoute
import vadimerenkov.biblioteka.navigation.ShowsRoute
import vadimerenkov.biblioteka.shows.presentation.ShowsScreen

@Composable
fun BibliotekaApp() {
	val backStack = remember { mutableStateListOf<NavKey>(BooksRoute) }

	setSingletonImageLoaderFactory { context ->
		ImageLoader.Builder(context)
			.components { addPlatformFileSupport() }
			.build()
	}

	var showNewVersionBadge by remember { mutableStateOf(false) }

	LaunchedEffect(true) {
		showNewVersionBadge = UpdateChecker.checkForUpdates()
	}

	PermanentNavigationDrawer(
		drawerContent = {
			PermanentDrawerSheet(
				drawerContainerColor = MaterialTheme.colorScheme.surface,
				drawerTonalElevation = 12.dp,
				modifier = Modifier
					.width(IntrinsicSize.Max)
			) {
				NavigationDrawerItem(
					shape = RectangleShape,
					icon = {
						Icon(
							painter = painterResource(Res.drawable.book_icon),
							contentDescription = null,
							tint = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					label = {
						Text(
							text = stringResource(Res.string.books),
							fontSize = 18.sp,
							color = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					selected = backStack.lastOrNull() is BooksRoute,
					onClick = {
						backStack.clear()
						backStack.add(BooksRoute)
					}
				)
				NavigationDrawerItem(
					shape = RectangleShape,
					icon = {
						Icon(
							painter = painterResource(Res.drawable.movie),
							contentDescription = null,
							tint = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					label = {
						Text(
							text = stringResource(Res.string.movies),
							fontSize = 18.sp,
							color = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					selected = backStack.lastOrNull() is MoviesRoute,
					onClick = {
						backStack.clear()
						backStack.add(MoviesRoute)
					}
				)
				NavigationDrawerItem(
					shape = RectangleShape,
					icon = {
						Icon(
							painter = painterResource(Res.drawable.tv_show),
							contentDescription = null,
							tint = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					label = {
						Text(
							text = stringResource(Res.string.tv_shows),
							fontSize = 18.sp,
							color = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					selected = backStack.lastOrNull() is ShowsRoute,
					onClick = {
						backStack.clear()
						backStack.add(ShowsRoute)
					}
				)
				Spacer(modifier = Modifier.weight(1f))
				NavigationDrawerItem(
					shape = RectangleShape,
					icon = {
						Icon(
							imageVector = Icons.Default.Info,
							contentDescription = null,
							tint = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					label = {
						Text(
							text = stringResource(Res.string.about),
							fontSize = 18.sp,
							color = MaterialTheme.colorScheme.onSecondaryContainer
						)
					},
					selected = backStack.lastOrNull() is AboutRoute,
					onClick = {
						backStack.add(AboutRoute)
					},
					badge = {
						if (showNewVersionBadge) {
							Box(
								modifier = Modifier
									.background(
										color = MaterialTheme.colorScheme.primary,
										shape = RoundedCornerShape(8.dp)
									)
									.padding(vertical = 4.dp, horizontal = 8.dp)
							) {
								Text(
									text = stringResource(Res.string.new_version_available),
									color = MaterialTheme.colorScheme.onPrimary
								)
							}
						}
					}
				)
			}
		}
	) {


		NavDisplay(
			backStack = backStack,
			sceneStrategies = listOf(DialogSceneStrategy()),
			entryDecorators = listOf(
				rememberSaveableStateHolderNavEntryDecorator(),
				rememberViewModelStoreNavEntryDecorator()
			),
			entryProvider = entryProvider {
				entry<BooksRoute> {
					BooksScreen()
				}
				entry<MoviesRoute> {
					MoviesScreen()
				}
				entry<ShowsRoute> {
					ShowsScreen()
				}
				entry<AboutRoute>(metadata = dialog()) {
					Window(
						title = stringResource(Res.string.about),
						icon = painterResource(biblioteka.desktopapp.generated.resources.Res.drawable.app_icon),
						onCloseRequest = {
							backStack.removeLastOrNull()
						},
						state = WindowState(
							position = WindowPosition.Aligned(Alignment.Center)
						)
					) {
						AboutScreen()
					}
				}
			}
		)
	}
}