package spiral.bit.dev.dailymood.ui.feature.analytics.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class AnalyticsEffect : SideEffectMarker {
    object NavigateBack : AnalyticsEffect()
}