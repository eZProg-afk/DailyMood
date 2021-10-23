package spiral.bit.dev.dailymood.ui.feature.main.models

import com.hsalf.smileyrating.SmileyRating

enum class MoodType(val translate: String, val smileyRating: SmileyRating.Type) {
    HAPPY("Я счастливый", SmileyRating.Type.GREAT),
    GOOD("Я радостный", SmileyRating.Type.GOOD),
    NEUTRAL("Я нейтрален", SmileyRating.Type.OKAY),
    SAD("Я грущу", SmileyRating.Type.BAD),
    ANGRY("Я злой", SmileyRating.Type.TERRIBLE),
    NOT_DEFINED("Не объявлено", SmileyRating.Type.NONE)
}
