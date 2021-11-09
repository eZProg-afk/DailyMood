package spiral.bit.dev.dailymood.ui.common.resolvers

import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType

class AnalyticsResolver {

    fun resolveMoodType(moodValue: Double): MoodType {
        Logger.logDebug("${(moodValue * 2)} SAOSOJSOJSOJ")
        return when ((moodValue * 100)) {
            in 0F..0.2F -> MoodType.ANGRY
            in 0.2F..0.6F -> MoodType.SAD
            in 20F..25F -> MoodType.NEUTRAL
            in 25F..35F -> MoodType.GOOD
            in 35F..50F -> MoodType.HAPPY
            else -> MoodType.ANGRY
        }
    }

    fun resolveWish(moodType: MoodType): Int {
        return when (moodType) {
            MoodType.HAPPY -> R.string.happy_wish
            MoodType.GOOD -> R.string.good_wish
            MoodType.NEUTRAL -> R.string.neutral_wish
            MoodType.SAD -> R.string.sad_wish
            MoodType.ANGRY -> R.string.angry_wish
            else -> R.string.happy_wish
        }
    }

    fun resolveMoodDrawableResource(moodType: MoodType): Int {
       return when (moodType) {
            MoodType.HAPPY -> R.drawable.ic_emotion_happy
            MoodType.GOOD -> R.drawable.ic_emotion_good
            MoodType.NEUTRAL -> R.drawable.ic_emotion_neutral
            MoodType.SAD -> R.drawable.ic_emotion_sad
            MoodType.ANGRY -> R.drawable.ic_emotion_sad
            else -> R.drawable.ic_emotion
        }
    }
}