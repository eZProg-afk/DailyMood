package spiral.bit.dev.dailymood.ui.feature.realtime_add_mood.view

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.data.emotion.MoodRepository
import spiral.bit.dev.dailymood.di.FaceDetectorOptionsRealtime
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.listenAwait
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.realtime_add_mood.models.mvi.RealtimeEffect
import spiral.bit.dev.dailymood.ui.feature.realtime_add_mood.models.mvi.RealtimeState
import javax.inject.Inject

@HiltViewModel
class RealtimeViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    @FaceDetectorOptionsRealtime private val faceDetector: FaceDetector
) : BaseViewModel<RealtimeState, RealtimeEffect>() {

    override val container = container<RealtimeState, RealtimeEffect>(RealtimeState(0F))

    private val faceDetectionMoodResolver = FaceMoodResolver()

    fun detectFace(image: InputImage, listener: () -> Unit) = intent {
        runCatching {
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
                        reduce { state.copy(smileProbability = smilingProbability) }
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
        val emotionItem = MoodEntity(moodType = moodValue)
        insertEmotion(emotionItem)
    }
}

