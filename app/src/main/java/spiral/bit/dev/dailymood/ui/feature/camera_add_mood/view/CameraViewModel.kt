package spiral.bit.dev.dailymood.ui.feature.camera_add_mood.view

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.di.FaceDetectorOptionsStatic
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.base.extensions.takePictureAwait
import spiral.bit.dev.dailymood.ui.base.listenAwait
import spiral.bit.dev.dailymood.ui.common.formatters.DateTimeFormatter
import spiral.bit.dev.dailymood.ui.feature.camera_add_mood.models.mvi.CameraEffect
import spiral.bit.dev.dailymood.ui.feature.camera_add_mood.models.mvi.CameraState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @FaceDetectorOptionsStatic private val faceDetector: FaceDetector,
    private val dateTimeFormatter: DateTimeFormatter
) : BaseViewModel<CameraState, CameraEffect>() {

    override val container = container<CameraState, CameraEffect>(CameraState(null, null))

    fun toggleFlash(context: Context, camera: Camera) = intent {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera.cameraControl.enableTorch(false)
        } else {
            camera.cameraControl.enableTorch(true)
        }
    }

    fun takePhoto(imageCapture: ImageCapture, activity: Activity) = intent {
        val photoFile = dateTimeFormatter.formatFile(activity.filesDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePictureAwait(outputOptions, ContextCompat.getMainExecutor(activity))
        val savedUri = photoFile.toUri()

        runCatching {
            reduce { state.copy(capturedSelfieUri = savedUri) }
            InputImage.fromFilePath(activity, savedUri)
        }.onSuccess { image ->
            detectFace(image)
        }.onFailure { throwable ->
            Logger.logError(throwable)
        }
    }

    private fun detectFace(image: InputImage) = intent {
        runCatching {
            return@runCatching faceDetector.process(image).listenAwait()
        }.onFailure {
            postSideEffect(CameraEffect.ExceptionHappened(it))
        }.onSuccess { faces ->
            when {
                faces.size > 1 -> postSideEffect(CameraEffect.Toast(R.string.faces_too_many))
                faces.isEmpty() -> postSideEffect(CameraEffect.Toast(R.string.your_face_not_visible_label))
                else -> {
                    reduce { state.copy(smileProbability = faces.first().smilingProbability!!) }
                }
            }
        }
    }

    fun showResultPhoto() = intent {
        postSideEffect(
            CameraEffect.NavigateToShowMoodRating(
                state.smileProbability!!,
                state.capturedSelfieUri!! //TODO !!
            )
        ).also {
            reduce {
                state.copy(
                    smileProbability = null,
                    capturedSelfieUri = null
                )
            }
        }
    }
}