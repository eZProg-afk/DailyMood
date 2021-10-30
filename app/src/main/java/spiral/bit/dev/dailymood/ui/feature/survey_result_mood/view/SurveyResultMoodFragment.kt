package spiral.bit.dev.dailymood.ui.feature.survey_result_mood.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentSurveyResultMoodBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.mvi.MoodSurveyEffect
import spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.mvi.MoodSurveyState
import spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.surveyResultDelegate

class SurveyResultMoodFragment :
    BaseFragment<MoodSurveyState, MoodSurveyEffect, FragmentSurveyResultMoodBinding>(
        FragmentSurveyResultMoodBinding::inflate
    ) {

    override val viewModel: SurveyResultViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: SurveyResultMoodFragmentArgs by navArgs()
    private val surveyAdapter = ListDelegationAdapter(surveyResultDelegate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpClicks()
    }

    private fun setUpViews() = binding {
        setUpRecyclerView()
        viewModel.getCurrentMood(args.scores)
    }

    private fun setUpClicks() = binding {
        saveMoodButton.setOnClickListener {
            viewModel.saveMood(args.scores)
        }

        repeatSurveyButton.setOnClickListener {
            viewModel.repeatTest()
        }
    }

    private fun setUpRecyclerView() = binding {
        val totalScores = args.scores.toList()
        val depressionSectionItems = totalScores.subList(0, 7).sum()
        val neurosisSectionItems = totalScores.subList(7, 14).sum()
        val socialPhobiaSectionItems = totalScores.subList(14, 16).sum()
        val astheniaSectionItems = totalScores.subList(16, 18).sum()
        val insomniaSectionItems = totalScores.subList(18, 20).sum()
        viewModel.createDataForRecyclerView(
            depressionSectionItems,
            neurosisSectionItems,
            socialPhobiaSectionItems,
            astheniaSectionItems,
            insomniaSectionItems
        )
        surveyResultsRecyclerView.apply {
            adapter = surveyAdapter
        }
    }

    override fun renderState(state: MoodSurveyState) = binding {
        state.moodValue?.let { smileyRatingView.setCurrentRateStatus(state.moodValue.smileyRating) }
        surveyAdapter.items = state.surveyResultList
    }

    override fun handleSideEffect(sideEffect: MoodSurveyEffect) = binding {
        when (sideEffect) {
            MoodSurveyEffect.NavigateToMain -> {
                SurveyResultMoodFragmentDirections.toMain()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            MoodSurveyEffect.NavigateToSurvey -> {
                SurveyResultMoodFragmentDirections.toSurveyAddEmotionFragment().apply {
                    findNavController().navigate(this)
                }
            }
        }
    }
}