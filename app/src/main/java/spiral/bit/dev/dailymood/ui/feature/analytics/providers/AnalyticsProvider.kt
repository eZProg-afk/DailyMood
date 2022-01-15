package spiral.bit.dev.dailymood.ui.feature.analytics.providers

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.common.adapter.mappers.AdapterTypeMapper
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.AnalyticsResolver
import spiral.bit.dev.dailymood.ui.feature.analytics.analyticsContainer.models.AverageAnalyticsItem
import spiral.bit.dev.dailymood.ui.feature.analytics.analyticsContainer.models.AnalyticsSortOrder
import spiral.bit.dev.dailymood.ui.feature.analytics.analyticsContainer.models.SectionItem
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.EmptyDayItem
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.ManuallyMoodItem
import javax.inject.Inject

class AnalyticsProvider @Inject constructor(
    private val adapterTypeMapper: AdapterTypeMapper,
    private val analyticsResolver: AnalyticsResolver,
    private val moodTypeMapper: MoodTypeMapper,
    @ApplicationContext private val context: Context
) {

    fun provideAnalytics(
        manuallyMoodItems: List<ManuallyMoodItem>,
        analyticsSortOrder: AnalyticsSortOrder
    ): List<ListItem> {
        return if (manuallyMoodItems.isEmpty()) emptyList() else getAnalytics(manuallyMoodItems, analyticsSortOrder)
    }

    private fun emptyList(): List<ListItem> {
        return listOf(
            EmptyDayItem(
                R.string.analytics_assets_path,
                R.string.empty_day_analytics_title,
                R.string.empty_day_analytics_hint
            )
        )
    }

    private fun filledList(
        averageAverageAnalyticsItems: List<AverageAnalyticsItem>,
        manuallyMoodItems: List<ManuallyMoodItem>
    ): List<ListItem> {
        val sectionOneItems = listOf(SectionItem(R.string.mood_items))
        val sectionTwoItems = listOf(SectionItem(R.string.average_mood_items))
        return adapterTypeMapper.concatenate(
            sectionOneItems,
            manuallyMoodItems,
            sectionTwoItems,
            averageAverageAnalyticsItems
        )
    }

    private fun getAnalytics(
        manuallyMoodItems: List<ManuallyMoodItem>,
        analyticsSortOrder: AnalyticsSortOrder
    ): List<ListItem> {
        val averageMoodValue = manuallyMoodItems.toTypedArray().map { it.moodEntity.moodValue }.average()
        val moodType = analyticsResolver.resolveMoodType(averageMoodValue)
        val moodDrawableResource = analyticsResolver.resolveMoodDrawableResource(moodType)
        val wish = analyticsResolver.resolveWish(moodType)
        val averageItemsList = listOf(
            AverageAnalyticsItem(
                moodType.wellBeing,
                moodDrawableResource,
                wish,
                context.getString(analyticsSortOrder.periodName)
            )
        )
        return filledList(averageItemsList, manuallyMoodItems)
    }

    fun getMoodPercentage(moodType: MoodType, manuallyMoodItems: List<ManuallyMoodItem>): Float {
        val count = manuallyMoodItems.filter {
            moodTypeMapper.mapToMoodType(it.moodEntity.moodValue) == moodType
        }.count()
        return (count * 100).toFloat() / manuallyMoodItems.count()
    }
}