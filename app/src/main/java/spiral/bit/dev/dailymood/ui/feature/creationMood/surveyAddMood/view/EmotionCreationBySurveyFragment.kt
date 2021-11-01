package spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentMoodCreationBySurveyBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.models.mvi.SurveyEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.models.mvi.SurveyState

class EmotionCreationBySurveyFragment :
    BaseFragment<SurveyState, SurveyEffect, FragmentMoodCreationBySurveyBinding>(
        FragmentMoodCreationBySurveyBinding::inflate
    ) {

    override val viewModel: SurveyViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private var answerNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        setUpViews()
    }

    private fun setUpViews() = binding {
        setUpToolbar()
    }

    private fun setUpToolbar() = binding {
        surveyToolbar.titleTextView.text = getString(R.string.survey_label)
    }

    private fun setUpClicks() = binding {
        surveyToolbar.iconBackImageView.setOnClickListener {
            viewModel.navigateBack()
        }

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

        interruptSurveyButton.setOnClickListener {
            //TODO INTERRUPT SURVEY BUTTON
        }

        previousQuestionButton.setOnClickListener {
            viewModel.goBackToPreviousAnswer()
        }

        nextQuestionButton.setOnClickListener {
            if (answersRadioGroup.checkedRadioButtonId != NOT_CLICKED) {
                viewModel.onAnswer(answerNumber)
            } else {
                viewModel.answerMiss()
            }
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
            is SurveyEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
            is SurveyEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
        }
    }

    override fun renderState(state: SurveyState) = binding {
        if (state.isRepeatSurvey) viewModel.relaunchSurvey()
        else {
            questionTextView.text = state.question
            answersRadioGroup.clearCheck()
            numberOfQuestionTextView.text = getString(R.string.question_number_format, state.questionCounter.toString())
            answer1RadioButton.text = state.answers[0]
            answer2RadioButton.text = state.answers[1]
            answer3RadioButton.text = state.answers[2]
            answer4RadioButton.text = state.answers[3]
            answer5RadioButton.text = state.answers[4]
            questionProgressBar.progress = state.questionCounter * PROGRESS_FORMULA
        }
    }

    companion object {
        private const val NOT_CLICKED = -1
        private const val MAX_PROGRESS = 100
        private const val COUNT_OF_QUESTIONS = 23
        private const val PROGRESS_FORMULA = MAX_PROGRESS / COUNT_OF_QUESTIONS + 1
    }
}