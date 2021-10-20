package spiral.bit.dev.dailymood.ui.common.mappers

import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.ui.feature.main.models.EmotionType
import spiral.bit.dev.dailymood.ui.feature.main.models.EmotionUiItem

class EmotionTypeMapper {

    fun mapToEmotionType(moodValue: Float): EmotionType {
        return when (moodValue) {
            -1.0F -> EmotionType.ANGRY
            -0.5F -> EmotionType.SAD
            0F -> EmotionType.NEUTRAL
            0.5F -> EmotionType.GOOD
            1.0F -> EmotionType.HAPPY
            else -> EmotionType.NOT_DEFINED
        }
    }

    fun mapToMoodValue(emotionType: EmotionType): Float {
        return when (emotionType) {
            EmotionType.HAPPY -> 1.0F
            EmotionType.GOOD -> 0.5F
            EmotionType.NEUTRAL -> 0F
            EmotionType.SAD -> -0.5F
            EmotionType.ANGRY -> -1.0F
            EmotionType.NOT_DEFINED -> -2F
        }
    }

    fun toEmotionItems(emotionItems: List<EmotionItem>): List<EmotionUiItem> =
        emotionItems.map { it.toEmotionMainItem() }

    private fun EmotionItem.toEmotionMainItem() = EmotionUiItem(this)
}