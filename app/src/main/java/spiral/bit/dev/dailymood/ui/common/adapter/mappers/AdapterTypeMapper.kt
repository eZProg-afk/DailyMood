package spiral.bit.dev.dailymood.ui.common.adapter.mappers

import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodItem

class AdapterTypeMapper {

    fun toMoodItems(moodEntities: List<MoodEntity>) = moodEntities.map { MoodItem(it) }
}