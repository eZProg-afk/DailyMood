package spiral.bit.dev.dailymood.ui.feature.creationMood.cameraAddMood.models.mvi

import android.net.Uri
import androidx.camera.core.ImageCapture
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class CameraState(
    val smileProbability: Float?,
    val capturedSelfieUri: Uri?,
    val flashMode: Int
) : StateMarker