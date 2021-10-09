package spiral.bit.dev.dailymood.data

import javax.inject.Inject

class EmotionRepository @Inject constructor(private val emotionDao: EmotionDao) {

    suspend fun insert(emotion: Emotion) = emotionDao.insert(emotion)

    suspend fun delete(emotion: Emotion) = emotionDao.delete(emotion)

    fun getEmotionsByDate(dayStart: Long, dayEnd: Long) =
        emotionDao.getEmotionsByDate(dayStart, dayEnd)

    fun getAllHappyEmotions() = emotionDao.getAllHappyEmotions()

    fun getAllHappyEmotionsByDate(monthDayStart: Long, monthDayEnd: Long) =
        emotionDao.getAllHappyEmotionsByDate(monthDayStart, monthDayEnd)

    fun getAllNeutralEmotions(monthDayStart: Long, monthDayEnd: Long) =
        emotionDao.getAllNeutralEmotions(monthDayStart, monthDayEnd)

    fun getAllSadEmotions(monthDayStart: Long, monthDayEnd: Long) =
        emotionDao.getAllSadEmotions(monthDayStart, monthDayEnd)

    fun getAllAngryEmotions(monthDayStart: Long, monthDayEnd: Long) =
        emotionDao.getAllAngryEmotions(monthDayStart, monthDayEnd)
}