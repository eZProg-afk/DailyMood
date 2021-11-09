package spiral.bit.dev.dailymood.ui.common.adapter.mappers

import spiral.bit.dev.dailymood.data.analytics.AnalyticsEntity
import spiral.bit.dev.dailymood.data.models.CreationType
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem
import spiral.bit.dev.dailymood.ui.feature.analytics.storeAnalytics.models.AnalyticsItem
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.ManuallyMoodItem
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.PhotoMoodItem
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.SurveyMoodItem
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.VoiceMoodItem
import javax.inject.Inject

class AdapterTypeMapper @Inject constructor() {

    private fun toManuallyMoodItem(moodEntity: MoodEntity) = ManuallyMoodItem(moodEntity)

    private fun toSurveyMoodItem(moodEntity: MoodEntity) = SurveyMoodItem(moodEntity)

    private fun toPhotoMoodItem(moodEntity: MoodEntity) = PhotoMoodItem(moodEntity)

    private fun toVoiceMoodItem(moodEntity: MoodEntity) = VoiceMoodItem(moodEntity)

    fun toMoodEntities(manuallyMoodItems: List<ManuallyMoodItem>) =
        manuallyMoodItems.map { it.moodEntity }

    fun toAnalyticItems(analyticEntities: List<AnalyticsEntity>) =
        analyticEntities.map { AnalyticsItem(it) }

    fun toAnalyticEntities(analyticsItem: List<AnalyticsItem>) =
        analyticsItem.map { it.analyticsEntity }

    fun resolveMoodItem(moodEntities: List<MoodEntity>): List<ListItem> {
        return moodEntities.map { moodEntity ->
            when(moodEntity.creationType) {
                CreationType.BY_PHOTO -> toPhotoMoodItem(moodEntity)
                CreationType.BY_SURVEY -> toSurveyMoodItem(moodEntity)
                CreationType.BY_VOICE -> toVoiceMoodItem(moodEntity)
                else -> toManuallyMoodItem(moodEntity)
            }
        }
    }

    fun resolveListItem(item: ListItem): Result<MoodEntity> {
        return when (item) {
            is ManuallyMoodItem -> Result.success(item.moodEntity)
            is SurveyMoodItem -> Result.success(item.moodEntity)
            is PhotoMoodItem -> Result.success(item.moodEntity)
            is VoiceMoodItem -> Result.success(item.moodEntity)
            else -> Result.failure(UnknownItemException("Item unknown: ${item.javaClass}"))
        }
    }

    fun <T> concatenate(vararg lists: List<T>): List<T> {
        return listOf(*lists).flatten()
    }

    companion object {
        data class UnknownItemException(val msg: String) : IllegalStateException(msg)
    }
}