package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.models.mvi

import androidx.annotation.StringRes
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class PhotoEffect : SideEffectMarker {
    object NavigateToMain : PhotoEffect()
    class Toast(@StringRes val msg: Int): PhotoEffect()
    class ExceptionHappened(val error: Throwable) : PhotoEffect()
}