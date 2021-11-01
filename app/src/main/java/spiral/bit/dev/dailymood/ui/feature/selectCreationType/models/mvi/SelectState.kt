package spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.SelectionTypeItem

data class SelectState(
    val selectionTypeItems: List<SelectionTypeItem>
) : StateMarker