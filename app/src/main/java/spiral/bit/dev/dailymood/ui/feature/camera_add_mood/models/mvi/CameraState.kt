package spiral.bit.dev.dailymood.ui.feature.camera_add_mood.models.mvi

import android.net.Uri
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class CameraState(
    val smileProbability: Float?,
    val capturedSelfieUri: Uri?
) : StateMarker