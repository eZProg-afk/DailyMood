package spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers

class ScoreProvider {

    fun getScore(answerNumber: Int): Int {
        return when (answerNumber) {
            1 -> 0
            2 -> 1
            3 -> 2
            4 -> 3
            5 -> 4
            else -> 0
        }
    }
}