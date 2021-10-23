package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.camera.view

import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.emotion.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.camera.models.mvi.CameraEffect
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.camera.models.mvi.CameraState
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val moodRepository: MoodRepository
) : BaseViewModel<CameraState, CameraEffect>() {

    override val container = container<CameraState, CameraEffect>(CameraState(0)) //TODO STATE

    //TODO FILL
}