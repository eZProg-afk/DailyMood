package spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.view

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.models.mvi.SurveyEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.models.mvi.SurveyState
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers.AnswerProvider
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers.QuestionProvider
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers.ScoreProvider
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @SuppressLint("StaticFieldLeak") //this is application context, not leak
@Inject constructor(
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
        scores.add(scoreForAnswer) //TODO COMBINE THIS LINE WITH UPPER LINE
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

    fun goBackToPreviousAnswer() = intent {
        val questionCounter = state.questionCounter.run { dec() }
        if (questionCounter == 0) {
            postSideEffect(SurveyEffect.Toast(R.string.cant_go_to_previous_question_toast))
        } else {
            reduce {
                state.copy(
                    questionCounter = questionCounter,
                    question = questionProvider.tryGetQuestion(context, questionCounter).question
                )
            }
        }
    }

    private fun showMoodResult() = intent {
        postSideEffect(SurveyEffect.NavigateToSurveyResult(state.scores.toIntArray()))
        reduce { state.copy(isRepeatSurvey = true) }
    }

    fun answerMiss() = intent {
        postSideEffect(SurveyEffect.Toast(R.string.you_not_answer))
    }

    fun navigateBack() = intent {
        postSideEffect(SurveyEffect.NavigateBack)
    }
}