package spiral.bit.dev.dailymood.data.emotion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotions")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val emotionId: Long = 0,
    val emotionType: Float,
    val note: String? = null,
    val photoPath: String? = null,
    val createdTime: Long = System.currentTimeMillis()
)