package spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models.mvi

sealed class MoodSurveyEffect {
    object NavigateToSurvey : MoodSurveyEffect()
    object NavigateToMain : MoodSurveyEffect()
}