package spiral.bit.dev.dailymood.ui.feature.survey_add_mood.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmotionCreationBySurveyBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.models.mvi.SurveyEffect
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.models.mvi.SurveyState

class EmotionCreationBySurveyFragment :
    BaseFragment<SurveyState, SurveyEffect, FragmentEmotionCreationBySurveyBinding>(
        FragmentEmotionCreationBySurveyBinding::inflate
    ) {

    override val viewModel: SurveyViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private var answerNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
    }

    private fun setUpClicks() = binding {
        answersRadioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            answerNumber = when (radioGroup.checkedRadioButtonId) {
                R.id.answer1RadioButton -> 1
                R.id.answer2RadioButton -> 2
                R.id.answer3RadioButton -> 3
                R.id.answer4RadioButton -> 4
                R.id.answer5RadioButton -> 5
                else -> 0
            }
        }

        nextQuestionButton.setOnClickListener {
            if (answersRadioGroup.checkedRadioButtonId != NOT_CLICKED) {
                viewModel.onAnswer(answerNumber)
            } else {
                viewModel.answerMiss()
            }
        }
    }

    override fun renderState(state: SurveyState) = binding {
        if (state.isRepeatSurvey) viewModel.relaunchSurvey()
        else {
            questionTextView.text = state.question
            answersRadioGroup.clearCheck()
            numberOfQuestionTextView.text =
                getString(R.string.question_number_format, state.questionCounter.toString())
            answer1RadioButton.text = state.answers[0]
            answer2RadioButton.text = state.answers[1]
            answer3RadioButton.text = state.answers[2]
            answer4RadioButton.text = state.answers[3]
            answer5RadioButton.text = state.answers[4]
        }
    }

    override fun handleSideEffect(sideEffect: SurveyEffect) = binding {
        when (sideEffect) {
            is SurveyEffect.NavigateToSurveyResult -> {
                EmotionCreationBySurveyFragmentDirections.toSurveyResultMoodFragment(
                    sideEffect.scores
                ).apply {
                    findNavController().navigate(this)
                }
            }
            is SurveyEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
        }
    }

    companion object {
        private const val NOT_CLICKED = -1
    }
}