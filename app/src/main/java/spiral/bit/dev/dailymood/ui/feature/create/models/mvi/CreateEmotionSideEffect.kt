package spiral.bit.dev.dailymood.ui.feature.create.models.mvi

import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class CreateEmotionSideEffect : SideEffectMarker {
    data class Toast(val msg: Int) : CreateEmotionSideEffect()
    data class ShowSnackbar(val msg: Int, val moodEntity: MoodEntity? = null) : CreateEmotionSideEffect()
    object NavigateToMain : CreateEmotionSideEffect()
}