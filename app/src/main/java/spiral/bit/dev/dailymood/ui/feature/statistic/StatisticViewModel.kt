package spiral.bit.dev.dailymood.ui.feature.statistic


import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.data.EmotionRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import javax.inject.Inject

class StatisticViewModel
    @Inject constructor(private val emotionRepository: EmotionRepository): BaseViewModel<StatisticsState, StatisticSideEffect>() {

    override val container = container<StatisticsState, StatisticSideEffect>(
        StatisticsState(
            emptyList(), SortOrder.BY_HAPPY
        )
    )

    fun getAllHappyEmotions() = intent {
        emotionRepository.getAllHappyEmotions().collect { emotions ->
            reduce {
                state.copy(emotions = emotions)
            }
        }
    }
}