package spiral.bit.dev.dailymood.ui.feature.main.models

import androidx.annotation.StringRes
import com.hadi.emojiratingbar.RateStatus
import spiral.bit.dev.dailymood.R

enum class MoodType(
    @StringRes val wellBeing: Int,
    @StringRes val feelAdverb: Int,
    val smileyRating: RateStatus
) {
    HAPPY(R.string.happy_well_being, R.string.happy_feel_adverb, RateStatus.GREAT),
    GOOD(R.string.good_well_being, R.string.good_feel_adverb, RateStatus.GOOD),
    NEUTRAL(R.string.neutral_well_being, R.string.neutral_feel_adverb, RateStatus.OKAY),
    SAD(R.string.sad_well_being, R.string.sad_feel_adverb, RateStatus.BAD),
    ANGRY(R.string.angry_will_being, R.string.angry_feel_adverb, RateStatus.AWFUL),
    NOT_DEFINED(R.string.not_defined_will_being, R.string.not_recognized_feel_adverb, RateStatus.GREAT)
}
