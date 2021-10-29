package spiral.bit.dev.dailymood.ui.feature.realtime_add_mood.models.mvi

import androidx.annotation.StringRes
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class RealtimeEffect : SideEffectMarker {
    object NavigateToMain : RealtimeEffect()
    class Toast(@StringRes val msg: Int): RealtimeEffect()
    class ExceptionHappened(val error: Throwable) : RealtimeEffect()
}