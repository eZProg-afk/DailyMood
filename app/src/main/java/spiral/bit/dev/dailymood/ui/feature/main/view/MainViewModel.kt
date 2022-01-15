package spiral.bit.dev.dailymood.ui.feature.main.view

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.helperFunctions.getEndOfDay
import spiral.bit.dev.dailymood.ui.base.helperFunctions.getStartOfDay
import spiral.bit.dev.dailymood.ui.common.adapter.mappers.AdapterTypeMapper
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionEffect
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionState
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val adapterTypeMapper: AdapterTypeMapper
) : BaseViewModel<EmotionState, EmotionEffect>() {

    override val container = container<EmotionState, EmotionEffect>(EmotionState(emptyList())) {
        onCreate()
    }

    private fun onCreate() = intent {
        Calendar.getInstance().apply {
            val startDay = getStartOfDay(LocalDate.now(ZoneId.systemDefault()))
            val endDay = getEndOfDay(LocalDate.now(ZoneId.systemDefault()))
            getAllEmotionsByDate(startDay, endDay)
        }
    }

    fun getAllEmotionsByDate(dayStart: Long, dayEnd: Long) = intent {
        moodRepository.getEmotionsByDate(dayStart, dayEnd)
            .onEach { moodEntities ->
                val moodItems = adapterTypeMapper.resolveMoodItem(moodEntities)
                reduce { state.copy(moodEntities = moodItems) }
            }.launchIn(viewModelScope)
    }

    fun searchByQuery(searchQuery: String) = intent {
        moodRepository.getEmotionsBySearchQuery(searchQuery).collect { emotions ->
            val moodItems = adapterTypeMapper.resolveMoodItem(emotions)
            reduce { state.copy(moodEntities = moodItems) }
        }
    }

    fun toSelectEmotion() = intent {
        postSideEffect(EmotionEffect.NavigateToSelect)
    }

    fun toDetail(emotionId: Long) = intent {
        postSideEffect(EmotionEffect.NavigateToDetail(emotionId))
    }
}