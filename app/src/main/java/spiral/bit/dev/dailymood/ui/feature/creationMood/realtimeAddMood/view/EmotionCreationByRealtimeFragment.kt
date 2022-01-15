package spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.FlowPreview
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentMoodCreationByRealtimeBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.hasPermissions
import spiral.bit.dev.dailymood.ui.base.extensions.toast
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.creationMood.photoAddMood.view.EmotionCreationByPhotoFragmentDirections
import spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.models.mvi.RealtimeEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.models.mvi.RealtimeState
import kotlin.concurrent.thread

@FlowPreview
class EmotionCreationByRealtimeFragment :
    BaseFragment<RealtimeState, RealtimeEffect, FragmentMoodCreationByRealtimeBinding>(
        FragmentMoodCreationByRealtimeBinding::inflate
    ) {

    private val imageCapture by lazy { ImageCapture.Builder().build() }
    private val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private val emotionTypeMapper = MoodTypeMapper()
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
                        thread {
                            Thread.sleep(400L) //awful camerax api ;(
                            imageProxy.image?.let { mediaImage ->
                                InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                ).run {
                                    viewModel.detectFace(requireActivity(), imageCapture, this) {
                                        imageProxy.close()
                                    }
                                }
                            }
                        }
                    })
                }

            runCatching {
                cameraProvider.apply {
                    unbindAll()
                    bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview,
                        imageFaceEmotionAnalyzer,
                        imageCapture
                    )
                }
            }.onFailure { throwable ->
                Logger.logError(throwable)
            }
        }, cameraExecutor)
    }

    override fun renderState(state: RealtimeState) = binding {
        val moodValue = faceMoodResolver.resolveEmotionType(state.smileProbability)
        val emotionType = moodValue?.let { emotionTypeMapper.mapToMoodType(it) }
        emotionType?.smileyRating?.let { smileyRatingView.setCurrentRateStatus(it) }
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