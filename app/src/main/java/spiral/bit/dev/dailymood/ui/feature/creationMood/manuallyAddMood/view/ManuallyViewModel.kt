package spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.view

import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.models.CreationType
import spiral.bit.dev.dailymood.data.models.ManuallyData
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.SliderItem
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi.ManuallyEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi.ManuallyState
import javax.inject.Inject

@HiltViewModel
class ManuallyViewModel @Inject constructor(
    private val moodRepository: MoodRepository
) : BaseViewModel<ManuallyState, ManuallyEffect>() {

    override val container = container<ManuallyState, ManuallyEffect>(
        ManuallyState(
            imageUri = null,
            sliderItems = sliderItems
        )
    )

    fun insert(moodValue: Float, note: String, reason: String) = intent {
        MoodEntity(
            moodValue = moodValue,
            creationType = CreationType.MANUALLY,
            manuallyData = ManuallyData(
                note = note,
                reason = reason
            )
        ).also { emotion ->
            moodRepository.insert(emotion)
            postSideEffect(ManuallyEffect.Toast(R.string.record_added_toast))
            postSideEffect(ManuallyEffect.NavigateToMain)
        }
    }

    fun navigateBack() = intent {
        postSideEffect(ManuallyEffect.NavigateBack)
    }

    companion object {
        private val sliderItems = listOf(
            SliderItem(R.drawable.ic_emotion_happy),
            SliderItem(R.drawable.ic_emotion_good),
            SliderItem(R.drawable.ic_emotion_neutral),
            SliderItem(R.drawable.ic_emotion_sad),
            SliderItem(R.drawable.ic_emotion_angry)
        )
    }
}