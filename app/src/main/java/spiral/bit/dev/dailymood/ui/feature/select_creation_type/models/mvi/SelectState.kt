package spiral.bit.dev.dailymood.ui.feature.select_creation_type.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker

data class SelectState(
    val selectionTypeItems: List<SelectionTypeItem>
) : StateMarker