package spiral.bit.dev.dailymood.data.emotion

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import spiral.bit.dev.dailymood.ui.feature.main.models.EmotionType
import java.text.SimpleDateFormat

@Parcelize
@Entity(tableName = "emotions", indices = [Index("emotion_id", unique = true)])
data class EmotionItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "emotion_id")
    val emotionId: Long = 0,
    val emotionType: Float,
    val note: String = "",
    val category: String = "",
    val createdTime: Long = System.currentTimeMillis(),
    val photoPath: String = ""
) : Parcelable {
    val formattedTime: String
        get() =
            SimpleDateFormat.getInstance().format(createdTime)
}