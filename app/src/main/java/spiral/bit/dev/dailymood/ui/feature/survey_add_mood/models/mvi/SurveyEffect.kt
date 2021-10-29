package spiral.bit.dev.dailymood.ui.feature.survey_add_mood.models.mvi

import androidx.annotation.StringRes
import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class SurveyEffect : SideEffectMarker {
    class NavigateToSurveyResult(val scores: IntArray) : SurveyEffect()
    class Toast(@StringRes val msg: Int): SurveyEffect()
}