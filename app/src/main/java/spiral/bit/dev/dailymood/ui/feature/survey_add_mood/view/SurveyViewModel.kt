package spiral.bit.dev.dailymood.ui.feature.survey_add_mood.view

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.models.mvi.SurveyEffect
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.models.mvi.SurveyState
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers.AnswerProvider
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers.QuestionProvider
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers.ScoreProvider
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @SuppressLint("StaticFieldLeak") //this is application context, not leak
@Inject constructor(
    private val moodRepository: MoodRepository,
    private val questionProvider: QuestionProvider,
    private val answerProvider: AnswerProvider,
    private val scoreProvider: ScoreProvider,
    @ApplicationContext private val context: Context
) : BaseViewModel<SurveyState, SurveyEffect>() {

    override val container = container<SurveyState, SurveyEffect>(
        SurveyState(
            questionCounter = 1,
            question = questionProvider.tryGetQuestion(context, 1).question,
            answers = answerProvider.getAnswers(context),
            scores = emptyList(),
            isRepeatSurvey = false
        )
    )

    fun onAnswer(answerNumber: Int) = intent {
        val scoreForAnswer = scoreProvider.getScore(answerNumber)
        val scores = state.scores.toMutableList()
        scores.add(scoreForAnswer)
        val questionCounter = state.questionCounter.run { inc() }
        val questionResult = questionProvider.tryGetQuestion(context, questionCounter)
        if (questionResult.question.isNotEmpty()) {
            reduce {
                state.copy(
                    scores = scores,
                    questionCounter = questionCounter,
                    question = questionResult.question
                )
            }
        } else {
            showMoodResult()
        }
    }

    private fun showMoodResult() = intent {
        postSideEffect(SurveyEffect.NavigateToSurveyResult(state.scores.toIntArray()))
        reduce { state.copy(isRepeatSurvey = true) }
    }

    fun answerMiss() = intent {
        postSideEffect(SurveyEffect.Toast(R.string.you_not_answer))
    }

    fun relaunchSurvey() = intent {
        reduce {
            state.copy(
                isRepeatSurvey = false,
                questionCounter = 1,
                question = questionProvider.tryGetQuestion(context, 1).question,
                answers = answerProvider.getAnswers(context),
                scores = emptyList()
            )
        }
    }
}