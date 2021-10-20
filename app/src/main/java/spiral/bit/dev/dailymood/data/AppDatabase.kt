package spiral.bit.dev.dailymood.data

import androidx.room.Database
import androidx.room.RoomDatabase
import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.data.emotion.EmotionDao

//TODO CHANGE BD VERSION TO 1
@Database(entities = [EmotionItem::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emotionDao(): EmotionDao
}