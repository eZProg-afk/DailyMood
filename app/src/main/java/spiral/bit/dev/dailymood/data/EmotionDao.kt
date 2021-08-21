package spiral.bit.dev.dailymood.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emotion: Emotion): Long

    @Delete
    suspend fun delete(emotion: Emotion)

    @Query("SELECT * FROM emotions WHERE createdTime BETWEEN :dayStart AND :dayEnd")
    fun getEmotionsByDate(dayStart: Long, dayEnd: Long): Flow<List<Emotion>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'Happy' AND createdTime BETWEEN :monthDayStart AND :monthDayEnd ")
    fun getAllHappyEmotions(monthDayStart: Long, monthDayEnd: Long): Flow<List<Emotion>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'Neutral' AND createdTime BETWEEN :monthDayStart AND :monthDayEnd")
    fun getAllNeutralEmotions(monthDayStart: Long, monthDayEnd: Long): Flow<List<Emotion>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'Sad' AND createdTime BETWEEN :monthDayStart AND :monthDayEnd")
    fun getAllSadEmotions(monthDayStart: Long, monthDayEnd: Long): Flow<List<Emotion>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'Angry' AND createdTime BETWEEN :monthDayStart AND :monthDayEnd")
    fun getAllAngryEmotions(monthDayStart: Long, monthDayEnd: Long): Flow<List<Emotion>>
}