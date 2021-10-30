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

    @Query("SELECT * FROM emotions WHERE note LIKE '%' || :searchQuery || '%' ORDER BY id DESC, note")
    fun getEmotionsBySearchQuery(searchQuery: String): Flow<List<MoodEntity>>

    //TODO 'MOOD TYPE'

    @Query("SELECT * FROM emotions WHERE moodType == 'HAPPY'")
    fun getAllHappyEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE moodType == 'NEUTRAL'")
    fun getAllNeutralEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE moodType == 'SAD'")
    fun getAllSadEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE moodType == 'ANGRY'")
    fun getAllAngryEmotions(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM emotions WHERE id == :emotionId")
    fun getEmotionById(emotionId: Long): MoodEntity
}