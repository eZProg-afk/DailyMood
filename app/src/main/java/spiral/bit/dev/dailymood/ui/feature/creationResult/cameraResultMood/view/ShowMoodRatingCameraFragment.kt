package spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.view

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentShowMoodRatingCameraBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi.MoodCameraEffect
import spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi.MoodCameraState

class ShowMoodRatingCameraFragment :
    BaseFragment<MoodCameraState, MoodCameraEffect, FragmentShowMoodRatingCameraBinding>(
        FragmentShowMoodRatingCameraBinding::inflate
    ) {

    private val smilingProbabilities by lazy { args.smilingProbabilities }
    private val photoPath by lazy { args.photoPath.toUri() }
    private val args: ShowMoodRatingCameraFragmentArgs by navArgs()
    override val viewModel: ShowMoodRatingViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        setUpViews()
        viewModel.resolveMood(smilingProbabilities)
    }

    private fun setUpViews() = binding {
        resultPhotoImageView.setImageURI(photoPath)
    }

    private fun setUpClicks() = binding {
        retakePhotoButton.setOnClickListener {
            viewModel.retakePhoto()
        }

        saveResultButton.setOnClickListener {
            viewModel.saveMoodEntity(smilingProbabilities)
        }
    }

    override fun renderState(state: MoodCameraState) = binding {
        state.smileRating?.let { smileyRatingView.setCurrentRateStatus(it) }
    }

    override fun handleSideEffect(sideEffect: MoodCameraEffect) {
        when (sideEffect) {
            MoodCameraEffect.NavigateToCameraAddMood -> {
                findNavController().popBackStack()
            }
            MoodCameraEffect.NavigateToMain -> {
                ShowMoodRatingCameraFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
        }
    }
}