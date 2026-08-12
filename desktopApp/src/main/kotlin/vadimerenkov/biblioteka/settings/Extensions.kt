package vadimerenkov.biblioteka.settings

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import vadimerenkov.biblioteka.core.domain.settings.Settings

private val WINDOW_PLACEMENT = stringPreferencesKey("WINDOW_PLACEMENT")
private val WINDOW_MINIMIZED = booleanPreferencesKey("WINDOW_MINIMIZED")
private val WINDOW_POSITION_X = floatPreferencesKey("WINDOW_POSITION_X")
private val WINDOW_POSITION_Y = floatPreferencesKey("WINDOW_POSITION_Y")
private val WINDOW_SIZE_HEIGHT = floatPreferencesKey("WINDOW_SIZE_HEIGHT")
private val WINDOW_SIZE_WIDTH = floatPreferencesKey("WINDOW_SIZE_WIDTH")

suspend fun Settings.saveWindowState(state: WindowState) {
	saveSetting(WINDOW_PLACEMENT, state.placement.name)
	saveSetting(WINDOW_MINIMIZED, state.isMinimized)
	saveSetting(WINDOW_POSITION_X, state.position.x.value)
	saveSetting(WINDOW_POSITION_Y, state.position.y.value)
	saveSetting(WINDOW_SIZE_WIDTH, state.size.width.value)
	saveSetting(WINDOW_SIZE_HEIGHT, state.size.height.value)
}

suspend fun Settings.restoreWindowState(): WindowState? {

	val placement = getSetting(WINDOW_PLACEMENT) ?: return null
	val isMinimized = getSetting(WINDOW_MINIMIZED) ?: return null
	val positionX = getSetting(WINDOW_POSITION_X) ?: return null
	val positionY = getSetting(WINDOW_POSITION_Y) ?: return null
	val height = getSetting(WINDOW_SIZE_HEIGHT) ?: return null
	val width = getSetting(WINDOW_SIZE_WIDTH) ?: return null

	return WindowState(
		placement = WindowPlacement.valueOf(placement),
		isMinimized = isMinimized,
		position = WindowPosition(positionX.dp, positionY.dp),
		size = DpSize(width.dp, height.dp)
	)
}

