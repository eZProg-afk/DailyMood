package spiral.bit.dev.dailymood.ui.feature.analytics.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentAnalyticsBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.feature.analytics.adapters.AnalyticsPagerAdapter
import spiral.bit.dev.dailymood.ui.feature.analytics.models.mvi.AnalyticsEffect
import spiral.bit.dev.dailymood.ui.feature.analytics.models.mvi.AnalyticsState

@AndroidEntryPoint
class AnalyticsFragment : BaseFragment<AnalyticsState, AnalyticsEffect, FragmentAnalyticsBinding>(
    FragmentAnalyticsBinding::inflate
) {

    override val viewModel: AnalyticsViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val analyticsViewPagerAdapter by lazy { AnalyticsPagerAdapter(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        setUpToolbar()
        setUpClicks()
    }

    private fun setUpToolbar() = binding {
        analyticsToolbar.titleTextView.text = requireContext().getString(R.string.analytics_option)
    }

    private fun setUpClicks() = binding {
        analyticsToolbar.iconBackImageView.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun setUpViewPager() = binding {
        analyticsViewPager.adapter = analyticsViewPagerAdapter
        TabLayoutMediator(analyticsTabLayout, analyticsViewPager) { tab, position ->
            val tabTitle = if (position == 0) {
                requireContext().getString(R.string.go_analyze)
            } else {
                requireContext().getString(R.string.stored_analytics)
            }
            tab.text = tabTitle
        }.attach()
    }

    override fun renderState(state: AnalyticsState) {

    }

    override fun handleSideEffect(sideEffect: AnalyticsEffect) {
        when (sideEffect) {
            AnalyticsEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
        }
    }
}