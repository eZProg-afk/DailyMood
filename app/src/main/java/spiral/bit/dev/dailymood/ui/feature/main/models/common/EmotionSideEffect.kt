package spiral.bit.dev.dailymood.ui.feature.main.models.common

import spiral.bit.dev.dailymood.data.Emotion
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class EmotionSideEffect : SideEffectMarker {
    data class Toast(val msg: Int) : EmotionSideEffect()
    data class ShowSnackbar(val msg: Int, val emotion: Emotion? = null) : EmotionSideEffect()
    data class NavigateToDetail(val emotion: Emotion) : EmotionSideEffect()
    object NavigateToCreate : EmotionSideEffect()
    object NavigateToMain : EmotionSideEffect()
}