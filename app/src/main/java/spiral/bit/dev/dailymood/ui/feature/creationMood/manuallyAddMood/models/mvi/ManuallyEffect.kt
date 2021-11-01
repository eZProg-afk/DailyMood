package spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi

import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class ManuallyEffect : SideEffectMarker {
    data class Toast(val msg: Int) : ManuallyEffect()
    data class ShowSnackbar(val msg: Int, val moodEntity: MoodEntity? = null) : ManuallyEffect()
    object NavigateToMain : ManuallyEffect()
}