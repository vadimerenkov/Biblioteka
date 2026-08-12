package vadimerenkov.biblioteka

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.book_icon
import biblioteka.core.presentation.generated.resources.books
import biblioteka.core.presentation.generated.resources.movie
import biblioteka.core.presentation.generated.resources.movies
import biblioteka.core.presentation.generated.resources.tv_show
import biblioteka.core.presentation.generated.resources.tv_shows
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.books.presentation.BooksScreen
import vadimerenkov.biblioteka.movies.presentation.MoviesScreen
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
			}
		)
	}
}