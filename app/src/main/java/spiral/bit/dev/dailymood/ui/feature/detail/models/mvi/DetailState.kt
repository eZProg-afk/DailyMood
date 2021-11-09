package spiral.bit.dev.dailymood.ui.feature.detail.models.mvi

import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class DetailState(
    val moodEntity: MoodEntity?
) : StateMarker