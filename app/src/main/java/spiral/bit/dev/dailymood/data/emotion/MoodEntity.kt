package spiral.bit.dev.dailymood.data.emotion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotions")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moodType: Float,
    val reason: String? = null,
    val note: String? = null,
    val photoPath: String? = null,
    val createdTime: Long = System.currentTimeMillis()
)