package spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi

import android.net.Uri
import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.SliderItem

data class ManuallyState(
    val imageUri: Uri?,
    val sliderItems: List<SliderItem>
) : StateMarker