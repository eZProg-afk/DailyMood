package spiral.bit.dev.dailymood.ui.feature.statistic.view

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.statistic.models.SortOrder
import spiral.bit.dev.dailymood.ui.feature.statistic.models.mvi.StatisticEffect
import spiral.bit.dev.dailymood.ui.feature.statistic.models.mvi.StatisticsState
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(private val moodRepository: MoodRepository) :
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
        moodRepository.getEmotionsBySearchQuery(query).collect { emotions ->
            reduce {
                state.copy(moodEntities = emotions)
            }
        }
    }

    private fun getAllHappyEmotions() = intent {
        moodRepository.getAllHappyEmotions().collect { emotions ->
            reduce {
                state.copy(moodEntities = emotions)
            }
        }
    }

    private fun getAllNeutralEmotions() = intent {
        moodRepository.getAllNeutralEmotions().collect { emotions ->
            reduce {
                state.copy(moodEntities = emotions)
            }
        }
    }

    private fun getAllSadEmotions() = intent {
        moodRepository.getAllSadEmotions().collect { emotions ->
            reduce {
                state.copy(moodEntities = emotions)
            }
        }
    }

    private fun getAllAngryEmotions() = intent {
        moodRepository.getAllAngryEmotions().collect { emotions ->
            reduce {
                state.copy(moodEntities = emotions)
            }
        }
    }

    fun toDetail(emotionId: Long) = intent {
        postSideEffect(StatisticEffect.NavigateToDetail(emotionId))
    }
}