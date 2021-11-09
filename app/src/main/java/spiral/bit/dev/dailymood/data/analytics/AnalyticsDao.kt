package spiral.bit.dev.dailymood.data.analytics

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalyticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(analyticsEntity: AnalyticsEntity)

    @Delete
    suspend fun delete(analyticsEntity: AnalyticsEntity)

    @Query("SELECT * FROM analytics")
    fun getAnalytics(): Flow<List<AnalyticsEntity>>
}