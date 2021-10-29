package spiral.bit.dev.dailymood.ui.feature.camera_result_mood.models.mvi

import com.hadi.emojiratingbar.RateStatus
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class MoodCameraState(
    val smileRating: RateStatus?
) : StateMarker