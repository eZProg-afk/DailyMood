package spiral.bit.dev.dailymood.ui.feature.create.models.mvi

import android.net.Uri
import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.feature.create.models.SliderItem

data class CreateEmotionState(
    val imageUri: Uri?,
    val sliderItems: List<SliderItem>
) : StateMarker