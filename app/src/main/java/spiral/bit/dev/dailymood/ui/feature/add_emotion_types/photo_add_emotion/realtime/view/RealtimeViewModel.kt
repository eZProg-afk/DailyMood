package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.view

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
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.listenAwait
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.models.mvi.RealtimeEffect
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.models.mvi.RealtimeState
import javax.inject.Inject

@HiltViewModel
class RealtimeViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val detector: FaceDetector
) : BaseViewModel<RealtimeState, RealtimeEffect>() {

    override val container = container<RealtimeState, RealtimeEffect>(RealtimeState(0.0F))

    private val faceDetectionMoodResolver = FaceMoodResolver()

    fun detectFace(image: InputImage, listener: () -> Unit) = intent {
        runCatching {
            return@runCatching detector.process(image).listenAwait().also<List<Face>> { listener.invoke() }
        }.onFailure {
            postSideEffect(RealtimeEffect.ExceptionHappened(it))
        }.onSuccess { faces ->
            when {
                faces.size > 1 -> postSideEffect(RealtimeEffect.Toast(R.string.faces_too_many))
                faces.isEmpty() -> postSideEffect(RealtimeEffect.Toast(R.string.your_face_not_visible_label))
                else -> {
                    reduce { state.copy(smileProbability = faces.first().smilingProbability!!) }
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
        val emotionItem = MoodEntity(emotionType = moodValue)
        insertEmotion(emotionItem)
    }

    fun onPhotoTypeClicked(itemId: Int) = intent {
        when (itemId) {
            0 -> postSideEffect(RealtimeEffect.AddEmotionByCamera)
            1 -> postSideEffect(RealtimeEffect.AddEmotionByGallery)
            2 -> postSideEffect(RealtimeEffect.AddEmotionByRealtime)
        }
    }
}

