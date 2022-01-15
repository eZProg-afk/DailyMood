package spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi

import spiral.bit.dev.dailymood.data.models.SurveyData
import spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.SurveyResultItem
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType

data class MoodSurveyState(val surveyResultList: List<SurveyResultItem>, val surveyData: SurveyData?, val moodValue: MoodType?)