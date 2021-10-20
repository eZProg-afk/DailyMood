package spiral.bit.dev.dailymood.data.emotion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emotionItem: EmotionItem): Long

    @Delete
    suspend fun delete(emotionItem: EmotionItem)

    @Query("SELECT * FROM emotions WHERE createdTime BETWEEN :dayStart AND :dayEnd")
    fun getEmotionsByDate(dayStart: Long, dayEnd: Long): Flow<List<EmotionItem>>

    @Query("SELECT * FROM emotions WHERE note LIKE '%' || :searchQuery || '%' ORDER BY emotion_id DESC, note")
    fun getEmotionsBySearchQuery(searchQuery: String): Flow<List<EmotionItem>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'HAPPY'")
    fun getAllHappyEmotions(): Flow<List<EmotionItem>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'NEUTRAL'")
    fun getAllNeutralEmotions(): Flow<List<EmotionItem>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'SAD'")
    fun getAllSadEmotions(): Flow<List<EmotionItem>>

    @Query("SELECT * FROM emotions WHERE emotionType == 'ANGRY'")
    fun getAllAngryEmotions(): Flow<List<EmotionItem>>

    @Query("SELECT * FROM emotions WHERE emotion_id == :emotionId")
    fun getEmotionById(emotionId: Long): EmotionItem
}