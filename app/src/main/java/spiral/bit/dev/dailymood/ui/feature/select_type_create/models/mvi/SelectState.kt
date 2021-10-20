package spiral.bit.dev.dailymood.ui.feature.select_type_create.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker

data class SelectState(
    val selectionTypeItems: List<SelectionTypeItem>
) : StateMarker