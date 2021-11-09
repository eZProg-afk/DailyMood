package spiral.bit.dev.dailymood.ui.feature.analytics.view

import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.analytics.models.mvi.AnalyticsEffect
import spiral.bit.dev.dailymood.ui.feature.analytics.models.mvi.AnalyticsState
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : BaseViewModel<AnalyticsState, AnalyticsEffect>() {

    override val container = container<AnalyticsState, AnalyticsEffect>(AnalyticsState(0))

    fun navigateBack() = intent {
        postSideEffect(AnalyticsEffect.NavigateBack)
    }
}