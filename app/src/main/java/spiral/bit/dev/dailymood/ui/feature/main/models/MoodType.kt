package spiral.bit.dev.dailymood.ui.feature.main.models

import com.hadi.emojiratingbar.RateStatus

enum class MoodType(val translate: String, val smileyRating: RateStatus) {
    HAPPY("Я счастливый", RateStatus.GREAT),
    GOOD("Я радостный", RateStatus.GOOD),
    NEUTRAL("Я нейтрален", RateStatus.OKAY),
    SAD("Я грущу",  RateStatus.BAD),
    ANGRY("Я злой",  RateStatus.AWFUL),
    NOT_DEFINED("Не объявлено", RateStatus.GREAT)
}
