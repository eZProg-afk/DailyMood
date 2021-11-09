package spiral.bit.dev.dailymood.data.analytics

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.feature.analytics.models.AnalyticsSortOrder

@Entity(tableName = "analytics")
data class AnalyticsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moodEntities: List<MoodEntity>,
    val period: AnalyticsSortOrder,
    @StringRes val averageMoodType: Int,
    val createdTime: Long = System.currentTimeMillis()
)