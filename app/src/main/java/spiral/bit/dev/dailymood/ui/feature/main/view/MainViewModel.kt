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
import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.data.emotion.EmotionRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.getEndOfDay
import spiral.bit.dev.dailymood.ui.base.getStartOfDay
import spiral.bit.dev.dailymood.ui.feature.main.models.EmotionUiItem
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionEffect
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionState
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val emotionRepository: EmotionRepository
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
        emotionRepository.getEmotionsByDate(dayStart, dayEnd)
            .onEmpty {
                reduce { state.copy(emotionItems = emptyList()) }
            }.onEach { emotions ->
                reduce { state.copy(emotionItems = emotions) }
            }.launchIn(viewModelScope)
    }

    fun searchByQuery(searchQuery: String) = intent {
        emotionRepository.getEmotionsBySearchQuery(searchQuery).collect { emotions ->
            reduce {
                state.copy(emotionItems = emotions)
            }
        }
    }

    fun emotionSwiped(emotion: EmotionUiItem) = intent {
        emotionRepository.delete(emotion.emotionItemMainModel)
        postSideEffect(EmotionEffect.ShowSnackbar(msg = R.string.emotion_deleted, emotion.emotionItemMainModel))
    }

    fun onUndoDelete(emotionItem: EmotionItem?) = intent {
        emotionItem?.let {
            emotionRepository.insert(it)
        }
    }

    fun toSelectEmotion() = intent {
        postSideEffect(EmotionEffect.NavigateToSelect)
    }
}