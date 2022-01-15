package spiral.bit.dev.dailymood.ui.feature.creationMood.cameraAddMood.models.mvi

import android.net.Uri

data class CameraState(
    val smileProbability: Float?,
    val capturedSelfieUri: Uri?,
    val flashMode: Int
)