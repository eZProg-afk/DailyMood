package spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.mvi

import android.net.Uri
import androidx.annotation.StringRes
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class PhotoEffect : SideEffectMarker {
    object AddEmotionByGallery : PhotoEffect()
    object AddEmotionByCamera : PhotoEffect()
    object AddEmotionByRealtime : PhotoEffect()
    class Toast(@StringRes val msg: Int) : PhotoEffect()
    class ExceptionHappened(val error: Throwable) : PhotoEffect()
    data class NavigateToShowMoodRating(
        val smilingProbabilities: Float,
        val galleryImage: Uri
    ) : PhotoEffect()
}