package vadimerenkov.biblioteka.shows.presentation.list

import io.github.vinceglb.filekit.PlatformFile

sealed interface ShowListAction {
	data class OnFolderSelected(val folder: PlatformFile): ShowListAction
	data class OnShowClick(val id: String): ShowListAction
	data class OnEditShowClick(val id: String): ShowListAction
	data class ViewChange(val view: ShowView): ShowListAction
	data class SortingChange(val sorting: SortingBy): ShowListAction
	data object OnAddShowClick: ShowListAction
}