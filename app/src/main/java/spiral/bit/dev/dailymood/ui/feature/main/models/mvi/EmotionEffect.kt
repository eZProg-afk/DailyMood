package spiral.bit.dev.dailymood.ui.feature.main.models.mvi

sealed class EmotionEffect {
    data class Toast(val msg: Int) : EmotionEffect()
    data class NavigateToDetail(val emotionId: Long) : EmotionEffect()
    object NavigateToSelect : EmotionEffect()
    object NavigateToMain : EmotionEffect()
}