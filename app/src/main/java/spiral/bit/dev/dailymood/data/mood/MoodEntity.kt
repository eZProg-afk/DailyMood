package spiral.bit.dev.dailymood.data.mood

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import spiral.bit.dev.dailymood.data.models.*

@Entity(tableName = "emotions")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moodValue: Float,
    val createdTime: Long = System.currentTimeMillis(),
    val creationType: CreationType,
    @Embedded val manuallyData: ManuallyData? = null,
    @Embedded val photoData: PhotoData? = null,
    @Embedded val audioData: AudioData? = null,
    @Embedded val surveyData: SurveyData? = null
)