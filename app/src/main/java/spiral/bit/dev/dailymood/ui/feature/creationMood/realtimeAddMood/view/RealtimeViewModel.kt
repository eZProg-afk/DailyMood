package spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.view

import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.models.CreationType
import spiral.bit.dev.dailymood.data.models.PhotoData
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.di.FaceDetectorOptionsRealtime
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.base.callbackConverters.listenAwait
import spiral.bit.dev.dailymood.ui.base.extensions.safeLet
import spiral.bit.dev.dailymood.ui.base.extensions.takePictureAwait
import spiral.bit.dev.dailymood.ui.common.formatters.AppDateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.models.mvi.RealtimeEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.realtimeAddMood.models.mvi.RealtimeState
import javax.inject.Inject

@HiltViewModel
class RealtimeViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    @FaceDetectorOptionsRealtime private val faceDetector: FaceDetector,
    private val faceDetectionMoodResolver: FaceMoodResolver,
    private val appDateTimeFormatter: AppDateTimeFormatter
) : BaseViewModel<RealtimeState, RealtimeEffect>() {

    override val container = container<RealtimeState, RealtimeEffect>(
        RealtimeState(
            smileProbability = null,
            currentInputImage = null
        )
    )

    fun detectFace(
        activity: FragmentActivity,
        imageCapture: ImageCapture,
        image: InputImage,
        listener: () -> Unit
    ) = intent {
        val photoFile = appDateTimeFormatter.formatFile(activity.filesDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        val savedUri = photoFile.toUri()
        runCatching {
            imageCapture.takePictureAwait(outputOptions, ContextCompat.getMainExecutor(activity))
            return@runCatching faceDetector.process(image).listenAwait()
                .also<List<Face>> { listener.invoke() }
        }.onFailure {
            postSideEffect(RealtimeEffect.ExceptionHappened(it))
        }.onSuccess { faces ->
            when {
                faces.size > 1 -> postSideEffect(RealtimeEffect.Toast(R.string.faces_too_many))
                faces.isEmpty() -> postSideEffect(RealtimeEffect.Toast(R.string.your_face_not_visible_label))
                else -> {
                    faces.first().smilingProbability?.let { smilingProbability ->
                        reduce {
                            state.copy(
                                smileProbability = smilingProbability,
                                currentInputImage = savedUri
                            )
                        }
                    }
                }
            }
        }
    }

    private fun insertEmotion(moodEntity: MoodEntity) = intent {
        moodRepository.insert(moodEntity)
        postSideEffect(RealtimeEffect.NavigateToMain)
    }

    fun takeEmotion() = intent {
        val moodValue = faceDetectionMoodResolver.resolveEmotionType(state.smileProbability)
        Logger.logDebug(state.currentInputImage.toString())
        safeLet(moodValue, state.currentInputImage) { nonNullMoodValue, inputImage ->
            val emotionItem = MoodEntity(
                moodValue = nonNullMoodValue,
                photoData = PhotoData(photoPath = inputImage.toString()),
                creationType = CreationType.BY_PHOTO
            )
            insertEmotion(emotionItem)
        }
    }
}

