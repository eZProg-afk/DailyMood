package spiral.bit.dev.dailymood.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import spiral.bit.dev.dailymood.data.analytics.AnalyticsDao
import spiral.bit.dev.dailymood.data.analytics.AnalyticsEntity
import spiral.bit.dev.dailymood.data.converters.QuestionsTypeConverter
import spiral.bit.dev.dailymood.data.converters.AudioTypeConverter
import spiral.bit.dev.dailymood.data.converters.MoodEntitiesTypeConverter
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodDao

//TODO CHANGE BD VERSION TO 1
@TypeConverters(MoodEntitiesTypeConverter::class, AudioTypeConverter::class, QuestionsTypeConverter::class)
@Database(entities = [MoodEntity::class, AnalyticsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emotionDao(): MoodDao
    abstract fun analyticsDao(): AnalyticsDao
}