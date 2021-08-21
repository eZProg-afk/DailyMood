package spiral.bit.dev.dailymood.ui.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.Emotion
import spiral.bit.dev.dailymood.data.EmotionRepository
import spiral.bit.dev.dailymood.helpers.getEndOfDay
import spiral.bit.dev.dailymood.helpers.getStartOfDay
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.main.models.common.EmotionSideEffect
import spiral.bit.dev.dailymood.ui.main.models.common.EmotionState
import spiral.bit.dev.dailymood.ui.main.models.other.EmotionType
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val emotionRepository: EmotionRepository
) : BaseViewModel<EmotionState, EmotionSideEffect>() {

    override val container =
        container<EmotionState, EmotionSideEffect>(EmotionState(emptyList())) {
            onCreate()
        }

    private fun onCreate() = intent {
        Calendar.getInstance().apply {
            val startDay = getStartOfDay(LocalDate.now(ZoneId.systemDefault()))
            val endDay = getEndOfDay(LocalDate.now(ZoneId.systemDefault()))
            getAllEmotionsByDate(startDay, endDay)
        }
    }

    fun insert(currentItem: Int, note: String, emotionUri: String) = intent {
        val emotionType = when (currentItem) {
            0 -> EmotionType.HAPPY
            1 -> EmotionType.NEUTRAL
            2 -> EmotionType.SAD
            3 -> EmotionType.ANGRY
            else -> EmotionType.HAPPY
        }
        Emotion(
            note = note,
            emotionType = emotionType,
            emotionPhoto = emotionUri
        ).also { emotion ->
            emotionRepository.insert(emotion)
            postSideEffect(EmotionSideEffect.Toast(R.string.record_added_toast))
            postSideEffect(EmotionSideEffect.NavigateToMain)
        }
    }

    fun selectEmotion(emotion: Emotion) = intent {
        postSideEffect(EmotionSideEffect.NavigateToDetail(emotion))
    }

    fun getAllEmotionsByDate(dayStart: Long, dayEnd: Long) = intent {
        emotionRepository.getEmotionsByDate(dayStart, dayEnd)
            .onEmpty {
                reduce { state.copy(emotions = emptyList()) }
            }.onEach {
                reduce { state.copy(emotions = it) }
            }.launchIn(viewModelScope)
    }

    fun toCreateEmotion() = intent {
        postSideEffect(EmotionSideEffect.NavigateToCreate)
    }

    fun emotionSwiped(emotion: Emotion) = intent {
        emotionRepository.delete(emotion)
        postSideEffect(EmotionSideEffect.ShowSnackbar(msg = R.string.emotion_deleted, emotion))
    }

    fun onUndoDelete(emotion: Emotion?) = intent {
        emotion?.let {
            emotionRepository.insert(it)
        }
    }
}