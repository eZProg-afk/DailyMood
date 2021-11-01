package spiral.bit.dev.dailymood.ui.common.mappers

import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodItem

class EmotionTypeMapper {

    fun mapToEmotionType(moodValue: Float): MoodType {
        return when  {
            moodValue <= -0.7 -> MoodType.ANGRY
            moodValue <= -0.3 -> MoodType.SAD
            moodValue <= 0.3 -> MoodType.NEUTRAL
            moodValue <= 0.7 -> MoodType.GOOD
            moodValue <= 1.0 -> MoodType.HAPPY
            else -> MoodType.NOT_DEFINED
        }
    }

    fun mapToMoodValue(moodType: MoodType): Float {
        return when (moodType) {
            MoodType.HAPPY -> 1.0F
            MoodType.GOOD -> 0.5F
            MoodType.NEUTRAL -> 0F
            MoodType.SAD -> -0.5F
            MoodType.ANGRY -> -1.0F
            MoodType.NOT_DEFINED -> -2F
        }
    }

    fun toEmotionItems(moodEntities: List<MoodEntity>): List<MoodItem> =
        moodEntities.map { it.toEmotionMainItem() }

    private fun MoodEntity.toEmotionMainItem() = MoodItem(this)
}