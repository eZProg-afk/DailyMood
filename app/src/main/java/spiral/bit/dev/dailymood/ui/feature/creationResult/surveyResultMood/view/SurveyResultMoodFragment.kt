package spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.models.SurveyData
import spiral.bit.dev.dailymood.databinding.FragmentSurveyResultMoodBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi.MoodSurveyEffect
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi.MoodSurveyState
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.surveyResultDelegate
import java.lang.Double.sum

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
        val totalScoresList = args.scores.toList()
        val summaryScores = totalScoresList.sum()
        val depressionSectionItems = totalScoresList.subList(0, 7).sum()
        val neurosisSectionItems = totalScoresList.subList(7, 14).sum()
        val socialPhobiaSectionItems = totalScoresList.subList(14, 16).sum()
        val astheniaSectionItems = totalScoresList.subList(16, 18).sum()
        val insomniaSectionItems = totalScoresList.subList(18, 20).sum()
        viewModel.createDataForRecyclerView(
            SurveyData(
                summaryScores,
                depressionSectionItems,
                neurosisSectionItems,
                socialPhobiaSectionItems,
                astheniaSectionItems,
                insomniaSectionItems,
            )
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