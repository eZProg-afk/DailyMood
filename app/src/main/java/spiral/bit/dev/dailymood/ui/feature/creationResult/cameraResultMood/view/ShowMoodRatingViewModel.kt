package spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.view

import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.models.CreationType
import spiral.bit.dev.dailymood.data.models.PhotoData
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi.MoodCameraEffect
import spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi.MoodCameraState
import javax.inject.Inject

@HiltViewModel
class ShowMoodRatingViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val faceMoodResolver: FaceMoodResolver,
    private val moodTypeMapper: MoodTypeMapper
) : BaseViewModel<MoodCameraState, MoodCameraEffect>() {

    override val container =
        container<MoodCameraState, MoodCameraEffect>(MoodCameraState(smileRating = null))

    fun resolveMood(smilingProbabilities: Float) = intent {
        val moodValue = faceMoodResolver.resolveEmotionType(smilingProbabilities)
        val emotionType = moodValue?.let { moodTypeMapper.mapToMoodType(it) }
        reduce { state.copy(smileRating = emotionType?.smileyRating) }
    }

    fun saveMoodEntity(smilingProbabilities: Float, photoUri: String) = intent {
        val moodValue = faceMoodResolver.resolveEmotionType(smilingProbabilities)
        val moodEntity = MoodEntity(
            moodValue = moodValue!!,
            photoData = PhotoData(photoUri),
            creationType = CreationType.BY_PHOTO
        )
        moodRepository.insert(moodEntity)
        postSideEffect(MoodCameraEffect.NavigateToMain)
    }

    fun retakePhoto() = intent {
        postSideEffect(MoodCameraEffect.NavigateToCameraAddMood)
    }
}