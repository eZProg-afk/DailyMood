package spiral.bit.dev.dailymood.ui.feature.analytics.analyticsContainer.models.mvi

sealed class AnalyticsEffect {
    object NavigateBack : AnalyticsEffect()
}