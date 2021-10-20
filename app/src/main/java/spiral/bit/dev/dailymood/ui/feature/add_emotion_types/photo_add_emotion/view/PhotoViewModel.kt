package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.view

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.data.emotion.EmotionRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.listenAwait
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.models.mvi.PhotoEffect
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.models.mvi.PhotoState
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val emotionRepository: EmotionRepository,
    private val detector: FaceDetector
) : BaseViewModel<PhotoState, PhotoEffect>() {

    override val container = container<PhotoState, PhotoEffect>(PhotoState(0.0F))

    private val faceDetectionMoodResolver = FaceMoodResolver()

    fun detectFace(image: InputImage, listener: () -> Unit) = intent {
        runCatching {
            return@runCatching detector.process(image).listenAwait().also<List<Face>> { listener.invoke() }
        }.onFailure {
            postSideEffect(PhotoEffect.ExceptionHappened(it))
        }.onSuccess { faces ->
            when {
                faces.size > 1 -> postSideEffect(PhotoEffect.Toast(R.string.faces_too_many))
                faces.isEmpty() -> postSideEffect(PhotoEffect.Toast(R.string.your_face_not_visible_label))
                else -> {
                    reduce { state.copy(smileProbability = faces.first().smilingProbability!!) }
                }
            }
        }
    }

    private fun insertEmotion(emotionItem: EmotionItem) = intent {
        emotionRepository.insert(emotionItem)
        postSideEffect(PhotoEffect.NavigateToMain)
    }

    fun takeEmotion() = intent {
        val moodValue = faceDetectionMoodResolver.resolveEmotionType(state.smileProbability)
        val emotionItem = EmotionItem(emotionType = moodValue)
        insertEmotion(emotionItem)
    }
}

