package spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.models.mvi

data class SurveyState(
    val isRepeatSurvey: Boolean,
    val questionCounter: Int,
    val question: String,
    val answers: List<String>,
    val scores: List<Int>
)