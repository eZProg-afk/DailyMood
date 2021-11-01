package spiral.bit.dev.dailymood.data.mood

import javax.inject.Inject

class MoodRepository @Inject constructor(private val moodDao: MoodDao) {

    suspend fun insert(moodEntity: MoodEntity) = moodDao.insert(moodEntity)

    suspend fun delete(moodEntity: MoodEntity) = moodDao.delete(moodEntity)

    fun getEmotionsByDate(dayStart: Long, dayEnd: Long) =
        moodDao.getEmotionsByDate(dayStart, dayEnd)

    fun getEmotionsBySearchQuery(searchQuery: String) =
        moodDao.getEmotionsBySearchQuery(searchQuery)

    fun getAllHappyEmotions() = moodDao.getAllHappyEmotions()

    fun getAllNeutralEmotions() = moodDao.getAllNeutralEmotions()

    fun getAllSadEmotions() = moodDao.getAllSadEmotions()

    fun getAllAngryEmotions() = moodDao.getAllAngryEmotions()

    fun getEmotionById(emotionId: Long) = moodDao.getEmotionById(emotionId)
}