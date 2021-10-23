package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.camera.view

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmotionCreationByCameraBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.camera.models.mvi.CameraEffect
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.camera.models.mvi.CameraState

class EmotionCreationByCameraFragment :
    BaseFragment<CameraState, CameraEffect, FragmentEmotionCreationByCameraBinding>(
        FragmentEmotionCreationByCameraBinding::inflate
    ) {

    override val viewModel: CameraViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun renderState(state: CameraState) = binding {
        //TODO STATE
    }

    override fun handleSideEffect(sideEffect: CameraEffect) = binding {
        when (sideEffect) {
            //TODO EFFECT
        }
    }


}