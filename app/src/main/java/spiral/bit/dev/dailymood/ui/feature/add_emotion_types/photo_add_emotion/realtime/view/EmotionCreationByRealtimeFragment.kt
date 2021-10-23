package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmotionCreationByRealtimeBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.models.mvi.RealtimeEffect
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.models.mvi.RealtimeState
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.view.EmotionCreationByPhotoFragmentDirections

class EmotionCreationByRealtimeFragment :
    BaseFragment<RealtimeState, RealtimeEffect, FragmentEmotionCreationByRealtimeBinding>(
        FragmentEmotionCreationByRealtimeBinding::inflate
    ) {

    private val emotionTypeMapper = EmotionTypeMapper()
    private val faceMoodResolver = FaceMoodResolver()
    override val viewModel: RealtimeViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val permission = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) setUpCamera()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (handlePermissions()) setUpCamera()
        setUpClicks()
    }

    private fun setUpClicks() = binding {
        addEmotionButton.setOnClickListener {
            viewModel.takeEmotion()
        }
    }

    private fun handlePermissions() =
        if (requireContext().hasPermissions(Manifest.permission.CAMERA)) {
            true
        } else {
            permission.launch(Manifest.permission.CAMERA)
            false
        }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setUpCamera() = binding {
        val cameraExecutor = ContextCompat.getMainExecutor(requireContext())
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageFaceEmotionAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, { imageProxy ->
                        imageProxy.image?.let { mediaImage ->
                            InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            ).run {
                                viewModel.detectFace(this) { imageProxy.close() }
                            }
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            runCatching {
                cameraProvider.apply {
                    unbindAll()
                    bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview,
                        imageFaceEmotionAnalyzer
                    )
                }
            }.onFailure { throwable ->
                Logger.logError(throwable)
            }
        }, cameraExecutor)
    }

    override fun renderState(state: RealtimeState) = binding {
        val moodValue = faceMoodResolver.resolveEmotionType(state.smileProbability)
        val emotionType = emotionTypeMapper.mapToEmotionType(moodValue)
        smileyRatingView.setRating(emotionType.smileyRating)
    }

    override fun handleSideEffect(sideEffect: RealtimeEffect) = binding {
        when (sideEffect) {
            is RealtimeEffect.NavigateToMain -> {
                EmotionCreationByPhotoFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is RealtimeEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
            is RealtimeEffect.ExceptionHappened -> {
                Logger.logError(sideEffect.error)
            }
        }
    }
}