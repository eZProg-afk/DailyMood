package spiral.bit.dev.dailymood.ui.feature.create.view

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.data.emotion.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.create.models.mvi.CreateEmotionSideEffect
import spiral.bit.dev.dailymood.ui.feature.create.models.mvi.CreateEmotionState
import spiral.bit.dev.dailymood.ui.feature.create.models.SliderItem
import javax.inject.Inject

@HiltViewModel
class CreateEmotionViewModel @Inject constructor(
    private val moodRepository: MoodRepository
    ) : BaseViewModel<CreateEmotionState, CreateEmotionSideEffect>() {

    override val container = container<CreateEmotionState, CreateEmotionSideEffect>(
        CreateEmotionState(
            null,
            sliderItems
        )
    )

    fun insert(moodValue: Float, note: String) = intent {
        MoodEntity(
            note = note,
            emotionType = moodValue,
            photoPath = state.imageUri.toString()
        ).also { emotion ->
            moodRepository.insert(emotion)
            postSideEffect(CreateEmotionSideEffect.Toast(R.string.record_added_toast))
            postSideEffect(CreateEmotionSideEffect.NavigateToMain)
        }
    }

    fun onImageSelect(uri: Uri?) = intent {
        reduce { state.copy(imageUri = uri) }
    }

    companion object {
        private val sliderItems = listOf(
            SliderItem(R.drawable.ic_emotion_happy),
            SliderItem(R.drawable.ic_emotion_neutral),
            SliderItem(R.drawable.ic_emotion_sad),
            SliderItem(R.drawable.ic_emotion_angry)
        )
    }
}