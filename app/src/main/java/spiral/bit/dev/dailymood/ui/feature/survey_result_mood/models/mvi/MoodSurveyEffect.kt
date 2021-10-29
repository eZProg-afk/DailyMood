package spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class MoodSurveyEffect : SideEffectMarker {
    object NavigateToSurvey : MoodSurveyEffect()
    object NavigateToMain : MoodSurveyEffect()
}