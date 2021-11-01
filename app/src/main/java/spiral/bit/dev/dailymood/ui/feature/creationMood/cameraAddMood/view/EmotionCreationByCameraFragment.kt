package spiral.bit.dev.dailymood.ui.feature.creationMood.cameraAddMood.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentMoodCreationByCameraBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.hasPermissions
import spiral.bit.dev.dailymood.ui.base.extensions.safeLet
import spiral.bit.dev.dailymood.ui.base.extensions.switchLens
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.feature.creationMood.cameraAddMood.models.mvi.CameraEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.cameraAddMood.models.mvi.CameraState

class EmotionCreationByCameraFragment :
    BaseFragment<CameraState, CameraEffect, FragmentMoodCreationByCameraBinding>(
        FragmentMoodCreationByCameraBinding::inflate
    ) {

    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private val imageCapture by lazy { ImageCapture.Builder().build() }
    private lateinit var camera: Camera
    override val viewModel: CameraViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) setUpCamera()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (handleCameraPermissions()) setUpCamera()
        setUpClicks()
    }

    private fun setUpClicks() = binding {
        switchCameraImageView.setOnClickListener {
            cameraSelector.switchLens().run {
                setUpCamera()
            }
        }

        takePhotoImageView.setOnClickListener {
            viewModel.takePhoto(imageCapture, requireActivity())
        }

        flashToggleImageView.setOnClickListener {
            viewModel.toggleFlash(requireContext(), camera)
        }
    }

    @SuppressLint("UnsafeOptInUsageError") //TODO NORMAL ANNOTATiON
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

            runCatching {
                cameraProvider.apply {
                    unbindAll()
                    camera = bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                }
            }.onFailure { throwable ->
                Logger.logError(throwable)
            }
        }, cameraExecutor)
    }

    private fun handleCameraPermissions() =
        if (requireContext().hasPermissions(Manifest.permission.CAMERA)) {
            true
        } else {
            cameraPermission.launch(Manifest.permission.CAMERA)
            false
        }

    override fun renderState(state: CameraState) = binding {
        safeLet(state.smileProbability, state.capturedSelfieUri) { _, _ ->
            viewModel.showResultPhoto()
        }
    }

    override fun handleSideEffect(sideEffect: CameraEffect) = binding {
        when (sideEffect) {
            is CameraEffect.NavigateToShowMoodRating -> {
                EmotionCreationByCameraFragmentDirections.toShowMoodRatingCameraFragment(
                    sideEffect.smilingProbabilities, sideEffect.capturedSelfieUri.toString()
                ).apply {
                    findNavController().navigate(this)
                }
            }
            is CameraEffect.ExceptionHappened -> {
                error(sideEffect.error)
            }
            is CameraEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
        }
    }
}