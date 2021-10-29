package spiral.bit.dev.dailymood.data.emotion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodEntity: MoodEntity): Long

    @Delete
    suspend fun delete(moodEntity: MoodEntity)

    @Query("SELECT * FROM emotions WHERE createdTime BETWEEN :dayStart AND :dayEnd")
    fun getEmotionsByDate(dayStart: Long, dayEnd: Long): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE note LIKE '%' || :searchQuery || '%' ORDER BY emotionId DESC, note")
    fun getEmotionsBySearchQuery(searchQuery: String): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'HAPPY'")
    fun getAllHappyEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'NEUTRAL'")
    fun getAllNeutralEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'SAD'")
    fun getAllSadEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'ANGRY'")
    fun getAllAngryEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE emotionId == :emotionId")
    fun getEmotionById(emotionId: Long): MoodEntity
}