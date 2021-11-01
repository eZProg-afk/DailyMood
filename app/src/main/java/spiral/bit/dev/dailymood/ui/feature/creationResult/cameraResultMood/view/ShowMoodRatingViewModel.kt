package spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.view

import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi.MoodCameraEffect
import spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi.MoodCameraState
import javax.inject.Inject

@HiltViewModel
class ShowMoodRatingViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val faceMoodResolver: FaceMoodResolver,
    private val emotionTypeMapper: EmotionTypeMapper
) : BaseViewModel<MoodCameraState, MoodCameraEffect>() {

    override val container =
        container<MoodCameraState, MoodCameraEffect>(MoodCameraState(smileRating = null))

    fun resolveMood(smilingProbabilities: Float) = intent {
        val moodValue = faceMoodResolver.resolveEmotionType(smilingProbabilities)
        val emotionType = emotionTypeMapper.mapToEmotionType(moodValue)
        reduce { state.copy(smileRating = emotionType.smileyRating) }
    }

    fun saveMoodEntity(smilingProbabilities: Float) = intent {
        val moodValue = faceMoodResolver.resolveEmotionType(smilingProbabilities)
        val moodEntity = MoodEntity(moodType = moodValue)
        moodRepository.insert(moodEntity)
        postSideEffect(MoodCameraEffect.NavigateToMain)
    }

    fun retakePhoto() = intent {
        postSideEffect(MoodCameraEffect.NavigateToCameraAddMood)
    }
}