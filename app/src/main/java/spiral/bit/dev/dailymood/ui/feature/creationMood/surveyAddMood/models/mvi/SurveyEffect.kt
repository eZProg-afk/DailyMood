package spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.models.mvi

import androidx.annotation.StringRes

sealed class SurveyEffect {
    class NavigateToSurveyResult(val scores: IntArray) : SurveyEffect()
    object NavigateBack : SurveyEffect()
    object NavigateToMain : SurveyEffect()
    class Toast(@StringRes val msg: Int): SurveyEffect()
}