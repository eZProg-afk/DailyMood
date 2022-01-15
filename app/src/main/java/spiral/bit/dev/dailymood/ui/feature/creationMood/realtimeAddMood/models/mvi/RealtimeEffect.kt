package spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.models.mvi

import androidx.annotation.StringRes

sealed class RealtimeEffect {
    object NavigateToMain : RealtimeEffect()
    class Toast(@StringRes val msg: Int): RealtimeEffect()
    class ExceptionHappened(val error: Throwable) : RealtimeEffect()
}