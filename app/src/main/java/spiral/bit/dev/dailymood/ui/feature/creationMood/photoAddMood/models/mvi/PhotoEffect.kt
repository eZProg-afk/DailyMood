package spiral.bit.dev.dailymood.ui.feature.creationMood.photoAddMood.models.mvi

import android.net.Uri
import androidx.annotation.StringRes

sealed class PhotoEffect {
    object AddEmotionByGallery : PhotoEffect()
    object AddEmotionByCamera : PhotoEffect()
    object AddEmotionByRealtime : PhotoEffect()
    class Toast(@StringRes val msg: Int) : PhotoEffect()
    class ExceptionHappened(val error: Throwable) : PhotoEffect()
    data class NavigateToShowMoodRating(val smilingProbabilities: Float, val galleryImage: Uri) : PhotoEffect()
    object NavigateBack : PhotoEffect()
}