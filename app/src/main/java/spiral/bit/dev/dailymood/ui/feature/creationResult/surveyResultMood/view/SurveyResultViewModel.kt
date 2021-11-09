package spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.view

import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.models.CreationType
import spiral.bit.dev.dailymood.data.models.SurveyData
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.SurveyResolver
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.SurveyResultItem
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi.MoodSurveyEffect
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi.MoodSurveyState
import javax.inject.Inject

@HiltViewModel
class SurveyResultViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val surveyResolver: SurveyResolver,
    private val moodTypeMapper: MoodTypeMapper
) : BaseViewModel<MoodSurveyState, MoodSurveyEffect>() {

    override val container =
        container<MoodSurveyState, MoodSurveyEffect>(
            MoodSurveyState(
                surveyResultList = emptyList(),
                surveyData = null,
                moodValue = null
            )
        )

    fun createDataForRecyclerView(surveyData: SurveyData) = intent {
        val surveyResultsList = listOf(
            SurveyResultItem(
                surveyReason = R.string.depression_reason,
                scoresInThisSection = surveyData.depressionScores,
                advicesText = R.string.depression_advices
            ),
            SurveyResultItem(
                surveyReason = R.string.neurosis_reason,
                scoresInThisSection = surveyData.neurosisScores,
                advicesText = R.string.neurosis_advices
            ),
            SurveyResultItem(
                surveyReason = R.string.social_phobia_reason,
                scoresInThisSection = surveyData.socialPhobiaScores,
                advicesText = R.string.social_phobia_advices
            ),
            SurveyResultItem(
                surveyReason = R.string.asthenia_reason,
                scoresInThisSection = surveyData.astheniaScores,
                advicesText = R.string.asthenia_advices
            ),
            SurveyResultItem(
                surveyReason = R.string.insomnia_reason,
                scoresInThisSection = surveyData.insomniaScores,
                advicesText = R.string.insomnia_advices
            )
        )
        reduce { state.copy(surveyResultList = surveyResultsList, surveyData = surveyData) }
    }

    fun repeatTest() = intent {
        postSideEffect(MoodSurveyEffect.NavigateToSurvey)
    }

    fun getCurrentMood(scores: IntArray) = intent {
        val moodValue = surveyResolver.resolveSurvey(scores)
        val emotionType = moodTypeMapper.mapToMoodType(moodValue)
        reduce { state.copy(moodValue = emotionType) }
    }

    fun saveMood(scores: IntArray) = intent {
        val moodValue = surveyResolver.resolveSurvey(scores)
        val moodEntity = MoodEntity(
            moodValue = moodValue, surveyData = state.surveyData,
            creationType = CreationType.BY_SURVEY
        )
        moodRepository.insert(moodEntity)
        postSideEffect(MoodSurveyEffect.NavigateToMain)
    }
}