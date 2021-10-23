package spiral.bit.dev.dailymood.ui.feature.main.models.mvi

import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class EmotionEffect : SideEffectMarker {
    data class Toast(val msg: Int) : EmotionEffect()
    data class ShowSnackbar(val msg: Int, val moodEntity: MoodEntity? = null) : EmotionEffect()
    data class NavigateToDetail(val emotionId: Long) : EmotionEffect()
    object NavigateToSelect : EmotionEffect()
    object NavigateToMain : EmotionEffect()
}