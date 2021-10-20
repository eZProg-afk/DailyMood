package spiral.bit.dev.dailymood.ui.feature.statistic.view

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.emotion.EmotionRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.statistic.models.SortOrder
import spiral.bit.dev.dailymood.ui.feature.statistic.models.mvi.StatisticEffect
import spiral.bit.dev.dailymood.ui.feature.statistic.models.mvi.StatisticsState
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(private val emotionRepository: EmotionRepository) :
    BaseViewModel<StatisticsState, StatisticEffect>() {

    override val container = container<StatisticsState, StatisticEffect>(
        StatisticsState(emptyList(), SortOrder.BY_HAPPY)
    )

    fun selectBy(sortOrder: SortOrder) {
        when (sortOrder) {
            SortOrder.BY_HAPPY -> getAllHappyEmotions()
            SortOrder.BY_NEUTRAL -> getAllNeutralEmotions()
            SortOrder.BY_SAD -> getAllSadEmotions()
            SortOrder.BY_ANGRY -> getAllAngryEmotions()
        }
    }

    fun searchByQuery(query: String) = intent {
        emotionRepository.getEmotionsBySearchQuery(query).collect { emotions ->
            reduce {
                state.copy(emotionItems = emotions)
            }
        }
    }

    private fun getAllHappyEmotions() = intent {
        emotionRepository.getAllHappyEmotions().collect { emotions ->
            reduce {
                state.copy(emotionItems = emotions)
            }
        }
    }

    private fun getAllNeutralEmotions() = intent {
        emotionRepository.getAllNeutralEmotions().collect { emotions ->
            reduce {
                state.copy(emotionItems = emotions)
            }
        }
    }

    private fun getAllSadEmotions() = intent {
        emotionRepository.getAllSadEmotions().collect { emotions ->
            reduce {
                state.copy(emotionItems = emotions)
            }
        }
    }

    private fun getAllAngryEmotions() = intent {
        emotionRepository.getAllAngryEmotions().collect { emotions ->
            reduce {
                state.copy(emotionItems = emotions)
            }
        }
    }

    fun toDetail(emotionId: Long) = intent {
        postSideEffect(StatisticEffect.NavigateToDetail(emotionId))
    }
}