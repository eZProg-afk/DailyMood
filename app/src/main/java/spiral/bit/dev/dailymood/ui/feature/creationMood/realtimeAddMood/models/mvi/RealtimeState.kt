package spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.models.mvi

import android.net.Uri
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class RealtimeState(
    val smileProbability: Float?,
    val currentInputImage: Uri?
) : StateMarker