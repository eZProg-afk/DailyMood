package spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers

import android.content.Context
import spiral.bit.dev.dailymood.R

class QuestionProvider {

    fun tryGetQuestion(context: Context, questionCounter: Int): QuestionResult {
        val questions = context.resources.getStringArray(R.array.survey_questions)
        return if (questionCounter >= questions.size) QuestionResult(isQuestionsEnded = true)
        else QuestionResult(question = questions[questionCounter])
    }

    data class QuestionResult(val question: String = "", val isQuestionsEnded: Boolean = false)
}