package spiral.bit.dev.dailymood.data.emotion

import javax.inject.Inject

class EmotionRepository @Inject constructor(private val emotionDao: EmotionDao) {

    suspend fun insert(emotionItem: EmotionItem) = emotionDao.insert(emotionItem)

    suspend fun delete(emotionItem: EmotionItem) = emotionDao.delete(emotionItem)

    fun getEmotionsByDate(dayStart: Long, dayEnd: Long) =
        emotionDao.getEmotionsByDate(dayStart, dayEnd)

    fun getEmotionsBySearchQuery(searchQuery: String) =
        emotionDao.getEmotionsBySearchQuery(searchQuery)

    fun getAllHappyEmotions() = emotionDao.getAllHappyEmotions()

    fun getAllNeutralEmotions() = emotionDao.getAllNeutralEmotions()

    fun getAllSadEmotions() = emotionDao.getAllSadEmotions()

    fun getAllAngryEmotions() = emotionDao.getAllAngryEmotions()

    fun getEmotionById(emotionId: Long) = emotionDao.getEmotionById(emotionId)
}