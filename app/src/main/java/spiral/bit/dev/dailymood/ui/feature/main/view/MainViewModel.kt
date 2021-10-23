package spiral.bit.dev.dailymood.ui.feature.main.view

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.data.emotion.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.getEndOfDay
import spiral.bit.dev.dailymood.ui.base.getStartOfDay
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodItem
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionEffect
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionState
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moodRepository: MoodRepository
) : BaseViewModel<EmotionState, EmotionEffect>() {

    override val container =
        container<EmotionState, EmotionEffect>(EmotionState(emptyList())) {
            onCreate()
        }

    private fun onCreate() = intent {
        Calendar.getInstance().apply {
            val startDay = getStartOfDay(LocalDate.now(ZoneId.systemDefault()))
            val endDay = getEndOfDay(LocalDate.now(ZoneId.systemDefault()))
            getAllEmotionsByDate(startDay, endDay)
        }
    }

    fun toDetail(emotionId: Long) = intent {
        postSideEffect(EmotionEffect.NavigateToDetail(emotionId))
    }

    fun getAllEmotionsByDate(dayStart: Long, dayEnd: Long) = intent {
        moodRepository.getEmotionsByDate(dayStart, dayEnd)
            .onEmpty {
                reduce { state.copy(moodEntities = emptyList()) }
            }.onEach { emotions ->
                reduce { state.copy(moodEntities = emotions) }
            }.launchIn(viewModelScope)
    }

    fun searchByQuery(searchQuery: String) = intent {
        moodRepository.getEmotionsBySearchQuery(searchQuery).collect { emotions ->
            reduce {
                state.copy(moodEntities = emotions)
            }
        }
    }

    fun emotionSwiped(emotion: MoodItem) = intent {
        moodRepository.delete(emotion.moodEntity)
        postSideEffect(EmotionEffect.ShowSnackbar(msg = R.string.emotion_deleted, emotion.moodEntity))
    }

    fun onUndoDelete(moodEntity: MoodEntity?) = intent {
        moodEntity?.let {
            moodRepository.insert(it)
        }
    }

    fun toSelectEmotion() = intent {
        postSideEffect(EmotionEffect.NavigateToSelect)
    }
}