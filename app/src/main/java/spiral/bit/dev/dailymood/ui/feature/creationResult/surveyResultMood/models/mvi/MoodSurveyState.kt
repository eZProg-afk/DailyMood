package spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi

import spiral.bit.dev.dailymood.data.models.SurveyData
import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.SurveyResultItem

data class MoodSurveyState(
    val surveyResultList: List<SurveyResultItem>,
    val surveyData: SurveyData?,
    val moodValue: MoodType?
) : StateMarker