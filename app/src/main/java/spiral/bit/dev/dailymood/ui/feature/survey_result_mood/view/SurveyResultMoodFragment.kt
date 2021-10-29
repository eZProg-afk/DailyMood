package spiral.bit.dev.dailymood.ui.feature.survey_result_mood.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentSurveyResultMoodBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.SurveyResultItem
import spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.mvi.MoodSurveyEffect
import spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.mvi.MoodSurveyState

class SurveyResultMoodFragment :
    BaseFragment<MoodSurveyState, MoodSurveyEffect, FragmentSurveyResultMoodBinding>(
        FragmentSurveyResultMoodBinding::inflate
    ) {

    private val args: SurveyResultMoodFragmentArgs by navArgs()
    private val itemsAdapter = ItemAdapter<SurveyResultItem>()
    private val surveyResultAdapter = FastAdapter.with(itemsAdapter)
    override val viewModel: SurveyResultViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpNavArgs()
        setUpClicks()
    }

    private fun setUpViews() = binding {
        setUpRecyclerView()
        lifecycleScope.launchWhenResumed { viewModel.getCurrentMood(args.scores) }
    }

    private fun setUpClicks() = binding {
        saveMoodButton.setOnClickListener {
            viewModel.saveMood(args.scores)
        }

        repeatSurveyButton.setOnClickListener {
            viewModel.repeatTest()
        }
    }

    private fun setUpNavArgs() {
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
    }

    private fun setUpRecyclerView() = binding {
        surveyResultsRecyclerView.apply {
            adapter = surveyResultAdapter
        }
    }

    override fun renderState(state: MoodSurveyState) = binding {
        state.moodValue?.let {
            smileyRatingView.setCurrentRateStatus(state.moodValue.smileyRating)
            Logger.logDebug(state.moodValue.smileyRating.name)
        }
        itemsAdapter.set(state.surveyResultList)
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