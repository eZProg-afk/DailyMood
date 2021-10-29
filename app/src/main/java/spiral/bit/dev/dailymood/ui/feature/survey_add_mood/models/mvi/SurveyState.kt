package spiral.bit.dev.dailymood.ui.feature.survey_add_mood.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker

data class SurveyState(
    val isRepeatSurvey: Boolean,
    val questionCounter: Int,
    val question: String,
    val answers: List<String>,
    val scores: List<Int>
) : StateMarker