package spiral.bit.dev.dailymood.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Emotion::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emotionDao(): EmotionDao
}