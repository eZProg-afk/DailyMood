package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.models.mvi

import androidx.annotation.StringRes
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class RealtimeEffect : SideEffectMarker {
    object NavigateToMain : RealtimeEffect()
    class Toast(@StringRes val msg: Int): RealtimeEffect()
    class ExceptionHappened(val error: Throwable) : RealtimeEffect()

    object AddEmotionByGallery : RealtimeEffect()
    object AddEmotionByCamera : RealtimeEffect()
    object AddEmotionByRealtime : RealtimeEffect()
}